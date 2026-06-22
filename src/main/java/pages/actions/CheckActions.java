package pages.actions;

import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.wait.WaitUtils;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CheckActions {

    public static boolean isElementDisplayed(WebDriver driver, By locator) {
        try {
            Allure.step("Verify " + locator + " is displayed");
            WaitUtils.getWait(driver, 3)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static boolean isElementDisplayed(WebDriver driver, WebElement element) {
        try {
            Allure.step("Verify element is displayed");
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementDisplayedInShadowRoot(WebDriver driver, By shadowHostLocator, By shadowElementLocator) {
        try {
            Allure.step("Verify element in shadow root " + shadowElementLocator + " is displayed");
            WebElement shadowHost = WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.visibilityOfElementLocated(shadowHostLocator));
            WebElement shadowElement = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot.querySelector(arguments[1])",
                            shadowHost, shadowElementLocator.toString().replace("By.cssSelector: ", ""));
            return shadowElement != null && shadowElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to be visible: {}", locator);
        return Allure.step("Wait for " + locator + " to be visible", () ->
                WaitUtils.getDefaultWait(driver)
                        .until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, int timeoutSeconds) {
        log.debug("Waiting for element to be visible ({}s): {}", timeoutSeconds, locator);
        return Allure.step("Wait for " + locator + " to be visible (" + timeoutSeconds + "s)", () ->
                WaitUtils.getWait(driver, timeoutSeconds)
                         .until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    public static WebElement waitForVisibleElement(WebDriver driver, WebElement element) {
        log.debug("Waiting for element to be visible");
        return Allure.step("Wait for element to appear", () ->
                WaitUtils.getDefaultWait(driver)
                        .until(ExpectedConditions.visibilityOf(element)));
    }

    public static boolean isEnabled(WebDriver driver, By locator) {
        return Allure.step("Verify " + locator + " is enabled", () -> waitForVisible(driver, locator).isEnabled());
    }

    public static boolean isSelected(WebDriver driver, By locator) {
        return Allure.step("Verify " + locator + " is selected", () -> waitForVisible(driver, locator).isSelected());
    }

    public static List<WebElement> waitForElementsCount(WebDriver driver, By locator, int minSize) {
        log.debug("Waiting for at least {} elements: {}", minSize, locator);
        return Allure.step("Wait for at least " + minSize + " elements matching " + locator, () ->
                WaitUtils.getDefaultWait(driver).until(
                        ExpectedConditions.numberOfElementsToBeMoreThan(locator, minSize - 1)));
    }

    public static boolean waitForInvisible(WebDriver driver, By locator) {
        log.debug("Waiting for element to be invisible: {}", locator);
        try {
            return Allure.step("Wait for " + locator + " to disappear", () ->
                    WaitUtils.getDefaultWait(driver).until(
                            ExpectedConditions.invisibilityOfElementLocated(locator)));
        } catch (TimeoutException e) {
            log.warn("Element did not become invisible within timeout: {}", locator);
            return false;
        }
    }

    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String expectedText) {
        log.debug("Waiting for text '{}' in element: {}", expectedText, locator);
        try {
            return Allure.step("Wait for text '" + expectedText + "' to appear in " + locator, () ->
                    WaitUtils.getDefaultWait(driver).until(
                            ExpectedConditions.textToBePresentInElementLocated(locator, expectedText)));
        } catch (TimeoutException e) {
            log.warn("Text '{}' did not appear in element within timeout: {}", expectedText, locator);
            return false;
        }
    }
}