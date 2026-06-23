package config;

import static config.ConfigManager.CONFIG;

public final class TestProperties {

    private static final String ENVIRONMENT = "environment";
    private static final String BASE_URL = "baseUrl";

    // ── IDM ──
    private static final String IDM_BASE_URL = "idm.base_url";
    private static final String IDM_LOGIN = "idm.login";
    private static final String IDM_PASSWORD = "idm.password";

    // ── Easy Admin ──
    private static final String EASY_ADMIN_BASE_URL = "easy_admin.base_url";

    // ── SME Banking ──
    private static final String SME_CHANGE_COMPANY_DATA_API_URL = "smebanking.change_company_data_api.base_url";
    private static final String SME_CERTIFICATES_API_URL = "smebanking.certificates_api.base_url";
    private static final String SME_ACCOUNTS_API_URL = "smebanking.accounts_api.base_url";

    // ── Decision Engine ──
    private static final String DECISION_ENGINE_SMS_TOKEN_API_URL = "decision_engine.sms_token_api.base_url";
    private static final String DECISION_ENGINE_JWT_CLIENT_SECRET = "decision_engine.jwt_token.client_secret";
    private static final String DECISION_ENGINE_JWT_CLIENT_ID = "decision_engine.jwt_token.client_id";

    // ── Mobywatel ──
    private static final String MOBYWATEL_ADAPTER_API_URL = "mobywatel.adapter_api.base_url";
    private static final String MOBYWATEL_IDHUB_WEB_COMPONENT_URL = "mobywatel.idhub_web_component_url";

    // ── AOC ──
    private static final String AOC_API_URL = "aoc.base_url";

    // ── CEKE ──
    private static final String CEKE_API_URL = "ceke.base_url";

    // ── CIS ──
    private static final String CIS_BASE_URL = "cis.base_url";
    private static final String CIS_X_REQUEST_SYSTEM_ID = "cis.x_request_system_id";
    private static final String CIS_CORP_ID = "cis.corp_id";

    // ── Application JWT Token ──
    private static final String APPLICATION_JWT_TOKEN_API_URL = "application_jwt_token.base_url";
    private static final String APPLICATION_JWT_TOKEN_ENDPOINT = "application_jwt_token.endpoint";

    // ── CED JWT Token ──
    private static final String CED_JWT_TOKEN_CLIENT_SECRET = "ced_jwt_token.client_secret";
    private static final String CED_JWT_TOKEN_CLIENT_ID = "ced_jwt_token.client_id";

    // ── Accounts JWT Token ──
    private static final String ACCOUNTS_JWT_TOKEN_CLIENT_SECRET = "accounts_jwt_token.client_secret";
    private static final String ACCOUNTS_JWT_TOKEN_CLIENT_ID = "accounts_jwt_token.client_id";

    // ── User JWT Token ──
    private static final String USER_JWT_TOKEN_API_URL = "user_jwt_token.base_url";

    // ── Token V2 ──
    private static final String TOKEN_V2_API_URL = "token_v2.base_url";

    // ── Tosia ──
    private static final String TOSIA_URL = "tosia.base_url";

    // ── OneApp ──
    private static final String ONEAPP_ANDROID_PATH = "oneapp.android_path";
    private static final String ONEAPP_IOS_PATH = "oneapp.ios_path";

    private TestProperties() {
    }

    public static String getEnvironment() {
        return CONFIG.getPropertyOrWarn(ENVIRONMENT);
    }

    public static String getBaseUrl() {
        return CONFIG.getPropertyOrWarn(BASE_URL);
    }

    // ── IDM ──

    public static String getIdmBaseUrl() {
        return CONFIG.getPropertyOrWarn(IDM_BASE_URL);
    }

    public static String getIdmLogin() {
        return CONFIG.getPropertyOrWarn(IDM_LOGIN);
    }

    public static String getIdmPassword() {
        return CONFIG.getPropertyOrWarn(IDM_PASSWORD);
    }

    // ── Easy Admin ──

    public static String getEasyAdminBaseUrl() {
        return CONFIG.getPropertyOrWarn(EASY_ADMIN_BASE_URL);
    }

    // ── SME Banking ──

