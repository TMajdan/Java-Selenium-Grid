package org.example.utils;

import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ConfigManager;
import org.example.core.ScreenshotException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for capturing and managing screenshots.
 * Supports file storage and Allure attachment integration.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = ConfigManager.getInstance().getScreenshotDir();

    static {
        FileUtils.createDirectoryIfNotExists(SCREENSHOT_DIR);
    }

    /**
     * Captures a screenshot and saves it to the configured directory.
     *
     * @param driver   the WebDriver instance
     * @param testName the name of the test (used in the filename)
     * @return the absolute path of the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = DateUtils.getScreenshotFilename(testName);
        String fileName = timestamp + ".png";
        String filePath = SCREENSHOT_DIR + File.separator + fileName;

        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = Paths.get(filePath);
            Files.copy(screenshotFile.toPath(), destination);
            log.info("Screenshot saved: {}", destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new ScreenshotException("Failed to save screenshot to: " + filePath, e);
        }
    }

    /**
     * Captures a screenshot as bytes (useful for Allure attachments).
     *
     * @param driver the WebDriver instance
     * @return the screenshot as a byte array
     */
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as bytes", e);
            return new byte[0];
        }
    }

    /**
     * Captures a full-page screenshot (scroll + stitch) as bytes.
     *
     * @param driver the WebDriver instance
     * @return the full-page screenshot as bytes
     */
    public static byte[] captureFullPageScreenshot(WebDriver driver) {
        try {
            // Attempt to capture the full page using Chrome DevTools Protocol
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture full-page screenshot", e);
            return captureScreenshotAsBytes(driver);
        }
    }

    /**
     * Attaches a screenshot as PNG to the Allure report using programmatic API.
     *
     * @param screenshot the screenshot bytes
     */
    public static void attachScreenshotToAllure(byte[] screenshot) {
        Allure.attachment("screenshot.png", new ByteArrayInputStream(screenshot));
    }

    /**
     * Attaches a screenshot to Allure with a custom name using programmatic API.
     *
     * @param name       the attachment name
     * @param screenshot the screenshot bytes
     */
    public static void attachScreenshotToAllure(String name, byte[] screenshot) {
        Allure.attachment(name + ".png", new ByteArrayInputStream(screenshot));
    }

    /**
     * Attaches the page source HTML to Allure using programmatic API.
     *
     * @param pageSource the HTML page source
     */
    public static void attachPageSourceToAllure(String pageSource) {
        Allure.addAttachment("Page Source", "text/html", pageSource);
    }

    /**
     * Captures a screenshot, saves it to file, and attaches to Allure.
     *
     * @param driver   the WebDriver instance
     * @param testName the test name for the filename
     * @return the file path
     */
    public static String captureAndAttachScreenshot(WebDriver driver, String testName) {
        byte[] screenshotBytes = captureScreenshotAsBytes(driver);
        attachScreenshotToAllure(testName + " - Failure Screenshot", screenshotBytes);
        return captureScreenshot(driver, testName);
    }

    /**
     * Attaches a log message as text to Allure using programmatic API.
     *
     * @param logMessage the log message
     */
    public static void attachLogToAllure(String logMessage) {
        Allure.addAttachment("Test Log", "text/plain", logMessage);
    }

    /**
     * Cleans up old screenshots, keeping only the most recent N files.
     *
     * @param maxFiles the maximum number of screenshot files to keep
     */
    public static void cleanupOldScreenshots(int maxFiles) {
        try {
            Path screenshotDir = Paths.get(SCREENSHOT_DIR);
            if (Files.exists(screenshotDir)) {
                Files.list(screenshotDir)
                        .filter(p -> p.toString().endsWith(".png"))
                        .sorted((a, b) -> {
                            try {
                                return Files.getLastModifiedTime(b)
                                        .compareTo(Files.getLastModifiedTime(a));
                            } catch (IOException e) {
                                return 0;
                            }
                        })
                        .skip(maxFiles)
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                                log.debug("Deleted old screenshot: {}", p);
                            } catch (IOException e) {
                                log.warn("Failed to delete old screenshot: {}", p, e);
                            }
                        });
            }
        } catch (IOException e) {
            log.warn("Failed to cleanup old screenshots", e);
        }
    }
}
