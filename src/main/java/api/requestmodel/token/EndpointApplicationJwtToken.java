package api.requestmodel.token;

import api.logger.CustomRequestResponseLogger;
import com.github.javafaker.Faker;
import general.config.SeleniumProperties;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public final class EndpointApplicationJwtToken {

    private static final ThreadLocal<String> TOKEN_CACHE = ThreadLocal.withInitial(() -> null);
    private static final Faker FAKER = new Faker();
    private static final ApplicationJwtRequests JWT_REQUESTS = new ApplicationJwtRequests();

    private EndpointApplicationJwtToken() {}

    @Step("Pobranie Application JWT Token")
    public static String getApplicationJwtToken() {
        String cached = TOKEN_CACHE.get();
        if (cached != null && !cached.isBlank()) {
            log.debug("Application JWT Token pobrany z cache wątku");
            return cached;
        }

        String token = fetchToken();
        if (token != null && !token.isBlank()) {
            TOKEN_CACHE.set(token);
            log.debug("Application JWT Token został pobrany i zapisany w cache");
        }
        return token;
    }

    @Step("Reset Application JWT Token")
    public static void resetToken() {
        TOKEN_CACHE.remove();
        log.info("Application JWT Token usunięty z cache");
    }

    private static String fetchToken() {
        String baseUrl = SeleniumProperties.getApplicationJwtBaseUrl();
        String endpoint = SeleniumProperties.getApplicationJwtEndpoint();
        String clientId = SeleniumProperties.getAccountsJwtClientId();
        String clientSecret = SeleniumProperties.getAccountsJwtClientSecret();

        if (isBlank(baseUrl) || isBlank(clientId) || isBlank(clientSecret)) {
            log.warn("Application JWT Token — brak konfiguracji, używam placeholder-a z Faker");
            return FAKER.internet().uuid();
        }

        RequestSpecification rs = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.URLENC)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
                .build();

        String path = endpoint != null && !endpoint.isBlank() ? endpoint : "/connect/token";

        ValidatableResponse response = JWT_REQUESTS.fetchToken(rs, Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "grant_type", "client_credentials",
                "scope", "gateway"
        ), path);

        int statusCode = response.extract().statusCode();
        if (statusCode != 200) {
            log.error("Failed to fetch Application JWT Token. Status: {}", statusCode);
            return null;
        }

        return response.extract().jsonPath().getString("access_token");
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}