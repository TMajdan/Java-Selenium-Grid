package api.requestmodel.easyadmin;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class EasyAdminRequests {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse getWithFormParams(RequestSpecification rs, Map<String, String> formParams) {
        return requestModel.postRequest(rs, formParams, "");
    }
}