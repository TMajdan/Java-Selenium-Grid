package base;

import lombok.extern.slf4j.Slf4j;
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
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("  Environment: {}  |  Grid: {}  |  Debug: {}  |  Headless: {}  |  Threads: {}",
                CONFIG.getProperty("environment"),
                CONFIG.getProperty("selenium.grid"),
                CONFIG.getProperty("selenium.debug"),
                SeleniumProperties.isHeadlessMode(),
                CONFIG.getProperty("execution.threadCount"));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Create required directories
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.screenshotDir"));
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.downloadDir"));
        FileUtils.createDirectoryIfNotExists(CONFIG.getProperty("paths.allureResultsDir"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        // Create driver and store in ThreadLocal
        WebDriver threadDriver = DriverFactory.createDriver();
        DriverManager.setDriver(threadDriver);
        this.driver = threadDriver;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) {
        // Handle screenshot on failure
        if (!result.isSuccess() && Boolean.parseBoolean(CONFIG.getProperty("execution.screenshotOnFailure"))) {
            log.warn("❌ {} – FAILED, screenshot captured", method.getName());
            try {
                // Verify driver session is still valid
                driver.getTitle();
                byte[] screenshotBytes = ScreenshotUtils.captureScreenshotAsBytes(driver);
                ScreenshotHolder.set(screenshotBytes);
                ScreenshotUtils.attachScreenshotToAllure(method.getName() + " - Failure Screenshot", screenshotBytes);
                ScreenshotUtils.captureScreenshot(driver, method.getName());
            } catch (Exception e) {
                log.warn("  Could not capture screenshot – session invalid: {}", e.getMessage());
                // Still store empty marker so AllureListener knows a failure happened
                ScreenshotHolder.set(new byte[0]);
            }
        }

        // Quit driver
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (Boolean.parseBoolean(CONFIG.getProperty("execution.screenshotOnFailure"))) {
            ScreenshotUtils.cleanupOldScreenshots(Integer.parseInt(CONFIG.getProperty("execution.maxScreenshotHistory")));
        }

        log.info("  Clean-up complete");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
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
