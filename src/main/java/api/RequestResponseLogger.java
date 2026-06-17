package api;

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
 * with pretty-printed JSON bodies for better debugging and reporting.
 */
@Slf4j
public class RequestResponseLogger implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        logRequest(requestSpec);

        Response response = null;
        try {
            response = filterContext.next(requestSpec, responseSpec);
            logResponse(response);
        } catch (Exception e) {
            log.error("API request failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return response;
    }

    private void logRequest(FilterableRequestSpecification requestSpec) {
        URI uri = URI.create(requestSpec.getURI());
        String requestBody = requestSpec.getBody() != null
                ? formatBody(requestSpec.getBody().toString())
                : "";

        System.out.println("================== REQUEST ==================");
        System.out.println(requestSpec.getMethod() + " " + uri.getPath());
        System.out.println("Host: " + uri.getHost());
        System.out.println(requestSpec.getHeaders());
        System.out.println("Content-Length: " + requestBody.length());
        if (!requestBody.isEmpty()) {
            System.out.println(requestBody);
        }
        System.out.println("----------------------------------------------");
    }

    private void logResponse(Response response) {
        String responseBody = Optional.ofNullable(response.getBody())
                .map(ResponseBody::asString)
                .orElse("");

        System.out.println("================== RESPONSE ==================");
        System.out.println("HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusLine());
        System.out.println("Content-Type: " + response.getContentType());
        System.out.println("Content-Length: " + responseBody.length());
        if (!responseBody.isEmpty()) {
            System.out.println("");
            System.out.println(formatBody(responseBody));
        }
        System.out.println("----------------------------------------------");
    }

    private String formatBody(String body) {
        // Try to pretty-print JSON; fallback to raw string
        try {
            return new com.google.gson.GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()
                    .toJson(com.google.gson.JsonParser.parseString(body));
        } catch (Exception e) {
            return body;
        }
    }
}
