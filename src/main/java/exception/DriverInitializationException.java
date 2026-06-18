package exception;

/**
 * Exception thrown when WebDriver initialization fails.
 */
public class DriverInitializationException extends FrameworkException {

    public DriverInitializationException(String message) {
        super(message);
    }

    public DriverInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverInitializationException(String message, Object... args) {
        super(String.format(message, args));
    }
}