package api;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestModel {
    /**
     * Sends a POST request with a request body.
     *
     * @param rs          the RequestSpecification (from {@link ApiClient})
     * @param requestBody the request body as String
     * @param path        the endpoint path
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse postRequest(RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().post(path)
                .then();
    }

    /**
     * Sends a GET request with a path parameter.
     *
     * @param rs        the RequestSpecification
     * @param path      the endpoint path (e.g. "/users/")
     * @param pathParam the path parameter value (replaces "{id}" placeholder)
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse getRequest(RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().get(path + "{id}")
                .then();
    }

    /**
     * Sends a GET request without path parameters.
     *
     * @param rs   the RequestSpecification
     * @param path the endpoint path
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse getRequest(RequestSpecification rs, String path) {
        return given(rs)
                .when().get(path)
                .then();
    }

    /**
     * Sends a PUT request with a request body.
     *
     * @param rs          the RequestSpecification
     * @param requestBody the request body as String
     * @param path        the endpoint path
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse putRequest(RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().put(path)
                .then();
    }

    /**
     * Sends a PUT request with an object body.
     *
     * @param rs   the RequestSpecification
     * @param body the request body as a Java object
     * @param path the endpoint path
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse putRequest(RequestSpecification rs, Object body, String path) {
        return given(rs)
                .body(body)
                .when().put(path)
                .then();
    }

    /**
     * Sends a DELETE request with a path parameter.
     *
     * @param rs        the RequestSpecification
     * @param path      the endpoint path (e.g. "/users/")
     * @param pathParam the path parameter value (replaces "{id}" placeholder)
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse deleteRequest(RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().delete(path + "{id}")
                .then();
    }

    /**
     * Sends a DELETE request without path parameters.
     *
     * @param rs   the RequestSpecification
     * @param path the endpoint path
     * @return ValidatableResponse for chaining assertions
     */
    public ValidatableResponse deleteRequest(RequestSpecification rs, String path) {
        return given(rs)
                .when().delete(path)
                .then();
    }
}