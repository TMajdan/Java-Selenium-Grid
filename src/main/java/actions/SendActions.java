package actions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SendActions {

    public static void sendKeys(By locator, String text, WebDriver driver) {
        log.debug("Typing '{}' into element: {}", text, locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    public static void sendKeys(WebElement element, String text, WebDriver driver) {
        log.debug("Typing '{}' into element", text);
        CheckActions.waitForVisibleElement(driver, element);
        element.clear();
        element.sendKeys(text);
    }

    public static void sendKeys(By locator, Keys key, WebDriver driver) {
        log.debug("Sending key '{}' to element: {}", key, locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        element.sendKeys(key);
    }

    public static void selectDropdownByText(WebDriver driver, By locator, String visibleText) {
        log.debug("Selecting '{}' from dropdown: {}", visibleText, locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        new Select(element).selectByVisibleText(visibleText);
    }

    public static void selectDropdownByValue(WebDriver driver, By locator, String value) {
        log.debug("Selecting value '{}' from dropdown: {}", value, locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        new Select(element).selectByValue(value);
    }


    public static void selectDropdownByIndex(WebDriver driver, By locator, int index) {
        log.debug("Selecting index '{}' from dropdown: {}", index, locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        new Select(element).selectByIndex(index);
    }
}