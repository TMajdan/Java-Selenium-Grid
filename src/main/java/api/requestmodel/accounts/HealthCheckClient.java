package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class HealthCheckClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse healthcheck(RequestSpecification rs) {
        return requestModel.getRequest(rs, "/healthcheck", "");
    }
}
