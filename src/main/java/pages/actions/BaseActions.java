package pages.actions;

import config.TimeoutConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.wait.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;

/**
 * Base action utilities for browser-level operations (navigation, tabs, page refresh).
 * All wait operations are delegated to {@link WaitUtils} – the single source of truth.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BaseActions {

    // ── Wait delegates (single source of truth: WaitUtils) ──

    public static void waitForPageLoaded(WebDriver driver) {
        WaitUtils.waitForPageLoad(driver);
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition) {
        return WaitUtils.waitForCondition(driver, condition);
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition, int timeoutSeconds) {
        return WaitUtils.waitForCondition(driver, condition, timeoutSeconds);
    }

    // ── Browser-level operations ──

    /** @deprecated Use {@code driver.manage().timeouts().implicitlyWait(Duration.ZERO)} directly.
     *  Implicit waits should be set to 0 when using explicit waits. */
    @Deprecated
    public static void setDefaultImplicitlyWait(WebDriver driver, int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
        log.debug("Implicit wait set to {}s", timeoutSeconds);
    }

    public static void switchToNewTab(WebDriver driver) {
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                log.debug("Switched to new tab: {}", windowHandle);
                return;
            }
        }
        log.warn("No new tab found");
    }

    public static void refreshPage(WebDriver driver) {
        log.debug("Refreshing page");
        driver.navigate().refresh();
        WaitUtils.waitForPageLoad(driver);
    }
}