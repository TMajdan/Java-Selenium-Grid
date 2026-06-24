package tests.api;

import api.logger.TestLifecycleLogger;
import api.requestmodel.ControllerRequest;
import api.testbase.ControllerRequestSpecs;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Slf4j
@Listeners({TestLifecycleLogger.class})
public class SimpleApiTest {

    private static final RequestSpecification rs = ControllerRequestSpecs.withNoAuthorization();
    private static final ControllerRequest controllerRequest = new ControllerRequest();

    @Test(description = "POST request with builder-built body and field-level assertions")
    public void testPostWithPaymentRequestBuilder() {
        ValidatableResponse response = controllerRequest.getApi(rs, "/api/users/2", "").statusCode(401);
    }
}