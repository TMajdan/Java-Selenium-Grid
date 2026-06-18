package driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides pre-configured Chrome options for remote desktop execution.
 */
public final class DesktopCapabilitiesManager {

    private DesktopCapabilitiesManager() {
    }

    public static ChromeOptions setChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--no-first-run",
                "--disable-default-apps",
                "--disable-sync",
                "--disable-cache",
                "--disable-dev-shm-usage",
                "--remote-allow-origins=*",
                "--disable-notifications",
                "--disable-popup-blocking",
                "--disable-extensions"
        );

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, false);
        options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, org.openqa.selenium.PageLoadStrategy.NORMAL);

        return options;
    }
}