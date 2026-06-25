package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class UserDataCompanyClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse createUserDataCompanyFolder(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/company/folder");
    }

    public ValidatableResponse getUserDataCompany(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/user-data/company");
    }

    public ValidatableResponse updateUserDataCompany(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/user-data/company");
    }
}
