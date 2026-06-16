package org.example.driver;

import lombok.extern.slf4j.Slf4j;
import org.example.config.BrowserType;
import org.example.config.ConfigManager;
import org.example.config.ExecutionMode;
import org.example.core.DriverInitializationException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * Factory for creating WebDriver instances.
 * Supports local and remote (Selenium Grid) execution modes.
 * Thread-safe: each call creates a new driver instance.
 */
@Slf4j
public final class DriverFactory {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private DriverFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a WebDriver instance based on the configured execution mode and browser.
     *
     * @return a configured WebDriver instance
     */
    public static WebDriver createDriver() {
        return createDriver(CONFIG.getBrowser(), CONFIG.getExecutionMode());
    }

    /**
     * Creates a WebDriver instance for a specific browser and execution mode.
     *
     * @param browserType   the browser to use
     * @param executionMode the execution mode (LOCAL or GRID)
     * @return a configured WebDriver instance
     */
    public static WebDriver createDriver(BrowserType browserType, ExecutionMode executionMode) {
        log.info("Creating WebDriver - Browser: {}, Mode: {}", browserType, executionMode);

        WebDriver driver = switch (executionMode) {
            case LOCAL -> createLocalDriver(browserType);
            case GRID -> createRemoteDriver(browserType);
        };

        configureDriver(driver);
        log.info("WebDriver created successfully: {}", driver.getClass().getSimpleName());
        return driver;
    }

    /**
     * Creates a local WebDriver instance.
     */
    private static WebDriver createLocalDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> {
                ChromeOptions options = DriverOptions.createChromeOptions();
                yield new ChromeDriver(options);
            }
            case FIREFOX -> {
                FirefoxOptions options = DriverOptions.createFirefoxOptions();
                yield new FirefoxDriver(options);
            }
            case EDGE -> {
                EdgeOptions options = DriverOptions.createEdgeOptions();
                yield new EdgeDriver(options);
            }
        };
    }

    /**
     * Creates a remote WebDriver instance (Selenium Grid).
     */
    private static WebDriver createRemoteDriver(BrowserType browserType) {
        MutableCapabilities options = DriverOptions.createOptions(browserType);
        URL gridUrl = parseGridUrl();

        log.info("Connecting to Selenium Grid at: {}", gridUrl);
        try {
            return new RemoteWebDriver(gridUrl, options);
        } catch (Exception e) {
            throw new DriverInitializationException("Failed to create remote driver at: " + gridUrl, e);
        }
    }

    /**
     * Configures common driver settings (timeouts, window management).
     */
    private static void configureDriver(WebDriver driver) {
        // Timeouts
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(CONFIG.getPageLoadTimeout()));
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(CONFIG.getImplicitlyWait()));

        // Window management
        if (CONFIG.isMaximizeWindow()) {
            driver.manage().window().maximize();
        } else {
            org.openqa.selenium.Dimension dimension = new org.openqa.selenium.Dimension(
                    CONFIG.getWindowWidth(), CONFIG.getWindowHeight());
            driver.manage().window().setSize(dimension);
        }

        log.debug("Driver configured with timeouts - pageLoad: {}s, implicitlyWait: {}s",
                CONFIG.getPageLoadTimeout(), CONFIG.getImplicitlyWait());
    }

    /**
     * Parses the Grid hub URL from configuration.
     */
    private static URL parseGridUrl() {
        try {
            return URI.create(CONFIG.getGridHubUrl()).toURL();
        } catch (MalformedURLException e) {
            throw new DriverInitializationException("Invalid Grid hub URL: " + CONFIG.getGridHubUrl(), e);
        }
    }
}
