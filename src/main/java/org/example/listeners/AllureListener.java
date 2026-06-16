package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.ScreenshotHolder;
import org.example.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Optional;

/**
 * Allure-specific test listener for enhanced reporting.
 * Handles attaching screenshots, page source, and logs to Allure reports.
 * Works alongside the TestListener for comprehensive reporting.
 * <p>
 * Screenshots are captured in TestBase.tearDown() before the driver is quit,
 * stored in ScreenshotHolder (ThreadLocal), and attached here via Allure API.
 */
@Slf4j
public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshotsFromHolder(result);
        attachTestLog(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        attachScreenshotsFromHolder(result);
        attachTestLog(result);
    }

    /**
     * Attaches the screenshot (stored in ScreenshotHolder by TestBase.tearDown)
     * and page source to the Allure report.
     */
    private void attachScreenshotsFromHolder(ITestResult result) {
        try {
            byte[] screenshotBytes = ScreenshotHolder.get();
            if (screenshotBytes != null && screenshotBytes.length > 0) {
                ScreenshotUtils.attachScreenshotToAllure(
                        result.getName() + " - Failure Screenshot", screenshotBytes);
                log.debug("Attached screenshot to Allure for: {}", result.getName());
            }
            ScreenshotHolder.clear();
        } catch (Exception e) {
            log.warn("Failed to attach screenshot to Allure", e);
        }
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

        ScreenshotUtils.attachLogToAllure(logBuilder.toString());
    }
}
