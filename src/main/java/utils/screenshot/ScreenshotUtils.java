package utils.screenshot;

import driver.BaseDriver;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/**
 * Utility class for attaching screenshots and page source to Allure reports.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScreenshotUtils {

    /**
     * Captures a screenshot from the current thread's driver and attaches it to Allure.
     *
     * @param stepName description of the step (displayed as attachment name in Allure)
     */
    public static void attachScreenshot(String stepName) {
        try {
            byte[] screenshot = ((TakesScreenshot) BaseDriver.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(stepName + ".png", new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
        }
    }

    public static void attachPageSource(String pageSource) {
        Allure.addAttachment("Page Source", "text/html", pageSource);
    }

    public static void attachLog(String logMessage) {
        Allure.addAttachment("Test Log", "text/plain", logMessage);
    }
}