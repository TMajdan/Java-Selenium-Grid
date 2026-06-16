package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.FrameworkException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for JSON serialization and deserialization.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .findAndRegisterModules();

    /**
     * Serializes an object to a JSON string.
     *
     * @param object the object to serialize
     * @return the JSON string
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new FrameworkException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Serializes an object to a pretty-printed JSON string.
     *
     * @param object the object to serialize
     * @return the pretty JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new FrameworkException("Failed to serialize object to pretty JSON", e);
        }
    }

    /**
     * Deserializes a JSON string to an object.
     *
     * @param json  the JSON string
     * @param clazz the target class
     * @param <T>   the type parameter
     * @return the deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new FrameworkException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Deserializes a JSON file to an object.
     *
     * @param file  the JSON file
     * @param clazz the target class
     * @param <T>   the type parameter
     * @return the deserialized object
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read JSON file: " + file.getPath(), e);
        }
    }

    /**
     * Deserializes a JSON string to a list of objects.
     *
     * @param json  the JSON string
     * @param clazz the element class
     * @param <T>   the type parameter
     * @return the list of deserialized objects
     */
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        try {
            CollectionType type = OBJECT_MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, clazz);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new FrameworkException("Failed to deserialize JSON list to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Writes an object as JSON to a file.
     *
     * @param file   the target file
     * @param object the object to write
     */
    public static void writeToFile(File file, Object object) {
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, object);
            log.debug("JSON written to: {}", file.getPath());
        } catch (IOException e) {
            throw new FrameworkException("Failed to write JSON to file: " + file.getPath(), e);
        }
    }
}
