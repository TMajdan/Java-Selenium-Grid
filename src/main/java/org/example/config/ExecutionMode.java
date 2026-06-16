package org.example.config;

/**
 * Enum representing execution modes for test runs.
 */
public enum ExecutionMode {
    LOCAL,
    GRID;

    /**
     * Resolves an ExecutionMode from a string value (case-insensitive).
     *
     * @param value the mode string
     * @return the matching ExecutionMode
     * @throws IllegalArgumentException if no match is found
     */
    public static ExecutionMode fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Execution mode cannot be null or empty");
        }
        String trimmed = value.trim().toUpperCase();
        for (ExecutionMode mode : values()) {
            if (mode.name().equals(trimmed)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported execution mode: " + value
                + ". Supported values: LOCAL, GRID");
    }
}
