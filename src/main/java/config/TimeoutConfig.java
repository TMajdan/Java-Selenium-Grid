package config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * Centralized timeout configuration for all WebDriver wait operations.
 * Uses lazy initialization with double-checked locking for thread safety.
 * <p>
 * This is the SINGLE SOURCE OF TRUTH for timeout values.
 * Both WaitUtils and BaseActions delegate to this class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeoutConfig {

    private static volatile Integer defaultTimeout;
    private static volatile Integer pollingInterval;
    private static volatile Integer pageLoadTimeout;

    public static int getDefaultTimeout() {
        if (defaultTimeout == null) {
            synchronized (TimeoutConfig.class) {
                if (defaultTimeout == null) {
                    defaultTimeout = TestProperties.getDefaultTimeout();
                }
            }
        }
        return defaultTimeout;
    }

    public static Duration getDefaultDuration() {
        return Duration.ofSeconds(getDefaultTimeout());
    }

    public static int getPollingInterval() {
        if (pollingInterval == null) {
            synchronized (TimeoutConfig.class) {
                if (pollingInterval == null) {
                    pollingInterval = TestProperties.getPollingInterval();
                }
            }
        }
        return pollingInterval;
    }

    public static Duration getPollingDuration() {
        return Duration.ofMillis(getPollingInterval());
    }

    public static int getPageLoadTimeout() {
        if (pageLoadTimeout == null) {
            synchronized (TimeoutConfig.class) {
                if (pageLoadTimeout == null) {
                    pageLoadTimeout = TestProperties.getPageLoadTimeout();
                }
            }
        }
        return pageLoadTimeout;
    }

    public static Duration getPageLoadDuration() {
        return Duration.ofSeconds(getPageLoadTimeout());
    }
}