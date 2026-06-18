package pages.actions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GetActions {

    public static String getText(WebDriver driver, By locator) {
        log.debug("Getting text from element: {}", locator);
        return CheckActions.waitForVisible(driver, locator).getText();
    }

    public static String getText(WebDriver driver, WebElement element) {
        log.debug("Getting text from element");
        CheckActions.waitForVisibleElement(driver, element);
        return element.getText();
    }

    public static String getAttribute(WebDriver driver, By locator, String attribute) {
        log.debug("Getting attribute '{}' from element: {}", attribute, locator);
        return CheckActions.waitForVisible(driver, locator).getDomAttribute(attribute);
    }

    public static String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    public static String getCurrentUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    public static String getSelectedDropdownText(WebDriver driver, By locator) {
        WebElement element = CheckActions.waitForVisible(driver, locator);
        return new Select(element).getFirstSelectedOption().getText();
    }


    public static List<String> getDropdownOptions(WebDriver driver, By locator) {
        WebElement element = CheckActions.waitForVisible(driver, locator);
        return new Select(element).getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}