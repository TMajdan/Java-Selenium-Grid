package destkop.driver;

import general.config.SeleniumProperties;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Provides pre-configured ChromeOptions for ChromeDriver and RemoteWebDriver.
 */
public final class ChromeOptionsProvider {

    private ChromeOptionsProvider() {
    }

    public static ChromeOptions setChromeOptionsRemote() {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.WARNING);

        ChromeOptions options = new ChromeOptions()
                .addArguments("--start-maximized")
                .addArguments("--disable-infobars")
                .addArguments("--disable-extensions")
                .addArguments("--disable-notifications")
                .addArguments("--disable-dev-shm-usage")
                .addArguments("--no-sandbox")
                .addArguments("--ignore-certificate-errors")
                .addArguments("--remote-allow-origins=*");
        if (isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1200,1024");
        }
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.setCapability("goog:loggingPrefs", logPrefs);
        options.setCapability(CapabilityType.PLATFORM_NAME, "ANY");
        options.setCapability("se:name", "sme-banking");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return options;
    }

    public static ChromeOptions setChromeOptions() {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.WARNING);

        ChromeOptions options = (ChromeOptions) new ChromeOptions()
                .addArguments("--start-maximized")
                .addArguments("--disable-infobars")
                .addArguments("--disable-extensions")
                .addArguments("--disable-notifications")
                .addArguments("--disable-modal-animations")
                .addArguments("--disable-web-security")
                .addArguments("--disable-popup-blocking")
                .addArguments("--no-sandbox")
                .addArguments("--ignore-certificate-errors")
                .addArguments("--remote-allow-origins=*")
                .setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking", "enable-automation"))
                .setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (isHeadless()) {
            options.addArguments(
                    "--headless",
                    "--disable-gpu",
                    "--window-size=1920,1200",
                    "--ignore-certificate-errors",
                    "--disable-extensions",
                    "--no-sandbox",
                    "--disable-dev-shm-usage");
        }

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("credentials_enable_service", false);
        options.setExperimentalOption("prefs", prefs);
        options.setCapability("goog:loggingPrefs", logPrefs);

        return options;
    }

    private static boolean isHeadless() {
        return SeleniumProperties.isHeadlessMode();
    }
}