package destkop.driver;

import general.config.SeleniumProperties;
import general.config.TestProperties;
import general.config.TimeoutConfig;
import lombok.experimental.UtilityClass;
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
@UtilityClass
@SuppressWarnings("null")
public class BaseDriver {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static final DriverListener LISTENER = new DriverListener();

    public static WebDriver getWebDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            log.debug("No driver found for current thread, creating new instance");
            driver = createDriver();
            if (driver != null) {
                setDriver(driver);
            }
        }
        return driver;
    }

    public static Optional<WebDriver> getExistingWebDriver() {
        return Optional.ofNullable(DRIVER_THREAD_LOCAL.get());
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try { driver.manage().deleteAllCookies(); } catch (Exception ignored) { }
            try { driver.quit(); } catch (Exception ignored) { }
        }
        DRIVER_THREAD_LOCAL.remove();
    }

    private static WebDriver createDriver() {
        String framework = TestProperties.getFramework();
        log.debug("Framework: {}", framework);

        return switch (framework) {
            case "selenium" -> runDesktopTests();
            case "api" -> {
                log.info("API mode - no browser driver needed");
                yield null;
            }
            case "mobile" -> {
                log.info("Mobile mode - not yet implemented");
                yield null;
            }
            default -> throw new IllegalStateException("Unexpected framework: " + framework);
        };
    }

    private static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    private static WebDriver runDesktopTests() {
        return SeleniumProperties.isGridMode()
                ? runDesktopTestsRemotely()
                : runDesktopTestsLocally();
    }

    private static WebDriver runDesktopTestsLocally() {
        ChromeOptions options = ChromeOptionsProvider.setChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        log.debug("Local ChromeDriver created");
        return wrapAndConfigure(driver);
    }

    private static WebDriver runDesktopTestsRemotely() {
        log.debug("Creating RemoteWebDriver");
        ChromeOptions options = ChromeOptionsProvider.setChromeOptionsRemote();
        URL remoteUrl = parseRemoteUrl();

        try {
            ClientConfig clientConfig = ClientConfig.defaultConfig()
                    .baseUrl(remoteUrl)
                    .readTimeout(Duration.ofMinutes(3));

            HttpCommandExecutor executor = new HttpCommandExecutor(clientConfig);
            WebDriver driver = new RemoteWebDriver(executor, options);

            log.debug("RemoteWebDriver created successfully");
            return wrapAndConfigure(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RemoteWebDriver at: " + remoteUrl, e);
        }
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
            driver.manage().window().setSize(new Dimension(
                    SeleniumProperties.getWindowWidth(),
                    SeleniumProperties.getWindowHeight()));
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