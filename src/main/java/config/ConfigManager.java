package config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration manager that loads YAML files, merges them into a nested map,
 * and provides property lookup by dot-notation keys.
 */
@Slf4j
public class ConfigManager {

    private static final String PROPERTIES_PATH = "src/main/resources/";

    public static final ConfigManager CONFIG = initConfig();

    private final Yaml yaml = new Yaml();
    private final HashMap<String, Object> config = new HashMap<>();

    public ConfigManager(String filename) {
        loadConfigYaml(filename);
    }

    private static ConfigManager initConfig() {
        ConfigManager config = new ConfigManager(PROPERTIES_PATH + "BasicSettings.yaml");
        String environment = config.getPropertyOrWarn("environment");
        if (environment != null) {
            String envUpper = environment.toUpperCase();
            config.loadConfigYaml(PROPERTIES_PATH + "setting/" + String.format("%sSettings.yaml", envUpper));
            config.loadConfigYaml(PROPERTIES_PATH + "users/" + String.format("%sUsers.yaml", envUpper));
        } else {
            log.warn("No 'environment' property found in BasicSettings.yaml");
        }
        return config;
    }

    /**
     * Loads a specified configuration file and merges it into the config map.
     *
     * @param configPath Path to the configuration file.
     */
    private void loadConfigYaml(String configPath) {
        try {
            YamlFile yamlFile = new YamlFile(configPath);

            if (!yamlFile.exists()) {
                throw new FileNotFoundException("Configuration file not found: " + configPath);
            }
            yamlFile.load();

            // Parse via snakeyaml and merge into the config map
            try (InputStream stream = new FileInputStream(configPath)) {
                Object loaded = this.yaml.load(stream);
                if (loaded instanceof Map) {
                    //noinspection unchecked
                    updateMap(this.config, (Map<String, Object>) loaded);
                    //log.info("Loaded configuration from: {}", configPath);
                }
            }
        } catch (FileNotFoundException e) {
            log.warn("Configuration file not found: {}", configPath);
        } catch (IOException e) {
            log.error("Error while loading configuration from: {}", configPath, e);
        }
    }

    public void updateMap(Map<String, Object> destMap, Map<String, Object> sourceMap) {
        sourceMap.forEach((key, sourceValue) -> {
            Object destValue = destMap.get(key);
            if (sourceValue instanceof Map && destValue instanceof Map) {
                //noinspection unchecked
                updateMap((Map<String, Object>) destValue, (Map<String, Object>) sourceValue);
            } else {
                destMap.put(key, sourceValue);
            }
        });
    }

    public String getPropertyOrWarn(String propertyName) {
        try {
            String systemProperty = System.getProperty(propertyName);
            String value;
            if (systemProperty != null) {
                value = systemProperty;
            } else {
                value = findProperty(config, propertyName);
            }

            if (value == null || value.isBlank()) {
                System.err.printf("[YAML CONFIGURATION] Config key is empty: %s%n", propertyName);
            }
            return value;
        } catch (Exception e) {
            System.err.printf("[YAML CONFIGURATION] Config key not found: %s%n %s%n", propertyName, e.getMessage());
            return null;
        }
    }

    private String findProperty(Map<String, Object> config, String propertyName) {
        int dotPos = propertyName.indexOf('.');
        if (dotPos < 0) {
            Object value = config.get(propertyName);
            return value != null ? String.valueOf(value) : null;
        } else {
            String key = propertyName.substring(0, dotPos);
            String reminder = propertyName.substring(dotPos + 1);
            Map<String, Object> subMap = getSubMap(config, key);
            if (subMap != null) {
                return findProperty(subMap, reminder);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getSubMap(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else {
            return new HashMap<>();
        }
    }
}