package main.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import main.config.ConfigManager;

/**
 * Utility class for building RestAssured {@link RequestSpecification} instances.
 * <p>
 * Usage:
 * <pre>
 * Response response = ApiClient.withBaseSpec()
 *         .header("Authorization", "Bearer token")
 *         .when()
 *         .get("/endpoint")
 *         .then()
 *         .extract()
 *         .response();
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiClient {

    /**
     * Creates a base RequestSpecification with common settings:
     * base URL from config, relaxed HTTPS, JSON content type, and request/response logging.
     */
    public static RequestSpecification withBaseSpec() {
        ConfigManager config = ConfigManager.getInstance();

        RestAssured.reset();

        return new RequestSpecBuilder()
                .setBaseUri(config.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestResponseLogger())
                .build();
    }

    /**
     * Creates a RequestSpecification with custom base URI and custom content type.
     */
    public static RequestSpecification withCustomSpec(String baseUri, String contentType) {
        RestAssured.reset();

        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(contentType)
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestResponseLogger())
                .build();
    }

    /**
     * Creates a RequestSpecification with basic authentication.
     */
    public static RequestSpecification withBasicAuth(String username, String password) {
        return withBaseSpec()
                .auth()
                .preemptive()
                .basic(username, password);
    }

    /**
     * Creates a RequestSpecification with bearer token authentication.
     */
    public static RequestSpecification withBearerToken(String token) {
        return withBaseSpec()
                .header("Authorization", "Bearer " + token);
    }
}
