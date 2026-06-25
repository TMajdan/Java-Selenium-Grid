package tests.api.accounts;

import api.logger.TestLifecycleLogger;
import api.requestmodel.accounts.ApplicationClient;
import api.requestmodel.accounts.CommunicationsClient;
import api.requestmodel.accounts.DictionaryClient;
import api.requestmodel.accounts.HealthCheckClient;
import api.requestmodel.accounts.ProductsClient;
import api.requestmodel.accounts.UserDataCompanyClient;
import api.requestmodel.accounts.model.ApplicationRequest;
import api.requestmodel.accounts.model.CompanyDataRequest;
import api.requestmodel.accounts.model.DebitCardRequest;
import api.testbase.AccountsRequestSpecs;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

/**
 * Accounts (App2You) API tests.
 *
 * <h3>Konwencja:</h3>
 * <ul>
 *   <li>Klienty jako pola instancji.</li>
 *   <li>RequestSpecs z {@link AccountsRequestSpecs} — auth wewnątrz.</li>
 *   <li>Request body przez buildery z metodą {@code from(...)}.</li>
 *   <li>Asercje na {@link ValidatableResponse} (Hamcrest).</li>
 *   <li>Wartości z JSON wyciągane przez {@code .extract().jsonPath()}.</li>
 * </ul>
 */
@Slf4j
@Listeners({TestLifecycleLogger.class})
@SuppressWarnings("null")
public class AccountsApiTest {

    private final HealthCheckClient healthCheckClient = new HealthCheckClient();
    private final ApplicationClient applicationClient = new ApplicationClient();
    private final DictionaryClient dictionaryClient = new DictionaryClient();
    private final ProductsClient productsClient = new ProductsClient();
    private final UserDataCompanyClient userDataCompanyClient = new UserDataCompanyClient();
    private final CommunicationsClient communicationsClient = new CommunicationsClient();

    private final RequestSpecification rsNoAuth = AccountsRequestSpecs.withNoAuth();
    private final RequestSpecification rsAuth = AccountsRequestSpecs.withAuth();

    private static String currentAppId;

    @BeforeClass
    public void logConfiguration() {
        log.info("Accounts API — autoryzacja przez EndpointApplicationJwtToken + EndpointIdHubToken");
    }

    @Test(testName = "HealthCheck should return 200 OK")
    public void testReturnHealthyStatus() {
        healthCheckClient.healthcheck(rsNoAuth)
                .assertThat().statusCode(200);
    }

    @Test(testName = "POST /application should create application and return appId")
    public void testCreateApplication() {
        String body = ApplicationRequest.from("48", "123456789", "test@example.com").build().toJson();

        ValidatableResponse response = applicationClient.createApplication(rsAuth, body);

        response.assertThat()
                .statusCode(200)
                .body("appId", notNullValue());

        currentAppId = response.extract().jsonPath().getString("appId");
        log.info("Created application with appId: {}", currentAppId);
    }

    @Test(testName = "GET /application/{appId}/status should return application status",
          dependsOnMethods = "testCreateApplication")
    public void testReturnApplicationStatus() {
        ValidatableResponse response = applicationClient.getApplicationStatus(rsAuth, currentAppId);

        response.assertThat()
                .statusCode(200)
                .body("applicationStatus", notNullValue());

        String status = response.extract().jsonPath().getString("applicationStatus");
        log.info("Application status: {}", status);
    }

    @Test(testName = "GET /application/{appId}/status should return 404 for non-existent appId")
    public void testReturn404ForMissingApplication() {
        applicationClient.getApplicationStatus(rsAuth, "non-existent-id")
                .assertThat().statusCode(404);
    }

    @Test(testName = "GET /dictionaries/debit-cards should return card list",
          dependsOnMethods = "testCreateApplication")
    public void testReturnDebitCardsDictionary() {
        ValidatableResponse response = dictionaryClient.getDebitCardsDictionaries(rsAuth, currentAppId);

        response.assertThat()
                .statusCode(200)
                .body("cardList", not(empty()));

        int count = response.extract().jsonPath().getList("cardList").size();
        log.info("Debit cards count: {}", count);
    }

    @Test(testName = "GET /dictionaries/survey should return survey")
    public void testReturnSurveyDictionary() {
        String testAppId = currentAppId != null ? currentAppId : "test-app-id";

        dictionaryClient.getSurveyDictionaries(rsAuth, testAppId)
                .assertThat().statusCode(200);
    }

    @Test(testName = "PATCH /products/debit-card should update debit card",
          dependsOnMethods = "testCreateApplication")
    public void testUpdateDebitCardProduct() {
        String body = DebitCardRequest.from("mc_gold").build().toJson();

        productsClient.updateDebitCardProduct(rsAuth, body, currentAppId)
                .assertThat().statusCode(200);

        log.info("Debit card product updated for appId: {}", currentAppId);
    }

    @Test(testName = "GET /user-data/company should return company data",
          dependsOnMethods = "testCreateApplication")
    public void testReturnCompanyData() {
        ValidatableResponse response = userDataCompanyClient.getUserDataCompany(rsAuth, currentAppId);

        response.assertThat()
                .statusCode(200)
                .body("nip", notNullValue(),
                      "name", notNullValue());

        String name = response.extract().jsonPath().getString("name");
        log.info("Company name: {}", name);
    }

    @Test(testName = "PATCH /user-data/company should update company data",
          dependsOnMethods = "testCreateApplication")
    public void testUpdateCompanyData() {
        String body = CompanyDataRequest.from("1234567890").build().toJson();

        userDataCompanyClient.updateUserDataCompany(rsAuth, body, currentAppId)
                .assertThat().statusCode(200);

        log.info("Company data updated for appId: {}", currentAppId);
    }

    @Test(testName = "GET /panel/communications/{messageId} should return details")
    public void testReturnCommunicationDetails() {
        communicationsClient.details(rsAuth, "sample-message-id")
                .assertThat()
                .statusCode(anyOf(equalTo(200), equalTo(404)));
    }

    /** Zwraca appId z pierwszego testu. */
    public static String getAppId() {
        return currentAppId != null ? currentAppId : "unknown";
    }
}
