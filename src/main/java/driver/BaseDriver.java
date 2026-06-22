package driver;

import config.TestProperties;
import config.TimeoutConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

@Slf4j
@SuppressWarnings("null")
public final class BaseDriver {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static final DriverListener LISTENER = new DriverListener();

    private BaseDriver() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            log.info("No driver found for current thread, creating new instance");
            driver = createDriver();
            setDriver(driver);
        }
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                log.debug("WebDriver quit successfully for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver", e);
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    public static Optional<WebDriver> getExistingDriver() {
        return Optional.ofNullable(DRIVER_THREAD_LOCAL.get());
    }

    public static void closeWindow() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.close();
            } catch (Exception e) {
                log.warn("Error while closing browser window", e);
            }
        }
    }

    private static WebDriver createDriver() {
        boolean useGrid = TestProperties.isGridMode();
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
            throw new RuntimeException("Failed to create RemoteWebDriver at: " + remoteUrl, e);
        }

        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(LISTENER);
        WebDriver decorated = decorator.decorate(originalDriver);

        configureDriver(decorated);
        log.info("RemoteWebDriver created successfully");
        return decorated;
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(TimeoutConfig.getPageLoadDuration());

        if (TestProperties.isMaximizeWindow()) {
            driver.manage().window().maximize();
        } else {
            org.openqa.selenium.Dimension dimension = new org.openqa.selenium.Dimension(
                    TestProperties.getWindowWidth(),
                    TestProperties.getWindowHeight());
            driver.manage().window().setSize(dimension);
        }
    }

    private static URL parseRemoteUrl() {
        String hubUrl = TestProperties.getHubUrl();
        try {
            return URI.create(hubUrl).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid remote URL: " + hubUrl, e);
        }
    }
}