package actions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static config.ConfigManager.CONFIG;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BaseActions {

    private static final int DEFAULT_TIMEOUT = Integer.parseInt(CONFIG.getPropertyOrWarn("execution.timeout"));
    private static final int POLLING_INTERVAL = Integer.parseInt(CONFIG.getPropertyOrWarn("execution.pollingInterval"));

    public static WebDriverWait getDefaultWait(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL));
        wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
        return wait;
    }

    public static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL));
        wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
        return wait;
    }

    public static void waitForPageLoaded(WebDriver driver) {
        log.debug("Waiting for page to load completely");
        getDefaultWait(driver).until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")));
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition) {
        log.debug("Waiting for custom condition");
        return getDefaultWait(driver).until(condition);
    }

    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition, int timeoutSeconds) {
        log.debug("Waiting for custom condition ({}s)", timeoutSeconds);
        return getWait(driver, timeoutSeconds).until(condition);
    }

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
        waitForPageLoaded(driver);
    }
}