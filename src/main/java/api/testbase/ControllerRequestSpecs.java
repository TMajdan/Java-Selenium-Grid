package api.testbase;

import api.logger.CustomRequestResponseLogger;
import general.config.SeleniumProperties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerRequestSpecs {

    /**
     * Creates a base RequestSpecification with common settings:
     * base URL from config, relaxed HTTPS, JSON content type, and request/response logging.
     * Thread-safe – each call creates an independent RequestSpecification.
     */
    public static RequestSpecification withBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }

    /**
     * Creates a RequestSpecification with custom base URI and custom content type.
     * Thread-safe – each call creates an independent RequestSpecification.
     */
    public static RequestSpecification withCustomSpec(String baseUri, String contentType) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(contentType)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
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
        return withBaseSpec().header("Authorization", "Bearer " + token);
    }

    public static RequestSpecification withNoAuthorization() {
        return withBaseSpec();
    }
}