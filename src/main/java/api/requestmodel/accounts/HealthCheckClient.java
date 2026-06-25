package api.requestmodel.accounts;

import api.requestmodel.RequestModel;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class HealthCheckClient {

    private final RequestModel requestModel = new RequestModel();

    public ValidatableResponse healthcheck(RequestSpecification rs) {
        return requestModel.getRequest(rs, "/healthcheck", "");
    }
}