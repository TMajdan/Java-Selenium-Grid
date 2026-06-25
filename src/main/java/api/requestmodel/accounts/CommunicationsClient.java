package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class CommunicationsClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse details(RequestSpecification rs, String messageId) {
        return requestModel.getRequest(rs, "/panel/communications/", messageId);
    }
}
