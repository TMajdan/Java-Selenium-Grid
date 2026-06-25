package api.requestmodel.token;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApplicationJwtRequests {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse fetchToken(RequestSpecification rs, Map<String, String> formParams, String path) {
        return requestModel.postRequest(rs, formParams, path);
    }
}