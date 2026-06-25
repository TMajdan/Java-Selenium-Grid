package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class SignClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse signDocument(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign");
    }

    public ValidatableResponse confirmDocumentSign(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign");
    }

    public ValidatableResponse sendNewSignSms(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign/new-sms");
    }
}