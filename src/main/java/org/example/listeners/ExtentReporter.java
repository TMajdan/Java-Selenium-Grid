package org.example.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ConfigManager;
import org.example.driver.DriverManager;
import org.example.utils.DateUtils;
import org.example.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

/**
 * Optional ExtentReports listener for HTML-based reporting.
 * Enabled/disabled via the YAML configuration (reporting.extentEnabled).
 */
@Slf4j
public class ExtentReporter implements ITestListener {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentReports extent;

    static {
        if (CONFIG.isExtentEnabled()) {
            initExtentReports();
        }
    }

    /**
     * Initializes the ExtentReports engine.
     */
    private static synchronized void initExtentReports() {
        if (extent == null) {
            String reportDir = CONFIG.getExtentReportPath();
            new File(reportDir).mkdirs();

            String fileName = reportDir + File.separator
                    + "ExtentReport_" + DateUtils.getCurrentDate() + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Selenium Grid - Automation Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", CONFIG.getActiveEnvironment());
            extent.setSystemInfo("Browser", CONFIG.getBrowser().getBrowserName());
            extent.setSystemInfo("Execution Mode", CONFIG.getExecutionMode().name());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            log.info("ExtentReports initialized at: {}", fileName);
        }
    }

    @Override
    public void onStart(ITestContext context) {
        if (!CONFIG.isExtentEnabled()) return;
        log.debug("ExtentReports - Suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (!CONFIG.isExtentEnabled()) return;
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports generated successfully");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (!CONFIG.isExtentEnabled()) return;

        ExtentTest test = extent.createTest(
                result.getTestClass().getName() + "." + result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (!CONFIG.isExtentEnabled()) return;
        extentTest.get().pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (!CONFIG.isExtentEnabled()) return;

        ExtentTest test = extentTest.get();

        // Add failure reason
        if (result.getThrowable() != null) {
            test.fail(result.getThrowable());
        }

        // Add screenshot
        try {
            DriverManager.getExistingDriver().ifPresent(driver -> {
                byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes(driver);
                test.fail("Screenshot on failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(
                                java.util.Base64.getEncoder().encodeToString(screenshot)
                        ).build());
            });
        } catch (Exception e) {
            log.warn("Failed to attach screenshot to ExtentReport", e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (!CONFIG.isExtentEnabled()) return;
        extentTest.get().skip("Test skipped - " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (!CONFIG.isExtentEnabled()) return;
        extentTest.get().warning("Test failed but within success percentage");
    }
}
