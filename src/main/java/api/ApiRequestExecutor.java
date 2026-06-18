package api;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Executes HTTP requests (GET, POST, PUT, DELETE) using RestAssured.
 * Uses {@link ApiClient} for RequestSpecification configuration.
 */
public class ApiRequestExecutor {

    /**
     * Sends a POST request with a request body.
     */
    public ValidatableResponse postRequest(RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().post(path)
                .then();
    }

    /**
     * Sends a GET request with a path parameter.
     */
    public ValidatableResponse getRequest(RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().get(path + "{id}")
                .then();
    }

    /**
     * Sends a GET request without path parameters.
     */
    public ValidatableResponse getRequest(RequestSpecification rs, String path) {
        return given(rs)
                .when().get(path)
                .then();
    }

    /**
     * Sends a PUT request with a request body.
     */
    public ValidatableResponse putRequest(RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().put(path)
                .then();
    }

    /**
     * Sends a PUT request with an object body.
     */
    public ValidatableResponse putRequest(RequestSpecification rs, Object body, String path) {
        return given(rs)
                .body(body)
                .when().put(path)
                .then();
    }

    /**
     * Sends a DELETE request with a path parameter.
     */
    public ValidatableResponse deleteRequest(RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().delete(path + "{id}")
                .then();
    }

    /**
     * Sends a DELETE request without path parameters.
     */
    public ValidatableResponse deleteRequest(RequestSpecification rs, String path) {
        return given(rs)
                .when().delete(path)
                .then();
    }
}
