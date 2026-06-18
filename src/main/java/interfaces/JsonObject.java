package interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.function.Consumer;

public interface JsonObject {

    Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    default String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Allows to easily modify a nested fields of a created model object. Suitable for creating negative or complex test cases.
     * @param lambdaExpression a lambda expression which modifies a given object using JsonPath (ctx.set(), ctx.delete(), ctx.add(), etc.)
     * @return base JSON with modified fields
     */
    default String toJsonWithModification(Consumer<DocumentContext> lambdaExpression) {
        DocumentContext context = JsonPath.parse(toJson());
        try {
            lambdaExpression.accept(context);
        } catch (PathNotFoundException e) {
            throw new PathNotFoundException("Make sure that provided path is valid and present in modified object. If it is not present you can add it with put() method");
        }
        return context.jsonString();
    }

    /**
     * Allows to easily modify a nested fields of a created model object. Suitable for creating negative or complex test cases.
     * @param lambdaExpression a lambda expression which modifies a given object using JsonPath (ctx.set(), ctx.delete(), ctx.add(), etc.)
     * @return base model object with modified fields
     */
    @SuppressWarnings("unchecked")
    default <T> T toObjectWithModification(Consumer<DocumentContext> lambdaExpression) {
        DocumentContext context = JsonPath.parse(toJson());
        try {
            lambdaExpression.accept(context);
        } catch (PathNotFoundException e) {
            throw new PathNotFoundException("Make sure that provided path is valid and present in modified object. If it is not present you can add it with put() method");
        }

        String modifiedJson = context.jsonString();
        try {
            return (T) GSON.fromJson(modifiedJson, this.getClass());
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Failed to parse modified JSON back to " + this.getClass(), e);
        }
    }
}