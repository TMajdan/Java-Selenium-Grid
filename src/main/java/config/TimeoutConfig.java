package config;

import java.time.Duration;

import static config.ConfigManager.CONFIG;

/**
 * Centralized timeout configuration for all WebDriver wait operations.
 * Uses lazy initialization with double-checked locking for thread safety.
 * <p>
 * This is the SINGLE SOURCE OF TRUTH for timeout values.
 * Both WaitUtils and BaseActions delegate to this class.
 */
public final class TimeoutConfig {

    private static volatile Integer defaultTimeout;
    private static volatile Integer pollingInterval;
    private static volatile Integer pageLoadTimeout;

    private TimeoutConfig() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns the default explicit wait timeout in seconds.
     */
    public static int getDefaultTimeout() {
        if (defaultTimeout == null) {
            synchronized (TimeoutConfig.class) {
                if (defaultTimeout == null) {
                    defaultTimeout = Integer.parseInt(
                            CONFIG.getPropertyOrWarn("execution.timeout"));
                }
            }
        }
        return defaultTimeout;
    }

    /**
     * Returns the default timeout as a Duration.
     */
    public static Duration getDefaultDuration() {
        return Duration.ofSeconds(getDefaultTimeout());
    }

    /**
     * Returns the polling interval in milliseconds for WebDriverWait.
     */
    public static int getPollingInterval() {
        if (pollingInterval == null) {
            synchronized (TimeoutConfig.class) {
                if (pollingInterval == null) {
                    pollingInterval = Integer.parseInt(
                            CONFIG.getPropertyOrWarn("execution.pollingInterval"));
                }
            }
        }
        return pollingInterval;
    }

    /**
     * Returns the polling interval as a Duration.
     */
    public static Duration getPollingDuration() {
        return Duration.ofMillis(getPollingInterval());
    }

    /**
     * Returns the page load timeout in seconds.
     */
    public static int getPageLoadTimeout() {
        if (pageLoadTimeout == null) {
            synchronized (TimeoutConfig.class) {
                if (pageLoadTimeout == null) {
                    pageLoadTimeout = Integer.parseInt(
                            CONFIG.getPropertyOrWarn("execution.pageLoadTimeout"));
                }
            }
        }
        return pageLoadTimeout;
    }

    /**
     * Returns the page load timeout as a Duration.
     */
    public static Duration getPageLoadDuration() {
        return Duration.ofSeconds(getPageLoadTimeout());
    }
}