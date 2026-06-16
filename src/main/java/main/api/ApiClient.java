package main.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import main.config.ConfigManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Base API client class for REST API testing.
 * Provides common methods for API requests using Rest Assured.
 * This is a foundation for future API test layer expansion.
 */
@Slf4j
public class ApiClient {

    private final String baseUrl;
    private final RequestSpecification requestSpec;

    /**
     * Initializes the API client with the configured API base URL.
     */
    public ApiClient() {
        this.baseUrl = ConfigManager.getInstance().getApiBaseUrl();
        this.requestSpec = buildRequestSpec();
    }

    /**
     * Initializes the API client with a custom base URL.
     */
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.requestSpec = buildRequestSpec();
    }

    /**
     * Builds the base request specification with common settings.
     */
    private RequestSpecification buildRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 10000)
                                .setParam("http.socket.timeout", 30000)))
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setRelaxedHTTPSValidation()
                .build();
    }

    /**
     * Sends a GET request.
     */
    @Step("GET {endpoint}")
    public Response get(String endpoint) {
        log.info("GET request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a GET request with query parameters.
     */
    @Step("GET {endpoint} with params")
    public Response get(String endpoint, Map<String, String> queryParams) {
        log.info("GET request to: {}{} with params: {}", baseUrl, endpoint, queryParams);
        return RestAssured.given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a POST request with a request body.
     */
    @Step("POST {endpoint}")
    public Response post(String endpoint, Object body) {
        log.info("POST request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a PUT request with a request body.
     */
    @Step("PUT {endpoint}")
    public Response put(String endpoint, Object body) {
        log.info("PUT request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a PATCH request with a request body.
     */
    @Step("PATCH {endpoint}")
    public Response patch(String endpoint, Object body) {
        log.info("PATCH request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a DELETE request.
     */
    @Step("DELETE {endpoint}")
    public Response delete(String endpoint) {
        log.info("DELETE request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Sends a DELETE request with a request body.
     */
    @Step("DELETE {endpoint} with body")
    public Response delete(String endpoint, Object body) {
        log.info("DELETE request to: {}{}", baseUrl, endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Logs the response details for debugging.
     */
    public void logResponse(Response response) {
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Time: {} ms", response.getTimeIn(TimeUnit.MILLISECONDS));
        log.info("Response Body: {}", response.getBody().asPrettyString());
    }
}
