package destkop.utils.wait;

import general.config.TimeoutConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for WebDriver wait operations.
 * All waits use configurable timeouts from TimeoutConfig.
 * This is the SINGLE SOURCE OF TRUTH for wait logic.
 * BaseActions delegates to this class for all wait operations.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WaitUtils {

    /**
     * Creates a WebDriverWait with the configured default timeout and polling interval.
     *
     * @param driver the WebDriver instance
     * @return a configured WebDriverWait
     */
    public static WebDriverWait getDefaultWait(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, TimeoutConfig.getDefaultDuration());
        wait.pollingEvery(TimeoutConfig.getPollingDuration());
        wait.ignoring(NoSuchElementException.class);
        return wait;
    }

    /**
     * Creates a WebDriverWait with a custom timeout.
     *
     * @param driver         the WebDriver instance
     * @param timeoutSeconds the timeout in seconds
     * @return a configured WebDriverWait
     */
    public static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(TimeoutConfig.getPollingDuration());
        wait.ignoring(NoSuchElementException.class);
        return wait;
    }

    /**
     * Waits for an element to be visible.
     *
     * @param driver the WebDriver instance
     * @param locator the element locator
     * @return the visible WebElement
     */
    public static WebElement waitForVisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to be visible: {}", locator);
        return getDefaultWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be visible with a custom timeout.
     *
     * @param driver         the WebDriver instance
     * @param locator        the element locator
     * @param timeoutSeconds the timeout in seconds
     * @return the visible WebElement
     */
    public static WebElement waitForVisible(WebDriver driver, By locator, int timeoutSeconds) {
        log.debug("Waiting for element to be visible ({}s): {}", timeoutSeconds, locator);
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param driver the WebDriver instance
     * @param locator the element locator
     * @return the clickable WebElement
     */
    public static WebElement waitForClickable(WebDriver driver, By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return getDefaultWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be clickable with a custom timeout.
     *
     * @param driver         the WebDriver instance
     * @param locator        the element locator
     * @param timeoutSeconds the timeout in seconds
     * @return the clickable WebElement
     */
    public static WebElement waitForClickable(WebDriver driver, By locator, int timeoutSeconds) {
        log.debug("Waiting for element to be clickable ({}s): {}", timeoutSeconds, locator);
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be present in the DOM.
     *
     * @param driver  the WebDriver instance
     * @param locator the element locator
     * @return the present WebElement
     */
    public static WebElement waitForPresence(WebDriver driver, By locator) {
        log.debug("Waiting for element presence: {}", locator);
        return getDefaultWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for an element to be invisible.
     *
     * @param driver  the WebDriver instance
     * @param locator the element locator
     * @return true if the element disappeared within the timeout
     */
    public static boolean waitForInvisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to be invisible: {}", locator);
        try {
            return getDefaultWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.warn("Element did not become invisible within timeout: {}", locator);
            return false;
        }
    }

    /**
     * Waits for an element's text to contain the expected value.
     *
     * @param driver       the WebDriver instance
     * @param locator      the element locator
     * @param expectedText the expected text
     * @return true if the text appeared within the timeout
     */
    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String expectedText) {
        log.debug("Waiting for text '{}' in element: {}", expectedText, locator);
        try {
            return getDefaultWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
        } catch (TimeoutException e) {
            log.warn("Text '{}' did not appear in element within timeout: {}", expectedText, locator);
            return false;
        }
    }

    /**
     * Waits for a list of elements to have at least the specified size.
     *
     * @param driver     the WebDriver instance
     * @param locator    the element locator
     * @param minSize    the minimum expected size
     * @return the list of elements
     */
    public static List<WebElement> waitForElementsCount(WebDriver driver, By locator, int minSize) {
        log.debug("Waiting for at least {} elements: {}", minSize, locator);
        return getDefaultWait(driver).until(
                ExpectedConditions.numberOfElementsToBeMoreThan(locator, minSize - 1));
    }

    /**
     * Waits for a custom ExpectedCondition.
     *
     * @param driver    the WebDriver instance
     * @param condition the condition to wait for
     * @param <T>       the return type
     * @return the result of the condition
     */
    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition) {
        log.debug("Waiting for custom condition");
        return getDefaultWait(driver).until(condition);
    }

    /**
     * Waits for a custom condition with a timeout.
     *
     * @param driver         the WebDriver instance
     * @param condition      the condition to wait for
     * @param timeoutSeconds the timeout in seconds
     * @param <T>            the return type
     * @return the result of the condition
     */
    public static <T> T waitForCondition(WebDriver driver, ExpectedCondition<T> condition, int timeoutSeconds) {
        log.debug("Waiting for custom condition ({}s)", timeoutSeconds);
        return getWait(driver, timeoutSeconds).until(condition);
    }

    /**
     * Waits for a generic condition using a Function.
     *
     * @param driver    the WebDriver instance
     * @param condition the function to evaluate
     * @param <T>       the return type
     * @return the result of the function
     */
    public static <T> T waitFor(WebDriver driver, Function<WebDriver, T> condition) {
        return getDefaultWait(driver).until(condition);
    }

    /**
     * Waits for the page to be fully loaded (document.readyState === 'complete').
     *
     * @param driver the WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        log.debug("Waiting for page to load completely");
        getDefaultWait(driver).until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
    }

    /**
     * Waits for an element to be visible (WebElement overload).
     *
     * @param driver  the WebDriver instance
     * @param element the WebElement
     * @return the visible WebElement
     */
    public static WebElement waitForVisibleElement(WebDriver driver, WebElement element) {
        log.debug("Waiting for element to be visible");
        return getDefaultWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for an element to be clickable (WebElement overload).
     *
     * @param driver  the WebDriver instance
     * @param element the WebElement
     * @return the clickable WebElement
     */
    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        log.debug("Waiting for element to be clickable");
        return getDefaultWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for an alert to be present.
     *
     * @param driver the WebDriver instance
     * @return the alert
     */
    public static Alert waitForAlert(WebDriver driver) {
        log.debug("Waiting for alert to be present");
        return getDefaultWait(driver).until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Checks if an element is visible without waiting for the full timeout.
     *
     * @param driver  the WebDriver instance
     * @param locator the element locator
     * @return true if the element is visible within a short wait
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            getWait(driver, 3).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}