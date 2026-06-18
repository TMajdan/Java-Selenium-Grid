package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Optional;

/**
 * Custom RestAssured filter that logs all HTTP requests and responses
 * with pretty-printed JSON bodies for debugging and attaches them to Allure reports.
 */
@Slf4j
public class RequestResponseLogger implements Filter {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext filterContext) {
        String requestLog = buildRequestLog(requestSpec);
        log.debug(requestLog);
        Allure.addAttachment("API Request", "text/plain", requestLog);

        Response response;
        try {
            response = filterContext.next(requestSpec, responseSpec);
        } catch (Exception e) {
            log.error("API request failed: {}", e.getMessage());
            throw new RuntimeException("API request failed", e);
        }

        String responseLog = buildResponseLog(response);
        log.debug(responseLog);
        Allure.addAttachment("API Response", "text/plain", responseLog);

        return response;
    }

    private String buildRequestLog(FilterableRequestSpecification requestSpec) {
        URI uri = URI.create(requestSpec.getURI());
        String requestBody = requestSpec.getBody() != null
                ? formatBody(requestSpec.getBody().toString())
                : "";

        StringBuilder sb = new StringBuilder();
        sb.append("================== REQUEST ==================\n");
        sb.append(requestSpec.getMethod()).append(" ").append(uri.getPath()).append("\n");
        sb.append("Host: ").append(uri.getHost()).append("\n");
        sb.append(requestSpec.getHeaders()).append("\n");
        if (!requestBody.isEmpty()) {
            sb.append("\n").append(requestBody).append("\n");
        }
        sb.append("----------------------------------------------\n");
        return sb.toString();
    }

    private String buildResponseLog(Response response) {
        String responseBody = Optional.ofNullable(response.getBody())
                .map(ResponseBody::asString)
                .orElse("");

        StringBuilder sb = new StringBuilder();
        sb.append("================== RESPONSE ==================\n");
        sb.append("HTTP/1.1 ").append(response.getStatusCode())
                .append(" ").append(response.getStatusLine()).append("\n");
        sb.append("Content-Type: ").append(response.getContentType()).append("\n");
        if (!responseBody.isEmpty()) {
            sb.append("\n").append(formatBody(responseBody)).append("\n");
        }
        sb.append("----------------------------------------------\n");
        return sb.toString();
    }

    private String formatBody(String body) {
        try {
            Object json = MAPPER.readValue(body, Object.class);
            return MAPPER.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            return body; // fallback: raw string
        }
    }
}