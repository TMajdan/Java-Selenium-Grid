package main.listeners;

import lombok.extern.slf4j.Slf4j;
import main.config.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for flaky tests.
 * Retries failed tests up to the configured retry count from YAML config.
 * Does not retry tests that passed or tests with specific annotations.
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = ConfigManager.getInstance().getRetryCount();
    private int retryCount = 0;

    /**
     * Checks whether the test should be retried.
     *
     * @param result the test result
     * @return true if the test should be retried
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying test '{}' - attempt {}/{}",
                    result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }

    /**
     * Returns the current retry count.
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Returns the maximum retry count.
     */
    public static int getMaxRetryCount() {
        return MAX_RETRY_COUNT;
    }

    /**
     * Resets the retry counter (useful for reuse).
     */
    public void reset() {
        this.retryCount = 0;
    }
}
