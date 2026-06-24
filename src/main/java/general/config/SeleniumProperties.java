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
}