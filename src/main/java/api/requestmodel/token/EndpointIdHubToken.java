package api.requestmodel.token;

import api.logger.CustomRequestResponseLogger;
import api.requestmodel.accounts.model.User;
import api.requestmodel.easyadmin.EndpointEasyAdmin;
import com.github.javafaker.Faker;
import general.config.SeleniumProperties;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EndpointIdHubToken {

    private static final ThreadLocal<String[]> TOKEN_CACHE = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<String> PROCESS_ID = ThreadLocal.withInitial(() -> null);
    private static final Faker FAKER = new Faker();
    private static final IdHubRequests IDHUB_REQUESTS = new IdHubRequests();

    private EndpointIdHubToken() {}

    public static ValidatableResponse sendPostRequestIdhubSmsTokenSend(User user) {
        return sendPostRequestIdhubSmsTokenSend(user.getPhoneNumber());
    }

    @Step("Send POST request for IdHub SMS code")
    public static ValidatableResponse sendPostRequestIdhubSmsTokenSend(String phoneNumber) {
        String body = smsTokenSendBody(phoneNumber);
        return IDHUB_REQUESTS.sendSmsToken(buildSpec(), body);
    }

    @Step("Send POST request for IdHub SMS code (Faker)")
    public static ValidatableResponse sendPostRequestIdhubSmsTokenSend() {
        return sendPostRequestIdhubSmsTokenSend(FAKER.phoneNumber().cellPhone());
    }

    @Step("Send POST request for IdHub SMS refresh")
    public static ValidatableResponse sendPostRequestIdhubSmsTokenRefresh() {
        String body = "{\"refresh_token\":\"\",\"grant_type\":\"refresh_token\",\"application\":\"SMEACCOUNTS\"}";
        return IDHUB_REQUESTS.refreshSmsToken(buildSpec(), body);
    }

    @Step("Pobranie IdHub Token")
    public static String[] getIdHubTokens(String phoneNumber, String smsCode) {
        String[] cached = TOKEN_CACHE.get();
        if (cached != null && cached.length == 2) {
            log.debug("IdHub Token pobrany z cache watku");
            return cached;
        }
        String[] tokens = fetchTokens(phoneNumber, smsCode);
        if (tokens != null) {
            TOKEN_CACHE.set(tokens);
            log.debug("IdHub Token zostal pobrany i zapisany w cache");
        }
        return tokens;
    }

    public static String[] getIdHubTokensFromFaker() {
        String phoneNumber = FAKER.phoneNumber().cellPhone();
        String smsCode = FAKER.regexify("\\d{3}-\\d{3}");
        return fetchTokens(phoneNumber, smsCode);
    }

    public static String[] getIdHubTokensFromEasyAdmin(User user) {
        return getIdHubTokensFromEasyAdmin(user.getPhoneNumber(), user.getCifIndividual());
    }

    public static String[] getIdHubTokensFromEasyAdmin(String phoneNumber, String cif) {
        String authorizationCode = EndpointEasyAdmin.getAuthorizationCode(phoneNumber, cif);
        String smsCode = authorizationCode.substring(0, 3) + "-" + authorizationCode.substring(3);
        return getIdHubTokens(phoneNumber, smsCode);
    }

    public static String[] getIdHubTokens() { return TOKEN_CACHE.get(); }

    @Step("Reset IdHub Token")
    public static void resetIdHubTokens() {
        TOKEN_CACHE.remove();
        log.info("IdHub Token usuniety z cache");
    }

    public static String getProcessId() {
        String pid = PROCESS_ID.get();
        if (pid == null) {
            pid = FAKER.internet().uuid();
            PROCESS_ID.set(pid);
        }
        return pid;
    }

    public static void setProcessId(String processId) { PROCESS_ID.set(processId); }
    public static void resetProcessId() { PROCESS_ID.remove(); }

    private static String[] fetchTokens(String phoneNumber, String smsCode) {
        String baseUrl = SeleniumProperties.getIdHubBaseUrl();
        if (isBlank(baseUrl)) {
            log.warn("IdHub — brak konfiguracji base_url, uzywam placeholder-ow z Faker");
            return new String[]{FAKER.internet().uuid(), FAKER.internet().uuid()};
        }

        String body = smsTokenVerifyBody(phoneNumber, smsCode);
        ValidatableResponse vr = IDHUB_REQUESTS.verifySmsToken(buildSpec(), body);

        int statusCode = vr.extract().statusCode();
        if (statusCode != 200) {
            log.warn("IdHub — nie udalo sie pobrac tokena (status {}), uzywam placeholder-ow", statusCode);
            return new String[]{FAKER.internet().uuid(), FAKER.internet().uuid()};
        }

        String accessToken = vr.extract().jsonPath().getString("jwtToken.access_token");
        String refreshToken = vr.extract().jsonPath().getString("jwtToken.refresh_token");
        return new String[]{
                isBlank(accessToken) ? FAKER.internet().uuid() : accessToken,
                isBlank(refreshToken) ? FAKER.internet().uuid() : refreshToken
        };
    }

    private static RequestSpecification buildSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(SeleniumProperties.getIdHubBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }

    private static String smsTokenSendBody(String phoneNumber) {
        return "{\"phoneNumber\":\"%s\",\"application\":\"SMEACCOUNTS\",\"process\":\"SME\",\"processId\":\"%s\"}"
                .formatted(phoneNumber, getProcessId());
    }

    private static String smsTokenVerifyBody(String phoneNumber, String smsCode) {
        return "{\"phoneNumber\":\"%s\",\"smsToken\":\"%s\",\"application\":\"SMEACCOUNTS\",\"process\":\"SME\",\"processId\":\"%s\"}"
                .formatted(phoneNumber, smsCode, getProcessId());
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}