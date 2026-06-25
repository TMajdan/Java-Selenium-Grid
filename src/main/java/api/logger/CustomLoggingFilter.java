package api.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import general.common.Credentials;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CustomLoggingFilter implements Filter {

    private Credentials credentials;
    private boolean isTestStarted = false;

    public void resetTestStartedFlag() {
        isTestStarted = false;
    }

    public CustomLoggingFilter(Credentials initialCredentials) {
        this.credentials = initialCredentials;
    }

    public void updateCredentials(Credentials newCredentials) {
        this.credentials = newCredentials;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        String testName = LoggerListener.getCurrentTestName();
        String formattedTestName = formatTestNameWithParameters(testName, Arrays.stream(LoggerListener.getCurrentTestParameters()).filter(Objects::nonNull).toArray(Object[]::new));
        String methodName = LoggerListener.getCurrentTestMethodName();

        if (!isTestStarted && methodName != null && !methodName.isEmpty()) {
            logTestInfo(methodName, formattedTestName);
            isTestStarted = true;
        }

        // Add Authorization header only if credentials are provided
        // Authorization header is added only for snippet generation purposes (e.g., curl examples).
        if (isCredentialsProvided()) {
            String credentialsString = credentials.getUsername() + ":" + credentials.getPassword();
            String encodedCredentials = Base64.getEncoder().encodeToString(credentialsString.getBytes(StandardCharsets.UTF_8));
            requestSpec.header("Authorization", "Basic " + encodedCredentials);
        }

        logRequest(requestSpec);

        Response response = ctx.next(requestSpec, responseSpec);
        logResponse(response);

        return response;
    }

    private String formatTestNameWithParameters(String testName, Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return testName;
        }
        return testName + "[" + String.join(", ", Arrays.stream(parameters).map(p -> p.toString()).toArray(String[]::new)) + "]";
    }

    private void logTestInfo(String methodName, String description) {
        log("================== TEST =====================");
        log("Running test: " + methodName);
        log("Description: " + description);
        log("---------------------------------------------");
    }

    private void logRequest(FilterableRequestSpecification requestSpec) {
        URI uri = URI.create(requestSpec.getURI().replace("?", "%3F")); // special characters inside a path need to be URL encoded
        String requestBody = requestSpec.getBody() != null ? formatJson(requestSpec.getBody().toString()) : "";

        log("================== REQUEST ==================");
        // Log credentials only if they are provided; otherwise indicate that 'No Auth' request is used
        if (isCredentialsProvided()) {
            log("Using: '" + credentials.getUsername() + "' with '" + credentials.getPassword() + "' password\n");
        } else {
            log("Executing request with No Auth authorization\n");
        }

        log(requestSpec.getMethod() + " " + uri.getPath());
        if (uri.getPort() == -1) { // -1 indicates that the port is not explicitly specified in the URI
            log("Host: " + uri.getHost());
        } else {
            log("Host: " + uri.getHost() + ":" + uri.getPort());
        }
        log("Content-Type: " + requestSpec.getHeaders().getValue("Content-Type"));
        log("Content-Length: " + requestBody.length());
        if (requestSpec.getHeaders().hasHeaderWithName("API-KEY")) {
            log("API-KEY: " + requestSpec.getHeaders().getValue("API-KEY"));
        }
        log(requestBody);
        log("----------------------------------------------");
    }

    private void logResponse(Response response) {
        String ct = response.getContentType().toLowerCase();
        String responseBody = Optional.ofNullable(response.getBody()).map(body -> body.asString()).orElse("");
        String[] responseLines = responseBody.split("\n");

        log("================== RESPONSE ==================");
        log("HTTP/1.1 " + response.getStatusCode());
        log("Content-Type: " + ct);
        log("Content-Length: " + responseBody.length());
        log("Date: " + formatHttpDate(response.getHeader("Date")));
        log("");
        printTruncatedResponseBody(ct, responseLines, responseBody);
        log("----------------------------------------------");
    }

    private void log(String message) {
        System.out.println(message);
    }

    private String formatJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        // Parse JSON directly using parseString (handles lenient parsing internally)
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
                log(responseLines[i]);
            }
            log("... (truncated, total responseLines: " + responseLines.length + ")");
        } else {
            log(formatJson(responseBody));
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

    private boolean isCredentialsProvided() {
        return credentials.getUsername() != null && credentials.getPassword() != null;
    }
}