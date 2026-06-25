package api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class AgreementsClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse updateAgreements(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/agreements");
    }
}
