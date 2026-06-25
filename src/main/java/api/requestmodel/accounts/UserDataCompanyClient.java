package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import api.requestmodel.accounts.model.AccountsModel;
import api.requestmodel.accounts.model.CompanyDataRequest;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class UserDataCompanyClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse createUserDataCompanyFolder(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/company/folder");
    }

    public ValidatableResponse getUserDataCompany(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/user-data/company");
    }

    public ValidatableResponse updateUserDataCompany(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/user-data/company");
    }

    public ValidatableResponse updateUserDataCompany(RequestSpecification rs, AccountsModel accountsModel, String appId) {
        String body = CompanyDataRequest.from(accountsModel.getUser().getNip()).build().toJson();
        return requestModel.patchRequest(rs, body, "/application/" + appId + "/user-data/company");
    }
}