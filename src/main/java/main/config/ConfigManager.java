package main.config;

import lombok.extern.slf4j.Slf4j;
import main.core.ConfigurationException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Thread-safe singleton configuration manager.
 * Loads application.yaml once and provides typed accessors for all settings.
 * Supports runtime overrides via system properties (-Dproperty=value).
 */
@Slf4j
public final class ConfigManager {

    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/config/application.yaml";
    private static final String ACTIVE_ENV_KEY = "environment";
    private static final String DEFAULT_ENV = "qa";

    private static volatile ConfigManager instance;
    private static final Object lock = new Object();

    private final Map<String, Object> config;
    private final String activeEnvironment;
    private final Map<String, Object> executionConfig;
    private final Map<String, Object> gridConfig;
    private final Map<String, Object> seleniumConfig;
    private final Map<String, Object> pathsConfig;
    private final Map<String, Object> reportingConfig;
    private final Map<String, Object> remoteConfig;

    private ConfigManager() {
        this.config = loadConfig();
        this.activeEnvironment = resolveActiveEnvironment();
        this.executionConfig = getNestedMap("execution");
        this.gridConfig = getNestedMap("grid");
        this.seleniumConfig = getNestedMap("selenium");
        this.pathsConfig = getNestedMap("paths");
        this.reportingConfig = getNestedMap("reporting");
        this.remoteConfig = getNestedMap("remote");
        log.info("ConfigManager initialized for environment: {}", activeEnvironment);
    }

    /**
     * Returns the singleton instance (thread-safe, lazy initialization).
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Resets the singleton (useful for testing).
     */
    public static void reset() {
        synchronized (lock) {
            instance = null;
        }
    }

    // ========================================================================
    // Environment methods
    // ========================================================================

    /**
     * Returns the active environment name (dev, qa, stage, prod).
     */
    public String getActiveEnvironment() {
        return activeEnvironment;
    }

    /**
     * Returns the base URL for the active environment.
     */
    public String getBaseUrl() {
        return getEnvironmentValue("baseUrl");
    }

    /**
     * Returns the API base URL for the active environment.
     */
    public String getApiBaseUrl() {
        return getEnvironmentValue("apiBaseUrl");
    }

    /**
     * Returns the admin username for the active environment.
     */
    public String getAdminUsername() {
        return getEnvironmentValue("adminUsername");
    }

    /**
     * Returns the admin password for the active environment.
     */
    public String getAdminPassword() {
        return getEnvironmentValue("adminPassword");
    }

    // ========================================================================
    // Execution methods
    // ========================================================================

    /**
     * Returns the execution mode (LOCAL or GRID).
     */
    public ExecutionMode getExecutionMode() {
        String mode = getStringProperty("execution.mode", executionConfig, "execution.mode");
        return ExecutionMode.fromString(mode);
    }

    /**
     * Returns the configured browser type.
     */
    public BrowserType getBrowser() {
        String browser = getStringProperty("execution.browser", executionConfig, "execution.browser");
        return BrowserType.fromString(browser);
    }

    /**
     * Returns whether headless mode is enabled.
     */
    public boolean isHeadless() {
        return getBooleanProperty("execution.headless", executionConfig, "execution.headless");
    }

    /**
     * Returns the default timeout in seconds.
     */
    public int getTimeout() {
        return getIntProperty("execution.timeout", executionConfig, "execution.timeout");
    }

    /**
     * Returns the page load timeout in seconds.
     */
    public int getPageLoadTimeout() {
        return getIntProperty("execution.pageLoadTimeout", executionConfig, "execution.pageLoadTimeout");
    }

    /**
     * Returns the implicit wait timeout in seconds.
     */
    public int getImplicitlyWait() {
        return getIntProperty("execution.implicitlyWait", executionConfig, "execution.implicitlyWait");
    }

    /**
     * Returns the polling interval in milliseconds.
     */
    public int getPollingInterval() {
        return getIntProperty("execution.pollingInterval", executionConfig, "execution.pollingInterval");
    }

    /**
     * Returns the thread count for parallel execution.
     */
    public int getThreadCount() {
        return getIntProperty("execution.threadCount", executionConfig, "execution.threadCount");
    }

    /**
     * Returns the retry count for flaky tests.
     */
    public int getRetryCount() {
        return getIntProperty("execution.retryCount", executionConfig, "execution.retryCount");
    }

