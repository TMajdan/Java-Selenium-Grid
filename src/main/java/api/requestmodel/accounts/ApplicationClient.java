package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import api.requestmodel.accounts.model.AccountsModel;
import api.requestmodel.accounts.model.ApplicationRequest;
import api.requestmodel.token.EndpointIdHubToken;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ApplicationClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse createApplication(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/application");
    }

    public ValidatableResponse createApplication(RequestSpecification rs, AccountsModel accountsModel) {
        EndpointIdHubToken.sendPostRequestIdhubSmsTokenSend(accountsModel.getUser());
        String phoneNumber = accountsModel.getUser().getPhoneNumber();
        String prefix = "+" + phoneNumber.substring(0, 2);
        String number = phoneNumber.substring(2);
        String body = ApplicationRequest.from(prefix, number, accountsModel.getEmail()).build().toJson();
        return requestModel.postRequest(rs, body, "/application");
    }

    public ValidatableResponse updateApplication(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId);
    }

    public ValidatableResponse updateApplication(RequestSpecification rs, AccountsModel accountsModel, String appId) {
        String phoneNumber = accountsModel.getUser().getPhoneNumber();
        String prefix = "+" + phoneNumber.substring(0, 2);
        String number = phoneNumber.substring(2);
        String body = ApplicationRequest.from(prefix, number, accountsModel.getEmail()).build().toJson();
        return requestModel.patchRequest(rs, body, "/application/" + appId);
    }

    public ValidatableResponse getApplicationStatus(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/status");
    }

    public ValidatableResponse updateApplicationStatus(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/status");
    }

    public ValidatableResponse createUserDataVerification(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/verifications");
    }
}