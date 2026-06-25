package api.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class CustomRequestResponseLogger implements Filter {

    private static final String NPE_MESSAGE = """
            \n
            1. ### Check if test class implements ITestListener with overridden onTestStart() method, for initializing ManualRestDocumentation ###
            2. ### Check if documentation context for requests outside @Test methods was properly initialized and closed -> check if @BeforeClass method contains request which requires documentation  ###
            3. ### Check if documentation context for Static utility methods was properly initialized and closed within these utils ###
            """;

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        logRequest(filterableRequestSpecification);

        Response response = null;
        try {
            response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
            logResponse(response);
        } catch (NullPointerException e) {
            log.error(NPE_MESSAGE, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private void logRequest(FilterableRequestSpecification requestSpec) {
        URI uri = URI.create(requestSpec.getURI().replace("?", "%3F")); // special characters inside a path need to be URL encoded
        String requestBody = requestSpec.getBody() != null ? formatJson(requestSpec.getBody().toString()) : "";

        System.out.println("================== REQUEST ==================");
        logAuthInfo(requestSpec);
        System.out.println("");
        System.out.println(requestSpec.getMethod() + " " + uri.getPath());
        System.out.println("Host: " + uri.getHost() + ":" + uri.getPort());
        System.out.println(requestSpec.getHeaders());
        System.out.println("Content-Length: " + requestBody.length());
        System.out.println(requestBody);
        System.out.println("----------------------------------------------");
    }

    private void logResponse(Response response) {
        String ct = response.getContentType().toLowerCase();
        String responseBody = Optional.ofNullable(response.getBody()).map(body -> body.asString()).orElse("");
        String[] responseLines = responseBody.split("\n");

        System.out.println("================== RESPONSE ==================");
        System.out.println("HTTP/1.1 " + response.getStatusCode());
        System.out.println("Content-Type: " + ct);
        System.out.println("Content-Length: " + responseBody.length());
        System.out.println("Date: " + formatHttpDate(response.getHeader("Date")));
        System.out.println("");
        printTruncatedResponseBody(ct, responseLines, responseBody);
        System.out.println("----------------------------------------------");
    }

    private String formatJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            log.error("Malformed JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse JSON", e);
        }

        return gson.toJson(jsonElement);
    }

    private void printTruncatedResponseBody(String contentType, String[] responseLines, String responseBody) {
        if (contentType.contains("csv") || contentType.contains("ndjson")) {
            int limit = 50;
            for (int i = 0; i < Math.min(responseLines.length, limit); i++) {
                System.out.println(responseLines[i]);
            }
            System.out.println("... (truncated, total responseLines: " + responseLines.length + ")");
        } else {
            System.out.println(formatJson(responseBody));
        }
    }

    private String formatHttpDate(String httpDate) {
        if (httpDate == null || httpDate.isEmpty()) {
            return "N/A";
        }
        try {
            return ZonedDateTime.parse(httpDate, DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.ENGLISH))
                                .withZoneSameInstant(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH));
        } catch (Exception e) {
            log.warn("Failed to parse HTTP date: {}", httpDate, e);
            return "N/A";
        }
    }

    private void logAuthInfo(FilterableRequestSpecification requestSpec) {
        if (requestSpec.getHeaders().hasHeaderWithName("API-KEY")) {
            System.out.println("Authorization: API-KEY Header");
            return;
        }
        if (requestSpec.getHeaders().hasHeaderWithName("Authorization")) {
            try {
                String auth = requestSpec.getHeaders().getValue("Authorization");
                if (!auth.startsWith("Basic ")) {
                    System.out.println("Authorization: " + auth);
                    return;
                }
                String decoded = new String(java.util.Base64.getDecoder().decode(auth.substring(6)));
                String[] parts = decoded.split(":", 2);
                System.out.println("Authorization: Using: '" + parts[0] + "' user with '" + parts[1] + "' password");
            } catch (Exception e) {
                System.out.println("Cannot decode Authorization header: " + e.getMessage());
            }
            return;
        }
        System.out.println("Authorization: None");
    }
}