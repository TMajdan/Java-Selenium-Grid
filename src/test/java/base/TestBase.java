package base;

import general.config.SeleniumProperties;
import general.config.TestProperties;
import destkop.utils.screenshot.ScreenshotUtils;
import destkop.driver.BaseDriver;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.testng.SkipException;

import java.util.Optional;

/**
 * Base class for all test classes.
 * Provides setup/teardown lifecycle and thread-safe driver management.
 */
@Slf4j
@SuppressWarnings("null")
public abstract class TestBase {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        setDebugLevel();
        log.info("=======================================");
        log.info("  Environment: {}  |  Framework: {}  |  Grid: {}  |  Debug: {}  |  Headless: {}  |  Threads: {}",
                TestProperties.getEnvironment(),
                TestProperties.getFramework(),
                SeleniumProperties.isGridMode(),
                SeleniumProperties.isDebugMode(),
                SeleniumProperties.isHeadlessMode(),
                SeleniumProperties.getThreadCount());
        log.info("=======================================");
    }

    private static void setDebugLevel() {
        Logger root = (Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (SeleniumProperties.isDebugMode()) {
            root.setLevel(Level.DEBUG);
        } else {
            root.setLevel(Level.INFO);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String framework = TestProperties.getFramework();
        if (!"selenium".equals(framework)) {
            throw new SkipException("Skipped — framework is '" + framework + "', not 'selenium'");
        }
        BaseDriver.getWebDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.SKIP) {
            log.info("  [SKIP] {} - {}", result.getName(), result.getThrowable() != null ? result.getThrowable().getMessage() : "");
            return;
        }

        Optional<WebDriver> optDriver = BaseDriver.getExistingWebDriver();
        if (!result.isSuccess()) {
            if (optDriver.isPresent()) {
                WebDriver driver = optDriver.get();
                log.warn("  [FAIL] {} - screenshot captured", result.getName());
                ScreenshotUtils.attachScreenshot(result.getName() + " - Failure");
                attachBrowserConsoleLogs(driver);
            } else {
                log.warn("  [FAIL] {} - no driver available for screenshot", result.getName());
            }
        }
        BaseDriver.quitDriver();
    }

    private static void attachBrowserConsoleLogs(WebDriver driver) {
        try {
            String logs = driver.manage().logs().get(LogType.BROWSER).getAll().stream()
                    .map(logEntry -> "[%s] %s".formatted(logEntry.getLevel(), logEntry.getMessage()))
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("No browser console logs");
            Allure.addAttachment("Browser console logs", "text/plain", logs);
        } catch (Exception e) {
            log.warn("Failed to capture browser console logs: {}", e.getMessage());
        }
    }
}