    public static String getSmeChangeCompanyDataApiUrl() {
        return CONFIG.getPropertyOrWarn(SME_CHANGE_COMPANY_DATA_API_URL);
    }

    public static String getSmeCertificatesApiUrl() {
        return CONFIG.getPropertyOrWarn(SME_CERTIFICATES_API_URL);
    }

    public static String getSmeAccountsApiUrl() {
        return CONFIG.getPropertyOrWarn(SME_ACCOUNTS_API_URL);
    }

    // ── Decision Engine ──

    public static String getDecisionEngineSmsTokenApiUrl() {
        return CONFIG.getPropertyOrWarn(DECISION_ENGINE_SMS_TOKEN_API_URL);
    }

    public static String getDecisionEngineJwtClientSecret() {
        return CONFIG.getPropertyOrWarn(DECISION_ENGINE_JWT_CLIENT_SECRET);
    }

    public static String getDecisionEngineJwtClientId() {
        return CONFIG.getPropertyOrWarn(DECISION_ENGINE_JWT_CLIENT_ID);
    }

    // ── Mobywatel ──

    public static String getMobywatelAdapterApiUrl() {
        return CONFIG.getPropertyOrWarn(MOBYWATEL_ADAPTER_API_URL);
    }

    public static String getMobywatelIdhubWebComponentUrl() {
        return CONFIG.getPropertyOrWarn(MOBYWATEL_IDHUB_WEB_COMPONENT_URL);
    }

    // ── AOC ──

    public static String getAocApiUrl() {
        return CONFIG.getPropertyOrWarn(AOC_API_URL);
    }

    // ── CEKE ──

    public static String getCekeApiUrl() {
        return CONFIG.getPropertyOrWarn(CEKE_API_URL);
    }

    // ── CIS ──

    public static String getCisBaseUrl() {
        return CONFIG.getPropertyOrWarn(CIS_BASE_URL);
    }

    public static String getCisXRequestSystemId() {
        return CONFIG.getPropertyOrWarn(CIS_X_REQUEST_SYSTEM_ID);
    }

    public static String getCisCorpId() {
        return CONFIG.getPropertyOrWarn(CIS_CORP_ID);
    }

    // ── Application JWT Token ──

    public static String getApplicationJwtTokenApiUrl() {
        return CONFIG.getPropertyOrWarn(APPLICATION_JWT_TOKEN_API_URL);
    }

    public static String getApplicationJwtTokenEndpoint() {
        return CONFIG.getPropertyOrWarn(APPLICATION_JWT_TOKEN_ENDPOINT);
    }

    // ── CED JWT Token ──

    public static String getCedJwtTokenClientSecret() {
        return CONFIG.getPropertyOrWarn(CED_JWT_TOKEN_CLIENT_SECRET);
    }

    public static String getCedJwtTokenClientId() {
        return CONFIG.getPropertyOrWarn(CED_JWT_TOKEN_CLIENT_ID);
    }

    // ── Accounts JWT Token ──

    public static String getAccountsJwtTokenClientSecret() {
        return CONFIG.getPropertyOrWarn(ACCOUNTS_JWT_TOKEN_CLIENT_SECRET);
    }

    public static String getAccountsJwtTokenClientId() {
        return CONFIG.getPropertyOrWarn(ACCOUNTS_JWT_TOKEN_CLIENT_ID);
    }

    // ── User JWT Token ──

    public static String getUserJwtTokenApiUrl() {
        return CONFIG.getPropertyOrWarn(USER_JWT_TOKEN_API_URL);
    }

    // ── Token V2 ──

    public static String getTokenV2ApiUrl() {
        return CONFIG.getPropertyOrWarn(TOKEN_V2_API_URL);
    }

    // ── Tosia ──

    public static String getTosiaUrl() {
        return CONFIG.getPropertyOrWarn(TOSIA_URL);
    }

    // ── OneApp ──

    public static String getOneappAndroidPath() {
        return CONFIG.getPropertyOrWarn(ONEAPP_ANDROID_PATH);
    }

    public static String getOneappIosPath() {
        return CONFIG.getPropertyOrWarn(ONEAPP_IOS_PATH);
    }
}