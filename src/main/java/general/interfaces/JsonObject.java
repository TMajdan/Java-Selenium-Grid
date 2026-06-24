package general.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.function.Consumer;

/**
 * Interface for model objects that can be serialized to/from JSON.
 * Provides utility methods for modifying JSON fields via JsonPath,
 * useful for creating negative or complex test cases.
 */
public interface JsonObject {

    ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    default String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Allows to easily modify nested fields of a created model object.
     * Suitable for creating negative or complex test cases.
     *
     * @param lambdaExpression a lambda expression which modifies a given object
     *                         using JsonPath (ctx.set(), ctx.delete(), ctx.add(), etc.)
     * @return base JSON with modified fields
     */
    default String toJsonWithModification(Consumer<DocumentContext> lambdaExpression) {
        DocumentContext context = JsonPath.parse(toJson());
        try {
            lambdaExpression.accept(context);
        } catch (PathNotFoundException e) {
            throw new PathNotFoundException("Make sure that provided path is valid and present in modified object. "
                    + "If it is not present you can add it with put() method");
        }
        return context.jsonString();
    }

    /**
     * Allows to easily modify nested fields of a created model object.
     * Suitable for creating negative or complex test cases.
     *
     * @param lambdaExpression a lambda expression which modifies a given object
     *                         using JsonPath (ctx.set(), ctx.delete(), ctx.add(), etc.)
     * @return base model object with modified fields
     */
    @SuppressWarnings("unchecked")
    default <T> T toObjectWithModification(Consumer<DocumentContext> lambdaExpression) {
        DocumentContext context = JsonPath.parse(toJson());
        try {
            lambdaExpression.accept(context);
        } catch (PathNotFoundException e) {
            throw new PathNotFoundException("Make sure that provided path is valid and present in modified object. "
                    + "If it is not present you can add it with put() method");
        }

        String modifiedJson = context.jsonString();
        try {
            return (T) MAPPER.readValue(modifiedJson, this.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse modified JSON back to " + this.getClass(), e);
        }
    }
}