package org.example.driver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.BrowserType;
import org.example.config.ConfigManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating browser-specific options/capabilities.
 * Configures headless, incognito, notifications, proxy, and other settings
 * based on the application YAML configuration.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriverOptions {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    /**
     * Creates browser-specific options based on the BrowserType.
     *
     * @param browserType the browser type
     * @return configured MutableCapabilities for the browser
     */
    public static MutableCapabilities createOptions(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> createChromeOptions();
            case FIREFOX -> createFirefoxOptions();
            case EDGE -> createEdgeOptions();
        };
    }

    /**
     * Creates ChromeOptions with all configured settings.
     */
    public static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        applyCommonSettings(options);

        // Chrome-specific arguments
        options.addArguments("--no-first-run");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-cache");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Experimental options
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", CONFIG.getDownloadDir());
        prefs.put("download.prompt_for_download", false);
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));

        log.debug("ChromeOptions configured");
        return options;
    }

    /**
     * Creates FirefoxOptions with all configured settings.
     */
    public static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        applyCommonSettings(options);

        // Firefox-specific preferences
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", CONFIG.getDownloadDir());
        options.addPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/pdf,application/octet-stream");
        options.addPreference("pdfjs.disabled", true);
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("signon.rememberSignons", false);

        log.debug("FirefoxOptions configured");
        return options;
    }

    /**
     * Creates EdgeOptions with all configured settings.
     */
    public static EdgeOptions createEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        applyCommonSettings(options);

        // Edge-specific arguments
        options.addArguments("--no-first-run");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-sync");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", CONFIG.getDownloadDir());
        prefs.put("download.prompt_for_download", false);
        options.setExperimentalOption("prefs", prefs);

        log.debug("EdgeOptions configured");
        return options;
    }

    /**
     * Applies common settings across all browser types.
     */
    private static void applyCommonSettings(MutableCapabilities options) {
        // Headless mode
        if (CONFIG.isHeadless()) {
            if (options instanceof ChromeOptions) {
                ((ChromeOptions) options).addArguments("--headless=new");
            } else if (options instanceof FirefoxOptions) {
                ((FirefoxOptions) options).addArguments("-headless");
            } else if (options instanceof EdgeOptions) {
                ((EdgeOptions) options).addArguments("--headless=new");
            }
            log.info("Headless mode enabled");
        }

        // Incognito/private mode
        if (CONFIG.isIncognitoMode()) {
            if (options instanceof ChromeOptions) {
                ((ChromeOptions) options).addArguments("--incognito");
            } else if (options instanceof FirefoxOptions) {
                ((FirefoxOptions) options).addArguments("-private");
            } else if (options instanceof EdgeOptions) {
                ((EdgeOptions) options).addArguments("--inprivate");
            }
            log.info("Incognito mode enabled");
        }

        // Disable notifications
        if (CONFIG.isDisableNotifications()) {
            if (options instanceof ChromeOptions) {
                ((ChromeOptions) options).addArguments("--disable-notifications");
            } else if (options instanceof EdgeOptions) {
                ((EdgeOptions) options).addArguments("--disable-notifications");
            }
            // Firefox handles this via preferences in createFirefoxOptions
        }

        // Disable popups
        if (CONFIG.isDisablePopups()) {
            if (options instanceof ChromeOptions) {
                ((ChromeOptions) options).addArguments("--disable-popup-blocking");
            } else if (options instanceof EdgeOptions) {
                ((EdgeOptions) options).addArguments("--disable-popup-blocking");
            }
        }

        // Disable extensions
        if (CONFIG.isDisableExtensions()) {
            if (options instanceof ChromeOptions) {
                ((ChromeOptions) options).addArguments("--disable-extensions");
            } else if (options instanceof EdgeOptions) {
                ((EdgeOptions) options).addArguments("--disable-extensions");
            }
        }

        // Accept insecure certificates
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,
                CONFIG.isAcceptInsecureCerts());

        // Page load strategy
        String strategy = CONFIG.getPageLoadStrategy();
        switch (strategy.toLowerCase()) {
            case "normal" -> options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,
                    PageLoadStrategy.NORMAL);
            case "eager" -> options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,
                    PageLoadStrategy.EAGER);
            case "none" -> options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,
                    PageLoadStrategy.NONE);
            default -> options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY,
                    PageLoadStrategy.NORMAL);
        }

        // Proxy configuration
        if (CONFIG.isProxyEnabled()) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(CONFIG.getProxyHost() + ":" + CONFIG.getProxyPort());
            proxy.setSslProxy(CONFIG.getProxyHost() + ":" + CONFIG.getProxyPort());
            options.setCapability(CapabilityType.PROXY, proxy);
            log.info("Proxy configured: {}:{}", CONFIG.getProxyHost(), CONFIG.getProxyPort());
        }
    }
}
