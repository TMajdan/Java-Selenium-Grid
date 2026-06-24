package driver;

import config.SeleniumProperties;
import config.TimeoutConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.events.EventFiringDecorator;
import java.net.MalformedURLException;
import java.time.Duration;
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

    public static WebDriver getWebDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            log.debug("No driver found for current thread, creating new instance");
            driver = createDriver();
            setDriver(driver);
        }
        return driver;
    }

    private static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.manage().deleteAllCookies();
            } catch (Exception ignored) {}
            try {
                driver.quit();
            } catch (Exception ignored) {}
        }
        DRIVER_THREAD_LOCAL.remove();
    }

    public static Optional<WebDriver> getExistingWebDriver() {
        return Optional.ofNullable(DRIVER_THREAD_LOCAL.get());
    }

    private static WebDriver createDriver() {
        boolean useGrid = SeleniumProperties.isGridMode();
        return useGrid ? createRemoteDriver() : createLocalDriver();
    }

    private static WebDriver createLocalDriver() {
        ChromeOptions options = ChromeOptionsProvider.setChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        log.debug("Local ChromeDriver created");
        return wrapAndConfigure(driver);
    }

    private static WebDriver createRemoteDriver() {
        log.debug("Creating RemoteWebDriver");
        ChromeOptions options = ChromeOptionsProvider.setChromeOptionsRemote();
        URL remoteUrl = parseRemoteUrl();

        WebDriver driver;
        try {
            ClientConfig clientConfig = ClientConfig.defaultConfig()
                    .baseUrl(remoteUrl)
                    .readTimeout(Duration.ofMinutes(3));
            HttpCommandExecutor executor = new HttpCommandExecutor(clientConfig);

            driver = new RemoteWebDriver(executor, options);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RemoteWebDriver at: " + remoteUrl, e);
        }
        log.debug("RemoteWebDriver created successfully");
        return wrapAndConfigure(driver);
    }

    private static WebDriver wrapAndConfigure(WebDriver driver) {
        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(LISTENER);
        WebDriver decorated = decorator.decorate(driver);
        configureDriver(decorated);
        return decorated;
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(TimeoutConfig.getPageLoadDuration());

        if (SeleniumProperties.isMaximizeWindow()) {
            driver.manage().window().maximize();
        } else {
            Dimension dimension = new Dimension(
                    SeleniumProperties.getWindowWidth(),
                    SeleniumProperties.getWindowHeight());
            driver.manage().window().setSize(dimension);
        }
    }

    private static URL parseRemoteUrl() {
        String hubUrl = SeleniumProperties.getHubUrl();
        try {
            return URI.create(hubUrl).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid remote URL: " + hubUrl, e);
        }
    }
}