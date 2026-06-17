package driver;

import static config.ConfigManager.CONFIG;

/**
 * Typed accessors for Selenium-related configuration properties.
 */
public final class SeleniumProperties {

    private static final String GRID = "selenium.grid";
    private static final String DEBUG = "selenium.debug";
    private static final String HEADLESS = "selenium.headless";

    private SeleniumProperties() {
    }

    /**
     * Returns whether Selenium Grid mode is enabled.
     */
    public static boolean isGridMode() {
        return Boolean.parseBoolean(CONFIG.getProperty(GRID));
    }

    /**
     * Returns whether debug mode is enabled.
     */
    public static boolean isDebugMode() {
        return Boolean.parseBoolean(CONFIG.getProperty(DEBUG));
    }

    /**
     * Returns whether headless mode is enabled.
     */
    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(CONFIG.getProperty(HEADLESS));
    }
}
