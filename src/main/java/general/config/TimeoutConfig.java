package general.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeoutConfig {

    private static class DefaultTimeoutHolder {
        static final int VALUE = SeleniumProperties.getDefaultTimeout();
    }

    private static class PollingIntervalHolder {
        static final int VALUE = SeleniumProperties.getPollingInterval();
    }

    private static class PageLoadTimeoutHolder {
        static final int VALUE = SeleniumProperties.getPageLoadTimeout();
    }

    public static int getDefaultTimeout() {
        return DefaultTimeoutHolder.VALUE;
    }

    public static Duration getDefaultDuration() {
        return Duration.ofSeconds(getDefaultTimeout());
    }

    public static int getPollingInterval() {
        return PollingIntervalHolder.VALUE;
    }

    public static Duration getPollingDuration() {
        return Duration.ofMillis(getPollingInterval());
    }

    public static int getPageLoadTimeout() {
        return PageLoadTimeoutHolder.VALUE;
    }

    public static Duration getPageLoadDuration() {
        return Duration.ofSeconds(getPageLoadTimeout());
}
}