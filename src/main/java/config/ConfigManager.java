package config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration manager that loads YAML files from the classpath,
 * merges them into a nested map, and provides property lookup by dot-notation keys.
 * <p>
 * Resolution order (highest priority first):
 * 1. System properties (-Dkey=value)
 * 2. Environment variables (KEY_NAME)
 * 3. YAML configuration files
 */
@Slf4j
public class ConfigManager {

    public static final ConfigManager CONFIG = initConfig();

    private final Yaml yaml = new Yaml();
    private final Map<String, Object> config = new ConcurrentHashMap<>();

    public ConfigManager(String classpathResource) {
        loadConfigYaml(classpathResource);
    }

    private static ConfigManager initConfig() {
        ConfigManager config = new ConfigManager("BasicSettings.yaml");
        String environment = config.getPropertyOrWarn("environment");
        if (environment != null && !environment.isBlank()) {
            String envUpper = environment.toUpperCase();
            config.loadConfigYaml("setting/" + envUpper + "Settings.yaml");
            config.loadConfigYaml("users/" + envUpper + "Users.yaml");
        } else {
            log.warn("No 'environment' property found in BasicSettings.yaml");
        }
        return config;
    }

    /**
     * Loads a YAML configuration file from the classpath and merges it into the config map.
     *
     * @param classpathResource classpath-relative path (e.g. "BasicSettings.yaml")
     */
    private void loadConfigYaml(String classpathResource) {
        try (InputStream stream = getClass().getClassLoader()
                .getResourceAsStream(classpathResource)) {
            if (stream == null) {
                log.warn("Configuration file not found in classpath: {}", classpathResource);
                return;
            }
            Object loaded = this.yaml.load(stream);
            if (loaded instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> loadedMap = (Map<String, Object>) loaded;
                updateMap(this.config, loadedMap);
            }
        } catch (IOException e) {
            log.error("Error while loading configuration from classpath: {}", classpathResource, e);
        }
    }

    @SuppressWarnings("unchecked")
    public void updateMap(Map<String, Object> destMap, Map<String, Object> sourceMap) {
        sourceMap.forEach((key, sourceValue) -> {
            Object destValue = destMap.get(key);
            if (sourceValue instanceof Map && destValue instanceof Map) {
                updateMap((Map<String, Object>) destValue, (Map<String, Object>) sourceValue);
            } else {
                destMap.put(key, sourceValue);
            }
        });
    }

    public String getPropertyOrWarn(String propertyName) {
        try {
            String systemProperty = System.getProperty(propertyName);
            if (systemProperty != null && !systemProperty.isBlank()) {
                return systemProperty;
            }

            String envKey = propertyName.replace('.', '_').toUpperCase();
            String envValue = System.getenv(envKey);
            if (envValue != null && !envValue.isBlank()) {
                return envValue;
            }

            String value = findProperty(config, propertyName);
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