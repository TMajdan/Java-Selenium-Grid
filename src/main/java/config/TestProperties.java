package config;

import static config.ConfigManager.CONFIG;

public final class TestProperties {

    private static final String ENVIRONMENT = "environment";
    private static final String BASE_URL = "baseUrl";

    public static String getEnvironment() {
        return CONFIG.getPropertyOrWarn(ENVIRONMENT);
    }

    public static String getBaseUrl() {
        return CONFIG.getPropertyOrWarn(BASE_URL);
    }
}