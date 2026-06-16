package main.core;

/**
 * Exception thrown when configuration loading or parsing fails.
 */
public class ConfigurationException extends FrameworkException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message, Object... args) {
        super(String.format(message, args));
    }
}
