package main.test.base;

import lombok.extern.slf4j.Slf4j;
import main.config.BrowserType;
import main.config.ConfigManager;
import main.driver.DriverFactory;
import main.driver.DriverManager;
import main.utils.FileUtils;
import main.utils.ScreenshotHolder;
import main.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * Base class for all test classes.
 * Provides setup/teardown lifecycle, thread-safe driver management,
 * screenshot capture on failure, and reporting configuration.
 */
@Slf4j
public abstract class TestBase {

    protected WebDriver driver;
    protected ConfigManager config;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("================================================");
        log.info("TEST SUITE SETUP - Initializing framework");
        log.info("================================================");

        // Initialize configuration
        config = ConfigManager.getInstance();

        // Create required directories
        FileUtils.createDirectoryIfNotExists(config.getScreenshotDir());
        FileUtils.createDirectoryIfNotExists(config.getDownloadDir());
        FileUtils.createDirectoryIfNotExists(config.getAllureResultsDir());

        log.info("Environment: {}", config.getActiveEnvironment());
        log.info("Execution Mode: {}", config.getExecutionMode());
        log.info("Browser: {}", config.getBrowser());
        log.info("Headless: {}", config.isHeadless());
        log.info("Thread Count: {}", config.getThreadCount());
        log.info("================================================");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("") String browserParam, Method method) {
        log.info("================================================");
        log.info("SETUP: {} - Starting test: {}", 
                Thread.currentThread().getName(), method.getName());
        log.info("================================================");

        // Resolve browser type (method parameter > suite parameter > config default)
        BrowserType browserType = resolveBrowserType(browserParam);
        config = ConfigManager.getInstance();

        // Create driver and store in ThreadLocal
        WebDriver threadDriver = DriverFactory.createDriver(browserType, config.getExecutionMode());
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
        if (!result.isSuccess() && config.isScreenshotOnFailure()) {
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
        if (config.isScreenshotOnFailure()) {
            ScreenshotUtils.cleanupOldScreenshots(config.getMaxScreenshotHistory());
        }

        log.info("Framework teardown complete");
        log.info("================================================");

        // Reset ConfigManager singleton for clean state
        ConfigManager.reset();
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
        getDriver().get(config.getBaseUrl());
    }

    /**
     * Resolves the browser type from parameters and configuration.
     */
    private BrowserType resolveBrowserType(String browserParam) {
        if (browserParam != null && !browserParam.isEmpty()) {
            try {
                return BrowserType.fromString(browserParam);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid browser parameter '{}', using default", browserParam);
            }
        }
        // Check system property
        String systemBrowser = System.getProperty("browser");
        if (systemBrowser != null && !systemBrowser.isEmpty()) {
            try {
                return BrowserType.fromString(systemBrowser);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid system property browser '{}', using config default", systemBrowser);
            }
        }
        return config.getBrowser();
    }
}
