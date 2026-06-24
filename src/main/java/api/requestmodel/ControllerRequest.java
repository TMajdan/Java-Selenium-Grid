package api.requestmodel;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ControllerRequest {
    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse application(RequestSpecification rs, String requestBody, String path) {
        return requestModel.postRequest(rs, requestBody, "/application");
    }

    public ValidatableResponse getApi(RequestSpecification rs, String path, String pathParam) {
        return requestModel.getRequest(rs, path, pathParam);
    }
}