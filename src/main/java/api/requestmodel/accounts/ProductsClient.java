package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ProductsClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse updateDebitCardProduct(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/products/debit-card");
    }
}
