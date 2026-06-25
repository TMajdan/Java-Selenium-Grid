package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class CommunicationsClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse details(RequestSpecification rs, String messageId) {
        return requestModel.getRequest(rs, "/panel/communications/", messageId);
    }
}