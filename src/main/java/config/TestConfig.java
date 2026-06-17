package config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Central configuration loader that assembles all YAML settings files
 * into a single ConfigManager instance.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConfig {

    private static final String PROPERTIES_PATH = "src/main/resources/";
    public static final ConfigManager CONFIG = loadConfig();

    private static ConfigManager loadConfig() {
        ConfigManager config = new ConfigManager(PROPERTIES_PATH + "BasicSettings.yaml");
        ConfigManager.CONFIG = config;
        String environment = config.getProperty("environment");
        if (environment != null) {
            String envUpper = environment.toUpperCase();
            config.loadFileSettings(PROPERTIES_PATH + "setting/" + String.format("%sSettings.yaml", envUpper));
            config.loadFileSettings(PROPERTIES_PATH + "users/" + String.format("%sUsers.yaml", envUpper));
            log.info("TestConfig initialized for environment: {}", environment);
        } else {
            log.warn("No 'environment' property found in BasicSettings.yaml");
        }

        return config;
    }
}