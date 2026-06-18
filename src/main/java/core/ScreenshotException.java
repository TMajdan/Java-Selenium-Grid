package core;

/**
 * Exception thrown when screenshot capture or storage fails.
 */
public class ScreenshotException extends FrameworkException {

    public ScreenshotException(String message) {
        super(message);
    }

    public ScreenshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScreenshotException(String message, Object... args) {
        super(String.format(message, args));
    }
}