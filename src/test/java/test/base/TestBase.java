package test.base;

import lombok.extern.slf4j.Slf4j;
import config.TestConfig;
import static config.ConfigManager.CONFIG;
import driver.DriverFactory;
import driver.DriverManager;
import driver.SeleniumProperties;
import utils.FileUtils;
import utils.ScreenshotHolder;
import utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * Base class for all test classes.
 * Provides setup/teardown lifecycle, thread-safe driver management,
 * screenshot capture on failure, and reporting configuration.
 */
@Slf4j
public abstract class TestBase {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("================================================");
        log.info("TEST SUITE SETUP - Initializing framework");
        log.info("================================================");

        // Trigger TestConfig static initializer (sets ConfigManager.CONFIG)
        TestConfig.CONFIG.hashCode();

        // Create required directories
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.screenshotDir"));
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.downloadDir"));
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.allureResultsDir"));

        log.info("Environment: {}", CONFIG.getProperty("environment"));
        log.info("Headless: {}", SeleniumProperties.isHeadlessMode());
        log.info("Thread Count: {}", Integer.parseInt(CONFIG.getProperty("execution.threadCount")));
        log.info("================================================");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        log.info("================================================");
        log.info("SETUP: {} - Starting test: {}", 
                Thread.currentThread().getName(), method.getName());
        log.info("================================================");

        log.info("Browser: CHROME (only supported browser)");

        // Create driver and store in ThreadLocal
        WebDriver threadDriver = DriverFactory.createDriver();
        DriverManager.setDriver(threadDriver);
        this.driver = threadDriver;

        log.info("Driver initialized for test: {} on thread: {}",
                method.getName(), Thread.currentThread().getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) {
        log.info("================================================");
        log.info("TEARDOWN: {} - Test: {}",
                Thread.currentThread().getName(), method.getName());

        // Handle screenshot on failure
        if (!result.isSuccess() && Boolean.parseBoolean(CONFIG.getProperty("execution.screenshotOnFailure"))) {
            log.warn("Test FAILED: {} - Capturing screenshot", method.getName());
            try {
                // Verify driver session is still valid
                driver.getTitle();
                byte[] screenshotBytes = ScreenshotUtils.captureScreenshotAsBytes(driver);
                ScreenshotHolder.set(screenshotBytes);
                ScreenshotUtils.attachScreenshotToAllure(method.getName() + " - Failure Screenshot", screenshotBytes);
                ScreenshotUtils.captureScreenshot(driver, method.getName());
            } catch (Exception e) {
                log.warn("Could not capture screenshot - driver session may be invalid: {}", e.getMessage());
                // Still store empty marker so AllureListener knows a failure happened
                ScreenshotHolder.set(new byte[0]);
            }
        }

        // Quit driver
        DriverManager.quitDriver();
        log.info("Driver cleaned up for test: {}", method.getName());
        log.info("================================================");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("================================================");
        log.info("TEST SUITE TEARDOWN - Cleaning up resources");
        log.info("================================================");

        // Cleanup old screenshots
        if (Boolean.parseBoolean(CONFIG.getProperty("execution.screenshotOnFailure"))) {
            ScreenshotUtils.cleanupOldScreenshots(Integer.parseInt(CONFIG.getProperty("execution.maxScreenshotHistory")));
        }

        log.info("Framework teardown complete");
        log.info("================================================");
    }

    /**
     * Returns the current WebDriver instance.
     */
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Navigates to the base URL and waits for the page to load.
     */
    public void navigateToBaseUrl() {
        getDriver().get(CONFIG.getProperty("baseUrl"));
    }
}