    /**
     * Returns whether screenshots on failure are enabled.
     */
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("execution.screenshotOnFailure", executionConfig, "execution.screenshotOnFailure");
    }

    /**
     * Returns the max screenshot history count.
     */
    public int getMaxScreenshotHistory() {
        return getIntProperty("execution.maxScreenshotHistory", executionConfig, "execution.maxScreenshotHistory");
    }

    // ========================================================================
    // Grid methods
    // ========================================================================

    /**
     * Returns the Selenium Grid hub URL.
     */
    public String getGridHubUrl() {
        return getStringProperty("grid.hubUrl", gridConfig, "grid.hubUrl");
    }

    /**
     * Returns the Grid username (if authentication is used).
     */
    public String getGridUsername() {
        return getStringProperty("grid.username", gridConfig, "grid.username");
    }

    /**
     * Returns the Grid password (if authentication is used).
     */
    public String getGridPassword() {
        return getStringProperty("grid.password", gridConfig, "grid.password");
    }

    /**
     * Returns whether VNC is enabled for Grid.
     */
    public boolean isGridVncEnabled() {
        return getBooleanProperty("grid.enableVnc", gridConfig, "grid.enableVnc");
    }

    /**
     * Returns whether video recording is enabled for Grid.
     */
    public boolean isGridVideoEnabled() {
        return getBooleanProperty("grid.enableVideo", gridConfig, "grid.enableVideo");
    }

    // ========================================================================
    // Selenium options methods
    // ========================================================================

    /**
     * Returns whether to maximize the browser window.
     */
    public boolean isMaximizeWindow() {
        return getBooleanProperty("selenium.maximizeWindow", seleniumConfig, "selenium.maximizeWindow");
    }

    /**
     * Returns whether incognito mode is enabled.
     */
    public boolean isIncognitoMode() {
        return getBooleanProperty("selenium.incognitoMode", seleniumConfig, "selenium.incognitoMode");
    }

    /**
     * Returns whether notifications are disabled.
     */
    public boolean isDisableNotifications() {
        return getBooleanProperty("selenium.disableNotifications", seleniumConfig, "selenium.disableNotifications");
    }

    /**
     * Returns whether popups are disabled.
     */
    public boolean isDisablePopups() {
        return getBooleanProperty("selenium.disablePopups", seleniumConfig, "selenium.disablePopups");
    }

    /**
     * Returns whether extensions are disabled.
     */
    public boolean isDisableExtensions() {
        return getBooleanProperty("selenium.disableExtensions", seleniumConfig, "selenium.disableExtensions");
    }

    /**
     * Returns whether insecure certificates are accepted.
     */
    public boolean isAcceptInsecureCerts() {
        return getBooleanProperty("selenium.acceptInsecureCerts", seleniumConfig, "selenium.acceptInsecureCerts");
    }

    /**
     * Returns the page load strategy.
     */
    public String getPageLoadStrategy() {
        return getStringProperty("selenium.pageLoadStrategy", seleniumConfig, "selenium.pageLoadStrategy");
    }

    /**
     * Returns the window width.
     */
    public int getWindowWidth() {
        return getIntProperty("selenium.windowWidth", seleniumConfig, "selenium.windowWidth");
    }

    /**
     * Returns the window height.
     */
    public int getWindowHeight() {
        return getIntProperty("selenium.windowHeight", seleniumConfig, "selenium.windowHeight");
    }

    // ========================================================================
    // Paths methods
    // ========================================================================

    /**
     * Returns the download directory path.
     */
    public String getDownloadDir() {
        return getStringProperty("paths.downloadDir", pathsConfig, "paths.downloadDir");
    }

    /**
     * Returns the screenshot directory path.
     */
    public String getScreenshotDir() {
        return getStringProperty("paths.screenshotDir", pathsConfig, "paths.screenshotDir");
    }

    /**
     * Returns the logs directory path.
     */
    public String getLogsDir() {
        return getStringProperty("paths.logsDir", pathsConfig, "paths.logsDir");
    }

    /**
     * Returns the Allure results directory path.
     */
    public String getAllureResultsDir() {
        return getStringProperty("paths.allureResultsDir", pathsConfig, "paths.allureResultsDir");
    }

    /**
     * Returns the test data directory path.
     */
    public String getTestDataDir() {
        return getStringProperty("paths.testDataDir", pathsConfig, "paths.testDataDir");
    }

    // ========================================================================
    // Reporting methods
    // ========================================================================

    /**
     * Returns whether Allure reporting is enabled.
     */
    public boolean isAllureEnabled() {
        return getBooleanProperty("reporting.allureEnabled", reportingConfig, "reporting.allureEnabled");
    }

    /**
     * Returns whether ExtentReports is enabled.
     */
    public boolean isExtentEnabled() {
        return getBooleanProperty("reporting.extentEnabled", reportingConfig, "reporting.extentEnabled");
    }

    /**
     * Returns the ExtentReports path.
     */
    public String getExtentReportPath() {
        return getStringProperty("reporting.extentReportPath", reportingConfig, "reporting.extentReportPath");
    }

    // ========================================================================
    // Remote/proxy methods
    // ========================================================================

    /**
     * Returns whether proxy is enabled.
     */
    public boolean isProxyEnabled() {
        return getBooleanProperty("remote.enableProxy", remoteConfig, "remote.enableProxy");
    }

    /**
     * Returns the proxy host.
     */
    public String getProxyHost() {
        return getStringProperty("remote.proxyHost", remoteConfig, "remote.proxyHost");
    }

    /**
     * Returns the proxy port.
     */
    public int getProxyPort() {
        return getIntProperty("remote.proxyPort", remoteConfig, "remote.proxyPort");
    }

    /**
     * Returns the proxy protocol.
     */
    public String getProxyProtocol() {
        return getStringProperty("remote.proxyProtocol", remoteConfig, "remote.proxyProtocol");
    }

    // ========================================================================
    // Generic typed accessors
    // ========================================================================

    /**
     * Returns a raw string value from the config by dot-notation key.
     * Checks system properties first, then YAML config.
     */
    public String getString(String key) {
        return Optional.ofNullable(System.getProperty(key))
                .orElseGet(() -> resolveValue(key, String.class));
    }

    /**
     * Returns an integer value from the config by dot-notation key.
     */
    public int getInt(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null) {
            return Integer.parseInt(sysProp);
        }
        return resolveValue(key, Integer.class);
    }

    /**
     * Returns a boolean value from the config by dot-notation key.
     */
    public boolean getBoolean(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null) {
            return Boolean.parseBoolean(sysProp);
        }
        return resolveValue(key, Boolean.class);
    }

    // ========================================================================
    // Private helpers
    // ========================================================================

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadConfig() {
        Path configPath = findConfigPath();
        log.debug("Loading configuration from: {}", configPath.toAbsolutePath());

        try (InputStream input = Files.newInputStream(configPath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> raw = yaml.load(input);
            if (raw == null) {
                throw new ConfigurationException("Configuration file is empty: " + configPath);
            }
            return raw;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to load configuration from: " + configPath, e);
        }
    }

    private Path findConfigPath() {
        // Check system property override
        String configPathProp = System.getProperty("config.path");
        if (configPathProp != null && Files.exists(Paths.get(configPathProp))) {
            return Paths.get(configPathProp);
        }
        // Check classpath-relative path
        Path defaultPath = Paths.get(DEFAULT_CONFIG_PATH);
        if (Files.exists(defaultPath)) {
            return defaultPath;
        }
        // Fallback: try to find in classpath
        return Paths.get("src/main/resources/config/application.yaml");
    }

    private String resolveActiveEnvironment() {
        // Check system property first, then default
        return Optional.ofNullable(System.getProperty(ACTIVE_ENV_KEY))
                .orElse(DEFAULT_ENV);
    }

    @SuppressWarnings("unchecked")
    private <T> T getEnvironmentValue(String key) {
        Map<String, Object> environments = getNestedMap("environments");
        Map<String, Object> envConfig = (Map<String, Object>) environments.get(activeEnvironment);
        if (envConfig == null) {
            throw new ConfigurationException("Environment configuration not found for: " + activeEnvironment);
        }
        return (T) envConfig.get(key);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNestedMap(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = config;
        for (String k : keys) {
            Object value = current.get(k);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else {
                return Collections.emptyMap();
            }
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveValue(String dotKey, Class<T> type) {
        String[] keys = dotKey.split("\\.");
        Map<String, Object> current = config;
        for (String key : keys) {
            Object value = current.get(key);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else if (value != null) {
                if (type.isInstance(value)) {
                    return (T) value;
                }
                if (type == Integer.class && value instanceof Number) {
                    return (T) Integer.valueOf(((Number) value).intValue());
                }
                if (type == String.class) {
                    return (T) String.valueOf(value);
                }
                return (T) value;
            } else {
                throw new ConfigurationException("Configuration key not found: " + dotKey);
            }
        }
        throw new ConfigurationException("Configuration key not found: " + dotKey);
    }

    private String getStringProperty(String dotKey, Map<String, Object> configMap, String sysPropKey) {
        return Optional.ofNullable(System.getProperty(sysPropKey))
                .orElseGet(() -> {
                    Object value = resolveFromMap(dotKey, configMap);
                    return value != null ? String.valueOf(value) : null;
                });
    }

    private int getIntProperty(String dotKey, Map<String, Object> configMap, String sysPropKey) {
        String sysProp = System.getProperty(sysPropKey);
        if (sysProp != null) {
            return Integer.parseInt(sysProp);
        }
        Object value = resolveFromMap(dotKey, configMap);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private boolean getBooleanProperty(String dotKey, Map<String, Object> configMap, String sysPropKey) {
        String sysProp = System.getProperty(sysPropKey);
        if (sysProp != null) {
            return Boolean.parseBoolean(sysProp);
        }
        Object value = resolveFromMap(dotKey, configMap);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    private Object resolveFromMap(String dotKey, Map<String, Object> configMap) {
        String[] keys = dotKey.split("\\.");
        Map<String, Object> current = configMap;
        // Skip the first key if it matches the section name
        int startIndex = 0;
        if (keys.length > 1) {
            startIndex = 1;
        }
        for (int i = startIndex; i < keys.length; i++) {
            Object value = current.get(keys[i]);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else {
                return value;
            }
        }
        return current;
    }
}
