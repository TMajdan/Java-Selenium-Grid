package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import api.requestmodel.accounts.model.AccountsModel;
import api.requestmodel.accounts.model.DebitCardRequest;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ProductsClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse updateDebitCardProduct(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/products/debit-card");
    }

    public ValidatableResponse updateDebitCardProduct(RequestSpecification rs, AccountsModel accountsModel, String appId) {
        String body = DebitCardRequest.from(accountsModel.getDebitCard()).build().toJson();
        return requestModel.patchRequest(rs, body, "/application/" + appId + "/products/debit-card");
    }
}