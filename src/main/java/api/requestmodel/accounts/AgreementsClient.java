package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class AgreementsClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse updateAgreements(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/agreements");
    }
}