package utils.screenshot;

/**
 * ThreadLocal holder for the last captured screenshot bytes.
 * Used to pass screenshot data from TestBase.tearDown() to AllureListener
 * since the WebDriver is already quit by the time listener callbacks fire.
 */
public final class ScreenshotHolder {

    private static final ThreadLocal<byte[]> lastScreenshotBytes = new ThreadLocal<>();

    private ScreenshotHolder() {
    }

    public static void set(byte[] bytes) {
        lastScreenshotBytes.set(bytes);
    }

    public static byte[] get() {
        return lastScreenshotBytes.get();
    }

    public static void clear() {
        lastScreenshotBytes.remove();
    }
}