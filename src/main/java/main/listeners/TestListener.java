package main.listeners;

import lombok.extern.slf4j.Slf4j;
import main.driver.DriverManager;
import main.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Optional;

/**
 * TestNG listener for test lifecycle events.
 * Handles logging, screenshots on failure, and Allure attachments.
 */
@Slf4j
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        log.info("================================================");
        log.info("TEST SUITE STARTED: {}", context.getName());
        log.info("Suite includes {} tests", context.getAllTestMethods().length);
        log.info("================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("================================================");
        log.info("TEST SUITE FINISHED: {}", context.getName());
        log.info("Passed: {}, Failed: {}, Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        log.info("================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("------------------------------------------------");
        log.info("TEST STARTED: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
        log.info("Description: {}", Optional.ofNullable(result.getMethod().getDescription())
                .orElse("No description"));
        log.info("Parameters: {}", Arrays.toString(result.getParameters()));
        log.info("Thread: {}", Thread.currentThread().getName());
        log.info("------------------------------------------------");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST PASSED: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
        log.info("Duration: {} ms", result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("TEST FAILED: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            log.error("Failure reason: {}", throwable.getMessage());
            log.error("Stack trace:", throwable);
        }

        log.info("Duration: {} ms", result.getEndMillis() - result.getStartMillis());

        // Capture screenshot on failure
        captureScreenshotOnFailure(result);

        // Attach logs
        ScreenshotUtils.attachLogToAllure(
                String.format("Test '%s' failed with: %s",
                        result.getName(),
                        throwable != null ? throwable.getMessage() : "Unknown error"));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST SKIPPED: {}.{} - Reason: {}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                Optional.ofNullable(result.getThrowable())
                        .map(Throwable::getMessage)
                        .orElse("Unknown"));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("TEST FAILED WITHIN SUCCESS %: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }

    /**
     * Captures a screenshot when a test fails, attaches it to Allure.
     */
    private void captureScreenshotOnFailure(ITestResult result) {
        try {
            DriverManager.getExistingDriver().ifPresent(driver -> {
                String testName = result.getMethod().getMethodName();
                byte[] screenshotBytes = ScreenshotUtils.captureScreenshotAsBytes(driver);
                ScreenshotUtils.attachScreenshotToAllure(testName + "_FAILED", screenshotBytes);

                // Also attach page source
                try {
                    String pageSource = driver.getPageSource();
                    ScreenshotUtils.attachPageSourceToAllure(pageSource);
                } catch (Exception e) {
                    log.warn("Failed to capture page source", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to capture screenshot on test failure", e);
        }
    }
}
