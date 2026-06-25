package general.config;

import static general.config.ConfigManager.CONFIG;

public final class SeleniumProperties {

    private SeleniumProperties() {
    }

    public static boolean isGridMode() {
        return Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.grid"));
    }

    public static boolean isDebugMode() {
        return Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.debug"));
    }

    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.headless"));
    }

    public static boolean isMaximizeWindow() {
        return Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.maximizeWindow"));
    }

    public static int getWindowWidth() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("selenium.windowWidth"));
    }

    public static int getWindowHeight() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("selenium.windowHeight"));
    }

    public static String getHubUrl() {
        return CONFIG.getPropertyOrWarn("grid.hubUrl");
    }

    // ── Execution ──

    public static int getDefaultTimeout() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("execution.timeout"));
    }

    public static int getPollingInterval() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("execution.pollingInterval"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("execution.pageLoadTimeout"));
    }

    public static int getThreadCount() {
        return Integer.parseInt(CONFIG.getPropertyOrWarn("execution.threadCount"));
    }

    // ── API ──

    public static String getApiBaseUrl() {
        return CONFIG.getPropertyOrWarn("apiBaseUrl");
    }

    // ── Token Services ──

    /** Base URL dla serwisu Application JWT Token. */
    public static String getApplicationJwtBaseUrl() {
        return CONFIG.getPropertyOrWarn("application_jwt_token.base_url");
    }

    /** Endpoint ścieżka dla Application JWT Token. */
    public static String getApplicationJwtEndpoint() {
        return CONFIG.getPropertyOrWarn("application_jwt_token.endpoint");
    }

    /** Client ID dla Accounts JWT Token. */
    public static String getAccountsJwtClientId() {
        return CONFIG.getPropertyOrWarn("accounts_jwt_token.client_id");
    }

    /** Client Secret dla Accounts JWT Token. */
    public static String getAccountsJwtClientSecret() {
        return CONFIG.getPropertyOrWarn("accounts_jwt_token.client_secret");
    }

    /** Base URL dla Decision Engine SMS Token API (IdHub). */
    public static String getIdHubBaseUrl() {
        return CONFIG.getPropertyOrWarn("decision_engine.sms_token_api.base_url");
    }

    /** Client ID dla Decision Engine JWT Token. */
    public static String getDecisionEngineJwtClientId() {
        return CONFIG.getPropertyOrWarn("decision_engine.jwt_token.client_id");
    }

    /** Client Secret dla Decision Engine JWT Token. */
    public static String getDecisionEngineJwtClientSecret() {
        return CONFIG.getPropertyOrWarn("decision_engine.jwt_token.client_secret");
    }

    /** Base URL dla Accounts API (z configu smebanking). */
    public static String getSmeAccountsApiUrl() {
        return CONFIG.getPropertyOrWarn("smebanking.accounts_api.base_url");
    }

    /** Base URL dla EasyAdmin API. */
    public static String getEasyAdminUrl() {
        return CONFIG.getPropertyOrWarn("easy_admin.base_url");
    }
}