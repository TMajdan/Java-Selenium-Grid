package destkop.actions;

import io.qameta.allure.Allure;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import destkop.utils.wait.WaitUtils;

import java.time.Duration;

/**
 * Base action utilities for browser-level operations (navigation, tabs, page refresh).
 * All wait operations are delegated to {@link WaitUtils} – the single source of truth.
 */
@Slf4j
@NoArgsConstructor()
@SuppressWarnings("null")
public final class BaseActions {
    public static void waitForPageLoaded(WebDriver driver) {
        Allure.step("Wait for page to fully load", () -> WaitUtils.waitForPageLoad(driver));
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition) {
        return Allure.step("Wait for custom condition", () -> WaitUtils.waitForCondition(driver, condition));
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition, int timeoutSeconds) {
        return Allure.step("Wait for custom condition (" + timeoutSeconds + "s timeout)", () ->
                WaitUtils.waitForCondition(driver, condition, timeoutSeconds));
    }

    /** @deprecated Use {@code driver.manage().timeouts().implicitlyWait(Duration.ZERO)} directly.
     *  Implicit waits should be set to 0 when using explicit waits. */
    @Deprecated
    public static void setDefaultImplicitlyWait(WebDriver driver, int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
        log.debug("Implicit wait set to {}s", timeoutSeconds);
    }

    public static void switchToNewTab(WebDriver driver) {
        Allure.step("Switch to new tab", () -> {
            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!originalWindow.equals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    log.debug("Switched to new tab: {}", windowHandle);
                    return;
                }
            }
            log.warn("No new tab found");
        });
    }

    public static void refreshPage(WebDriver driver) {
        log.debug("Refreshing page");
        Allure.step("Refresh page", () -> {
            driver.navigate().refresh();
            WaitUtils.waitForPageLoad(driver);
        });
    }
}