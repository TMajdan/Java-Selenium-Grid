package general.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestListener;
import org.testng.ITestResult;

import destkop.utils.screenshot.ScreenshotUtils;

import java.util.Optional;

/**
 * Allure-specific test listener for enhanced reporting.
 * Handles attaching test logs to Allure reports.
 * Screenshots are captured directly in TestBase.tearDown() via ScreenshotUtils.captureAndAttach().
 */
@Slf4j
public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachTestLog(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        attachTestLog(result);
    }

    /**
     * Attaches a textual log of the test result to Allure.
     */
    private void attachTestLog(ITestResult result) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("Test Class: ").append(result.getTestClass().getName()).append("\n");
        logBuilder.append("Test Method: ").append(result.getMethod().getMethodName()).append("\n");
        logBuilder.append("Status: ").append(result.getStatus()).append("\n");
        logBuilder.append("Duration: ").append(result.getEndMillis() - result.getStartMillis()).append(" ms\n");
        logBuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");

        Optional.ofNullable(result.getThrowable()).ifPresent(t -> {
            logBuilder.append("Exception: ").append(t.getClass().getName()).append("\n");
            logBuilder.append("Message: ").append(t.getMessage()).append("\n");
        });

        ScreenshotUtils.attachLog(logBuilder.toString());
    }
}