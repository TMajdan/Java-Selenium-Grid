package config;

/**
 * Typed accessors for Selenium-related configuration properties.
 */
public final class SeleniumProperties {

    private SeleniumProperties() {
    }

    /**
     * Returns whether Selenium Grid mode is enabled.
     */
    public static boolean isGridMode() {
        return TestProperties.isGridMode();
    }

    /**
     * Returns whether debug mode is enabled.
     */
    public static boolean isDebugMode() {
        return TestProperties.isDebugMode();
    }

    /**
     * Returns whether headless mode is enabled.
     */
    public static boolean isHeadlessMode() {
        return TestProperties.isHeadlessMode();
    }
}