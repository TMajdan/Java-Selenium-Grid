package org.example.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.FrameworkException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Utility class for YAML operations using Jackson YAML dataformat.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlUtils {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(
            new YAMLFactory()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
    )
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules();

    /**
     * Reads a YAML file into a Map.
     *
     * @param filePath the path to the YAML file
     * @return the parsed Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readYaml(String filePath) {
        try {
            return YAML_MAPPER.readValue(new File(filePath), Map.class);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read YAML file: " + filePath, e);
        }
    }

    /**
     * Reads a YAML file into a Map from an InputStream.
     *
     * @param inputStream the input stream
     * @return the parsed Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readYaml(InputStream inputStream) {
        try {
            return YAML_MAPPER.readValue(inputStream, Map.class);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read YAML from stream", e);
        }
    }

    /**
     * Reads a YAML file into a typed object.
     *
     * @param filePath the path to the YAML file
     * @param clazz    the target class
     * @param <T>      the type parameter
     * @return the deserialized object
     */
    public static <T> T readYamlAs(String filePath, Class<T> clazz) {
        try {
            return YAML_MAPPER.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read YAML file as " + clazz.getSimpleName() + ": " + filePath, e);
        }
    }

    /**
     * Writes a Map to a YAML file.
     *
     * @param data     the data to write
     * @param filePath the target file path
     */
    public static void writeYaml(Map<String, Object> data, String filePath) {
        try {
            YAML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
            log.debug("YAML written to: {}", filePath);
        } catch (IOException e) {
            throw new FrameworkException("Failed to write YAML to file: " + filePath, e);
        }
    }
}
