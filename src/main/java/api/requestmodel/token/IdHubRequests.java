package api.requestmodel.token;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class IdHubRequests {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse sendSmsToken(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/sms/token/send");
    }

    public ValidatableResponse verifySmsToken(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/sms/token/verify");
    }

    public ValidatableResponse refreshSmsToken(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/sms/token/refresh");
    }
}