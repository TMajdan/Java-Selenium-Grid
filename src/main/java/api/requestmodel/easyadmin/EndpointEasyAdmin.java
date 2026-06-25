package api.requestmodel.easyadmin;

import api.logger.CustomRequestResponseLogger;
import api.requestmodel.accounts.model.User;
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
public final class EndpointEasyAdmin {

    private static final ThreadLocal<String> AUTHORIZATION_CODE_CACHE = ThreadLocal.withInitial(() -> null);
    private static final Faker FAKER = new Faker();
    private static final EasyAdminRequests EASY_ADMIN_REQUESTS = new EasyAdminRequests();

    private EndpointEasyAdmin() {}

    public static String getAuthorizationCode(User user) {
        return getAuthorizationCode(user.getPhoneNumber(), user.getCifIndividual());
    }

    @Step("Send GET request for Authorization code and save as a variable")
    public static String getAuthorizationCode(String phoneNumber, String cif) {
        String cached = AUTHORIZATION_CODE_CACHE.get();
        if (cached != null && !cached.isBlank()) {
            log.debug("Authorization code pobrany z cache watku");
            return cached;
        }
        String code = fetchAuthorizationCode(phoneNumber, cif);
        if (code != null && !code.isBlank()) {
            AUTHORIZATION_CODE_CACHE.set(code);
            log.debug("Authorization code zostal pobrany i zapisany w cache");
        }
        return code;
    }

    public static String getAuthorizationCode() { return AUTHORIZATION_CODE_CACHE.get(); }

    @Step("Reset Authorization code")
    public static void resetAuthorizationCode() {
        AUTHORIZATION_CODE_CACHE.remove();
        log.info("Authorization code usuniety z cache");
    }

    private static String fetchAuthorizationCode(String phoneNumber, String cif) {
        String baseUrl = SeleniumProperties.getEasyAdminUrl();
        if (isBlank(baseUrl)) {
            log.warn("EasyAdmin — brak konfiguracji base_url, uzywam placeholder-a z Faker");
            delay();
            return FAKER.regexify("\\d{6}");
        }
        delay();

        ValidatableResponse vr = callEasyAdmin(baseUrl, phoneNumber, cif);

        int statusCode = vr.extract().statusCode();
        if (statusCode != 200) {
            log.error("EasyAdmin — blad zapytania. Status: {}", statusCode);
            return FAKER.regexify("\\d{6}");
        }

        String code = vr.extract().jsonPath().getString("[0].authorizationCode");
        return !isBlank(code) ? code : FAKER.regexify("\\d{6}");
    }

    public static String fetchCif(String phoneNumber) {
        String baseUrl = SeleniumProperties.getEasyAdminUrl();
        if (isBlank(baseUrl)) return FAKER.regexify("\\d{10}");
        delay();

        ValidatableResponse vr = callEasyAdmin(baseUrl, phoneNumber, null);
        if (vr.extract().statusCode() != 200) return FAKER.regexify("\\d{10}");

        String cif = vr.extract().jsonPath().getString("[0].cif");
        return !isBlank(cif) ? cif : FAKER.regexify("\\d{10}");
    }

    public static String fetchMessage(String phoneNumber, String cif) {
        String baseUrl = SeleniumProperties.getEasyAdminUrl();
        if (isBlank(baseUrl)) return FAKER.lorem().sentence();
        delay();

        ValidatableResponse vr = callEasyAdmin(baseUrl, phoneNumber, cif);
        if (vr.extract().statusCode() != 200) return FAKER.lorem().sentence();

        String message = vr.extract().jsonPath().getString("[0].message");
        return !isBlank(message) ? message : FAKER.lorem().sentence();
    }

    private static ValidatableResponse callEasyAdmin(String baseUrl, String phoneNumber, String cif) {
        String param = !isBlank(phoneNumber) ? "phoneNumber" : "cif";
        String value = !isBlank(phoneNumber) ? phoneNumber : cif;

        RequestSpecification rs = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
                .addFormParam("env", "ZT004")
                .build();

        return EASY_ADMIN_REQUESTS.getWithFormParams(rs, Map.of(param, value));
    }

    private static void delay() {
        try { Thread.sleep(4000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}