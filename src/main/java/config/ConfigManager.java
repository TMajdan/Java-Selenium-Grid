package config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
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

    public static ConfigManager CONFIG;

    private final Yaml yaml = new Yaml();
    private final HashMap<String, Object> config = new HashMap<>();

    public ConfigManager(String filename) {
        loadFileSettings(filename);
    }

    public void loadFileSettings(String fileName) {
        try (InputStream stream = new FileInputStream(fileName)) {
            updateMap(config, yaml.load(stream));
        } catch (IOException e) {
            log.warn("cannot load file {}", fileName);
            throw new RuntimeException("Failed to load: " + fileName, e);
        }
    }

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

    public String getProperty(String propertyName) {
        String systemProperty = System.getProperty(propertyName);
        if (systemProperty != null) {
            return systemProperty;
        } else {
            return findProperty(config, propertyName);
        }
    }
}