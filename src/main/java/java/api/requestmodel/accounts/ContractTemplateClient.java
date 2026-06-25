package api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ContractTemplateClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse createContractTemplate(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/contract-template");
    }
}
