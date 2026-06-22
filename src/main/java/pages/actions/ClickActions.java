package pages.actions;

import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.wait.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("null")
public final class ClickActions {

    public static void click(WebDriver driver, By locator) {
        log.debug("Clicking element: {}", locator);
        Allure.step("Click on " + locator, () -> {
            WebElement element = WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        });
    }

    public static void clickOnElement(WebDriver driver, WebElement element) {
        log.debug("Clicking element");
        Allure.step("Click on element", () -> {
            WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.elementToBeClickable(element))
                    .click();
        });
    }

    public static void rightClick(WebDriver driver, By locator) {
        log.debug("Right-clicking element: {}", locator);
        Allure.step("Right-click on " + locator, () -> {
            WebElement element = WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).contextClick(element).perform();
        });
    }

    public static void doubleClick(WebDriver driver, By locator) {
        log.debug("Double-clicking element: {}", locator);
        Allure.step("Double-click on " + locator, () -> {
            WebElement element = WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).doubleClick(element).perform();
        });
    }

    public static void hover(WebDriver driver, By locator) {
        log.debug("Hovering over element: {}", locator);
        Allure.step("Hover over " + locator, () -> {
            WebElement element = CheckActions.waitForVisible(driver, locator);
            new Actions(driver).moveToElement(element).perform();
        });
    }


    public static WebElement waitForClickable(WebDriver driver, By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return Allure.step("Wait for " + locator + " to be clickable", () ->
                WaitUtils.getDefaultWait(driver)
                        .until(ExpectedConditions.elementToBeClickable(locator)));
    }
}