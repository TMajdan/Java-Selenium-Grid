package api.testbase;

import api.logger.CustomRequestResponseLogger;
import api.requestmodel.accounts.model.AccountsModel;
import api.requestmodel.token.EndpointApplicationJwtToken;
import api.requestmodel.token.EndpointIdHubToken;
import general.config.SeleniumProperties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountsRequestSpecs {

    private static final String BASE_URL = resolveBaseUrl();

    private static String resolveBaseUrl() {
        String url = SeleniumProperties.getSmeAccountsApiUrl();
        if (url == null || url.isBlank()) {
            url = SeleniumProperties.getApiBaseUrl();
        }
        return url;
    }

    public static RequestSpecification withNoAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }

    public static RequestSpecification withAuth() {
        String jwt = EndpointApplicationJwtToken.getApplicationJwtToken();
        String[] idHubTokens = EndpointIdHubToken.getIdHubTokens();
        if (idHubTokens == null) {
            idHubTokens = EndpointIdHubToken.getIdHubTokensFromFaker();
        }
        String idHubAccess = (idHubTokens != null && idHubTokens.length > 0) ? idHubTokens[0] : "";

        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Authorized-Party-Token", idHubAccess)
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }

    public static RequestSpecification withAuth(AccountsModel accountsModel) {
        String jwt = EndpointApplicationJwtToken.getApplicationJwtToken();
        String[] idHubTokens = EndpointIdHubToken.getIdHubTokens();
        if (idHubTokens == null) {
            idHubTokens = EndpointIdHubToken.getIdHubTokensFromEasyAdmin(accountsModel.getUser());
        }
        String idHubAccess = (idHubTokens != null && idHubTokens.length > 0) ? idHubTokens[0] : "";

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Authorized-Party-Token", idHubAccess)
                .addFilter(new CustomRequestResponseLogger());

        String channel = accountsModel.getSmeAccountsChannel();
        if (channel != null && !channel.isBlank()) {
            builder.addHeader("smeaccounts_channel", channel);
        }

        return builder.build();
    }

    public static RequestSpecification withAuthAndChannel(String channelValue) {
        String jwt = EndpointApplicationJwtToken.getApplicationJwtToken();
        String[] idHubTokens = EndpointIdHubToken.getIdHubTokens();
        if (idHubTokens == null) {
            idHubTokens = EndpointIdHubToken.getIdHubTokensFromFaker();
        }
        String idHubAccess = (idHubTokens != null && idHubTokens.length > 0) ? idHubTokens[0] : "";

        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Authorized-Party-Token", idHubAccess)
                .addHeader("smeaccounts_channel", channelValue)
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }

    public static RequestSpecification withAuthAndCorrelationId(String correlationId) {
        String jwt = EndpointApplicationJwtToken.getApplicationJwtToken();
        String[] idHubTokens = EndpointIdHubToken.getIdHubTokens();
        if (idHubTokens == null) {
            idHubTokens = EndpointIdHubToken.getIdHubTokensFromFaker();
        }
        String idHubAccess = (idHubTokens != null && idHubTokens.length > 0) ? idHubTokens[0] : "";

        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Authorized-Party-Token", idHubAccess)
                .addHeader("X-Correlation-Id", correlationId)
                .addFilter(new CustomRequestResponseLogger())
                .build();
    }
}