package base;

import config.TestProperties;
import driver.BaseDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.screenshot.ScreenshotUtils;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Base class for all test classes.
 * Provides setup/teardown lifecycle, thread-safe driver management,
 * and reporting configuration.
 */
@Slf4j
@SuppressWarnings("null")
public abstract class TestBase {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        deleteAllureResultsDirectory();
        log.info("=======================================");
        log.info("  Environment: {}  |  Grid: {}  |  Debug: {}  |  Headless: {}  |  Threads: {}",
                TestProperties.getEnvironment(),
                TestProperties.isGridMode(),
                TestProperties.isDebugMode(),
                TestProperties.isHeadlessMode(),
                TestProperties.getThreadCount());
        log.info("=======================================");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        BaseDriver.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        Optional<WebDriver> optDriver = BaseDriver.getExistingDriver();
        if (!result.isSuccess()) {
            if (optDriver.isPresent()) {
                log.warn("  [FAIL] {} - screenshot captured", result.getName());
                ScreenshotUtils.attachScreenshot(result.getName() + " - Failure");
            } else {
                log.warn("  [FAIL] {} - no driver available for screenshot", result.getName());
            }
        }
        BaseDriver.quitDriver();
    }

    public WebDriver getDriver() {
        return BaseDriver.getDriver();
    }

    public void navigateToBaseUrl() {
        getDriver().get(TestProperties.getBaseUrl());
    }

    private static void deleteAllureResultsDirectory() {
        try {
            Files.walk(Paths.get("target/allure-results"))
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> p.toFile().delete());
        } catch (Exception ignored) {
        }
    }
}