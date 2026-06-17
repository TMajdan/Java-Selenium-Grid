package config;

/**
 * Enum representing supported browser types for test execution.
 */
public enum BrowserType {
    CHROME("chrome");

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    /**
     * Resolves a BrowserType from a string value (case-insensitive).
     *
     * @param value the browser name string
     * @return the matching BrowserType
     * @throws IllegalArgumentException if no match is found
     */
    public static BrowserType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Browser type cannot be null or empty");
        }
        String trimmed = value.trim().toUpperCase();
        for (BrowserType type : values()) {
            if (type.name().equals(trimmed)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported browser type: " + value
                + ". Supported values: CHROME");
    }
}
