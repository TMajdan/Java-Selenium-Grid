package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ContractTemplateClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse createContractTemplate(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/contract-template");
    }
}