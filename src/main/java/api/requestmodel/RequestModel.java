package api.requestmodel;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class RequestModel {

    public ValidatableResponse postRequest(RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().post(path)
                .then();
    }

    public ValidatableResponse getRequest (RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().get(path + "{id}")
                .then();
    }

    public ValidatableResponse putRequest (RequestSpecification rs, String requestBody, String path) {
        return given(rs)
                .body(requestBody)
                .when().put(path)
                .then();
    }

    public ValidatableResponse deleteRequest(RequestSpecification rs, String path, String pathParam) {
        return given(rs)
                .pathParam("id", pathParam)
                .when().delete(path + "{id}")
                .then();
    }
}