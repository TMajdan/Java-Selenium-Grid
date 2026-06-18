package driver;

import config.TimeoutConfig;
import lombok.extern.slf4j.Slf4j;
import static config.ConfigManager.CONFIG;
import exception.DriverInitializationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * Factory for creating WebDriver instances.
 * Uses RemoteWebDriver when selenium.grid=true, local ChromeDriver otherwise.
 */
@Slf4j
public final class DriverFactory {

    private static final DriverListener LISTENER = new DriverListener();

    private DriverFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static WebDriver createDriver() {
        boolean useGrid = Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.grid"));
        return useGrid ? createRemoteDriver() : createLocalDriver();
    }

    private static WebDriver createLocalDriver() {
        ChromeOptions options = ChromeOptionsProvider.buildChromeOptions();
        ChromeDriver originalDriver = new ChromeDriver(options);

        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(LISTENER);
        WebDriver decorated = decorator.decorate(originalDriver);

        configureDriver(decorated);
        log.debug("Local ChromeDriver created");
        return decorated;
    }

    private static WebDriver createRemoteDriver() {
        log.info("Creating RemoteWebDriver");

        ChromeOptions options = ChromeOptionsProvider.buildChromeOptions();
        URL remoteUrl = parseRemoteUrl();

        RemoteWebDriver originalDriver;
        try {
            originalDriver = new RemoteWebDriver(remoteUrl, options);
        } catch (Exception e) {
            throw new DriverInitializationException("Failed to create RemoteWebDriver at: " + remoteUrl, e);
        }

        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(LISTENER);
        WebDriver decorated = decorator.decorate(originalDriver);

        configureDriver(decorated);
        log.info("RemoteWebDriver created successfully");
        return decorated;
    }

    private static void configureDriver(WebDriver driver) {
        // Never mix implicit and explicit waits – set implicitlyWait to 0.
        // All waiting is handled by WaitUtils / BaseActions using explicit waits only.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(TimeoutConfig.getPageLoadDuration());

        if (Boolean.parseBoolean(CONFIG.getPropertyOrWarn("selenium.maximizeWindow"))) {
            driver.manage().window().maximize();
        } else {
            org.openqa.selenium.Dimension dimension = new org.openqa.selenium.Dimension(
                    Integer.parseInt(CONFIG.getPropertyOrWarn("selenium.windowWidth")),
                    Integer.parseInt(CONFIG.getPropertyOrWarn("selenium.windowHeight")));
            driver.manage().window().setSize(dimension);
        }
    }

    private static URL parseRemoteUrl() {
        String hubUrl = CONFIG.getPropertyOrWarn("grid.hubUrl");
        try {
            return URI.create(hubUrl).toURL();
        } catch (MalformedURLException e) {
            throw new DriverInitializationException("Invalid remote URL: " + hubUrl, e);
        }
    }
}