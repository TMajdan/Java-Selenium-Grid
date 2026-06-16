package org.example.core;

/**
 * Base exception class for all framework-specific exceptions.
 * Provides structured error handling across the automation framework.
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(String message, Object... args) {
        super(String.format(message, args));
    }

    public FrameworkException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }
}
