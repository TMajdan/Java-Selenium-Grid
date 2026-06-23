package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@Slf4j
public class Utils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Extracts a value from a JSON object based on a given key and converts it to the specified type.
     * Supports nested paths separated by dots and array indexing with bracket notation (e.g., "proposals.proposal[0].quoteFxRate").
     *
     * @param jsonObject the JSONObject from which to extract the value
     * @param key the key to locate the value in the JSON object (can be nested, separated by dots, with optional array indices)
     * @param type the Class type of the value to be extracted
     * @return the extracted value cast to the specified type, or null if not found or conversion fails
     */
    private static Object extractValueFromJson(JSONObject jsonObject, String key, Class<?> type) {
        String[] pathParts = key.split("\\.");
        for (int i = 0; i < pathParts.length - 1; i++) {
            String pathSegment = pathParts[i];

            if (pathSegment.contains("[")) {
                int bracketStart = pathSegment.indexOf('[');
                int bracketEnd = pathSegment.indexOf(']');
                int arrayIndex = Integer.parseInt(pathSegment.substring(bracketStart + 1, bracketEnd));
                String arrayKey = pathSegment.substring(0, bracketStart);

                JSONArray jsonArray = jsonObject.getJSONArray(arrayKey);
                jsonObject = jsonArray.getJSONObject(arrayIndex);
            } else {
                jsonObject = jsonObject.getJSONObject(pathSegment);
            }
        }

        String lastKey = pathParts[pathParts.length - 1];

        return switch (type.getSimpleName()) {
            case "String" -> jsonObject.optString(lastKey);
            case "Integer" -> jsonObject.optInt(lastKey);
            case "Long" -> jsonObject.optLong(lastKey);
            case "Float" -> jsonObject.optFloat(lastKey);
            case "BigDecimal" -> jsonObject.optBigDecimal(lastKey, new BigDecimal(0));
            case "Boolean" -> jsonObject.optBoolean(lastKey);
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
        };
    }

    public static String extractValueFromJsonAsString(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (String) extractValueFromJson(jsonObject, valueToExtract, String.class);
    }

    public static int extractValueFromJsonAsInteger(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (Integer) extractValueFromJson(jsonObject, valueToExtract, Integer.class);
    }

    public static long extractValueFromJsonAsLong(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (Long) extractValueFromJson(jsonObject, valueToExtract, Long.class);
    }

    public static float extractValueFromJsonAsFloat(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (Float) extractValueFromJson(jsonObject, valueToExtract, Float.class);
    }

    public static BigDecimal extractValueFromJsonAsBigDecimal(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (BigDecimal) extractValueFromJson(jsonObject, valueToExtract, BigDecimal.class);
    }

    public static boolean extractValueFromJsonAsBoolean(String jsonBody, String valueToExtract) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        return (Boolean) extractValueFromJson(jsonObject, valueToExtract, Boolean.class);
    }

    /**
     * Extracts a JSON element at the given JsonPath and returns it as a pretty-printed JSON string.
     * Supports nested paths including array notation, e.g. "elements.DE_104.subElements.SE_56".
     *
     * @param jsonBody the full JSON response body as a string
     * @param path     JsonPath expression pointing to the desired element
     * @return pretty-printed JSON string for objects/arrays, string representation for primitives, or null if the path does not exist.
     */
    public static String extractElementFromJson(String jsonBody, String path) {
        Object value = JsonPath.from(jsonBody).get(path);
        if (value instanceof Map || value instanceof List) {
            try {
                return MAPPER.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(value);
            } catch (Exception e) {
                log.warn("Failed to serialize JSON element at path '{}', falling back to toString()", path, e);
                return String.valueOf(value);
            }
        }
        return String.valueOf(value);
    }

    public static BigDecimal extractBigDecimalFromResponse(ValidatableResponse responseBody, String valueToExtract) {
        return Utils.extractValueFromJsonAsBigDecimal(responseBody.extract().asString(), valueToExtract);
    }

    public static int countResponseFields(ValidatableResponse response) {
        String contentType = response.extract().contentType();
        if (contentType != null && contentType.contains("application/json")) {
            return countJsonFields(response.extract().jsonPath().get());
        } else {
            // Plain text or other content types are considered as a single field
            log.debug("verifyNumberOfResponseFields() received non-JSON response (content-type: {}) expectedNumberOfFields = 1", contentType);
            return 1;
        }
    }

    public static int countJsonFields(Object jsonResponse) {
        if (jsonResponse instanceof Map<?, ?> jsonObject) {
            return jsonObject.size() + jsonObject.values().stream().mapToInt(Utils::countJsonFields).sum();
        } else if (jsonResponse instanceof List<?> jsonArray) {
            return jsonArray.stream().mapToInt(Utils::countJsonFields).sum();
        }

        return 0;
    }

    public static float amountChargedByTransactionWithCommission(ValidatableResponse response) {
        float transactionAmount = response.extract().path("transactionAmountInMerchantSettlementCurrency");
        float transactionCommission = response.extract().path("fenigeCommissionInMerchantSettlementCurrency");

        return transactionAmount + transactionCommission;
    }

    public static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to resolve local IP address", e);
        }
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a single field in a JSON string, example:
     * updateJsonField(json, "email", email);
     *
     * @param json      the original JSON string
     * @param fieldName the name of the field to update
     * @param newValue  the new value for the field
     * @return the updated JSON string
     * @throws RuntimeException if the input JSON is invalid
     */
    public static String updateJsonField(String json, String fieldName, Object newValue) {
        try {
            Map<String, Object> map = MAPPER.readValue(json, new TypeReference<>() {});
            map.put(fieldName, newValue);
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update JSON field: " + fieldName, e);
        }
    }
}