package listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

    /**
     * https://testng.org/#_rerunning_failed_tests
     *
     * This implementation of retry analyzer moves on to another test case when AssertionError exception is thrown
     * Use of this retry analyzer is suitable only in cases when you are sure that outcome of assertions won't change when test case is rerun
     */
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (iTestResult.getThrowable() instanceof AssertionError) {
            return false; // do NOT retry
        }

        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Test failed due to: ", iTestResult.getThrowable());
            log.debug("Retrying test: {}, attempt: {}", iTestResult.getName(), retryCount);
            return true; // do retry
        }
        log.info("Max retry attempts reached for test {}", iTestResult.getName());
        return false; // do NOT retry
    }
}