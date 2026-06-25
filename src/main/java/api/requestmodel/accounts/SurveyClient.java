package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class SurveyClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse updateSurvey(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/survey");
    }

    public ValidatableResponse updateSurveyAdditional(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/survey/additional");
    }
}