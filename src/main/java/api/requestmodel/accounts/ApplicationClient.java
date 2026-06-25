package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ApplicationClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    // ========== Application Controller ==========

    public ValidatableResponse createApplication(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/application");
    }

    public ValidatableResponse updateApplication(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId);
    }

    public ValidatableResponse getApplicationStatus(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/status");
    }

    public ValidatableResponse updateApplicationStatus(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/status");
    }

    // ========== Verifications Controller ==========

    public ValidatableResponse createUserDataVerification(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/verifications");
    }
}
