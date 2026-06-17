package driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * Thread-safe WebDriver manager using ThreadLocal.
 * Ensures each test thread gets its own WebDriver instance.
 * Provides centralized driver lifecycle management.
 */
@Slf4j
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns the WebDriver instance for the current thread.
     * Creates one if not already initialized.
     *
     * @return the WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            log.info("No driver found for current thread, creating new instance");
            driver = DriverFactory.createDriver();
            setDriver(driver);
        }
        return driver;
    }

    /**
     * Returns the WebDriver instance for the current thread without creating one.
     *
     * @return an Optional containing the driver, or empty if not initialized
     */
    public static Optional<WebDriver> getExistingDriver() {
        return Optional.ofNullable(DRIVER_THREAD_LOCAL.get());
    }

    /**
     * Sets the WebDriver instance for the current thread.
     *
     * @param driver the WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    /**
     * Quits the WebDriver for the current thread and removes it.
     * Safe to call multiple times.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                log.debug("WebDriver quit successfully for thread: {}",
                        Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver", e);
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * Closes the current browser window (quits if last window).
     */
    public static void closeWindow() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.close();
        }
    }

    /**
     * Removes the driver from ThreadLocal without quitting.
     * Useful when the driver lifecycle is managed externally.
     */
    public static void removeDriver() {
        DRIVER_THREAD_LOCAL.remove();
    }

    /**
     * Returns the current thread name for logging purposes.
     */
    public static String getCurrentThreadInfo() {
        return String.format("[Thread: %s, ID: %d]",
                Thread.currentThread().getName(), Thread.currentThread().threadId());
    }
}
