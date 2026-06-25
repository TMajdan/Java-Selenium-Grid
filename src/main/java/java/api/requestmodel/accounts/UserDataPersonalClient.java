package api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class UserDataPersonalClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse getUserDataPersonal(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/user-data/personal");
    }

    public ValidatableResponse createUserDataPersonal(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/personal");
    }

    public ValidatableResponse createUserDataPersonalFolder(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/personal/folder");
    }

    public ValidatableResponse updateUserDataPersonalAdditional(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/user-data/personal/additional");
    }
}
