package config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration manager that loads YAML files from the classpath,
 * merges them into a nested map, and provides property lookup by dot-notation keys.
 * Resolution order (highest priority first):
 * 1. System properties (-Dkey=value)
 * 2. Environment variables (KEY_NAME)
 * 3. YAML configuration files
 */
@Slf4j
public class ConfigManager {

    static final ConfigManager CONFIG = initConfig();

    private final Yaml yaml = new Yaml();
    private final Map<String, Object> config = new ConcurrentHashMap<>();

    private ConfigManager(String classpathResource) {
        loadConfigYaml(classpathResource);
    }

    private static ConfigManager initConfig() {
        ConfigManager cm = new ConfigManager("BasicSettings.yaml");
        String env = cm.getPropertyOrWarn("environment");
        if (env != null && !env.isBlank()) {
            String envUpper = env.toUpperCase();
            cm.loadConfigYaml("setting/" + envUpper + "Settings.yaml");
            cm.loadConfigYaml("users/" + envUpper + "Users.yaml");
        } else {
            log.warn("No 'environment' property found in BasicSettings.yaml");
        }
        return cm;
    }

    @SuppressWarnings("unchecked")
    private void loadConfigYaml(String classpathResource) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(classpathResource)) {
            if (stream == null) {
                log.warn("Configuration file not found in classpath: {}", classpathResource);
                return;
            }
            Object loaded = yaml.load(stream);
            if (loaded instanceof Map) {
                mergeConfig(config, (Map<String, Object>) loaded);
            }
        } catch (IOException e) {
            log.error("Error while loading configuration from classpath: {}", classpathResource, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void mergeConfig(Map<String, Object> dest, Map<String, Object> source) {
        source.forEach((key, value) -> {
            Object existing = dest.get(key);
            if (value instanceof Map && existing instanceof Map) {
                mergeConfig((Map<String, Object>) existing, (Map<String, Object>) value);
            } else {
                dest.put(key, value);
            }
        });
    }

    public String getPropertyOrWarn(String key) {
        String value = System.getProperty(key);
        if (notBlank(value)) return value;

        value = System.getenv(key.replace('.', '_').toUpperCase());
        if (notBlank(value)) return value;

        value = resolveProperty(config, key);
        if (value == null || value.isBlank()) {
            log.warn("Config key is empty: {}", key);
        }
        return value;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    @SuppressWarnings("unchecked")
    private String resolveProperty(Map<String, Object> map, String key) {
        int dot = key.indexOf('.');
        if (dot < 0) {
            Object value = map.get(key);
            return value != null ? String.valueOf(value) : null;
        }
        String segment = key.substring(0, dot);
        String rest = key.substring(dot + 1);
        Object nested = map.get(segment);
        if (nested instanceof Map) {
            return resolveProperty((Map<String, Object>) nested, rest);
        }
        return null;
    }
}