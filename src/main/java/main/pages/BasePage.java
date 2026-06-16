package main.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import main.config.ConfigManager;
import main.driver.DriverManager;
import main.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for all Page Objects.
 * Provides common web interaction methods with automatic waiting,
 * logging, and Allure @Step annotations.
 *
 * All methods are designed to be thread-safe for parallel execution.
 */
@Slf4j
public abstract class BasePage {

    protected final WebDriver driver;
    protected final ConfigManager config;

    /**
     * Constructor initializes the page with the current thread's WebDriver.
     */
    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = ConfigManager.getInstance();
    }

    /**
     * Constructor with explicit WebDriver (for advanced use cases).
     */
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigManager.getInstance();
    }

    /**
     * Navigates to the specified URL.
     */
    @Step("Navigate to URL: {url}")
    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    /**
     * Navigates to the base URL from configuration.
     */
    @Step("Navigate to base URL")
    public void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        navigateTo(baseUrl);
    }

    /**
     * Clicks an element located by the given locator.
     */
    @Step("Click on element: {locator}")
    public void click(By locator) {
        log.debug("Clicking element: {}", locator);
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        element.click();
    }

    /**
     * Clicks on a WebElement.
     */
    @Step("Click on element")
    public void click(WebElement element) {
        log.debug("Clicking element");
        WaitUtils.waitForClickable(driver, element).click();
    }

    /**
     * Types text into an input field, clearing it first.
     */
    @Step("Type '{text}' into element: {locator}")
    public void type(By locator, String text) {
        log.debug("Typing '{}' into element: {}", text, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Types text into a WebElement, clearing it first.
     */
    @Step("Type '{text}' into element")
    public void type(WebElement element, String text) {
        log.debug("Typing '{}' into element", text);
        WaitUtils.waitForVisibleElement(driver, element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets the visible text of an element.
     */
    @Step("Get text from element: {locator}")
    public String getText(By locator) {
        log.debug("Getting text from element: {}", locator);
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    /**
     * Gets the visible text from a WebElement.
     */
    @Step("Get text from element")
    public String getText(WebElement element) {
        log.debug("Getting text from element");
        WaitUtils.waitForVisibleElement(driver, element);
        return element.getText();
    }

    /**
     * Gets the value of an attribute from an element.
     */
    @Step("Get attribute '{attribute}' from element: {locator}")
    public String getAttribute(By locator, String attribute) {
        log.debug("Getting attribute '{}' from element: {}", attribute, locator);
        return WaitUtils.waitForVisible(driver, locator).getDomAttribute(attribute);
    }

    /**
     * Checks if an element is displayed.
     */
    @Step("Check if element is displayed: {locator}")
    public boolean isDisplayed(By locator) {
        try {
            return WaitUtils.isElementVisible(driver, locator);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is displayed (WebElement overload).
     */
    @Step("Check if element is displayed")
    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is enabled.
     */
    @Step("Check if element is enabled: {locator}")
    public boolean isEnabled(By locator) {
        return WaitUtils.waitForVisible(driver, locator).isEnabled();
    }

    /**
     * Checks if an element is selected (checkbox/radio).
     */
    @Step("Check if element is selected: {locator}")
    public boolean isSelected(By locator) {
        return WaitUtils.waitForVisible(driver, locator).isSelected();
    }

    /**
     * Waits for an element to be visible.
     */
    @Step("Wait for element to be visible: {locator}")
    public WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    /**
     * Waits for an element to be visible with a custom timeout.
     */
    @Step("Wait for element to be visible: {locator} (timeout: {timeout}s)")
    public WebElement waitForVisible(By locator, int timeout) {
        return WaitUtils.waitForVisible(driver, locator, timeout);
    }

    /**
     * Waits for an element to be clickable.
     */
    @Step("Wait for element to be clickable: {locator}")
    public WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator);
    }

    /**
     * Scrolls the page until the element is in view.
     */
    @Step("Scroll element into view: {locator}")
    public void scrollIntoView(By locator) {
        log.debug("Scrolling element into view: {}", locator);
        WebElement element = driver.findElement(locator);
        scrollIntoView(element);
    }

    /**
     * Scrolls the page until the WebElement is in view.
     */
    @Step("Scroll element into view")
    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Performs a click using JavaScript (useful when normal click fails).
     */
    @Step("JavaScript click on element: {locator}")
    public void javascriptClick(By locator) {
        log.debug("JavaScript clicking element: {}", locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        javascriptClick(element);
    }

    /**
     * Performs a click using JavaScript on a WebElement.
     */
    @Step("JavaScript click on element")
    public void javascriptClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Hovers over an element.
     */
    @Step("Hover over element: {locator}")
    public void hover(By locator) {
        log.debug("Hovering over element: {}", locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        new Actions(driver).moveToElement(element).perform();
    }

    /**
     * Double-clicks an element.
     */
    @Step("Double-click on element: {locator}")
    public void doubleClick(By locator) {
        log.debug("Double-clicking element: {}", locator);
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        new Actions(driver).doubleClick(element).perform();
    }

    /**
     * Right-clicks (context click) an element.
     */
    @Step("Right-click on element: {locator}")
    public void rightClick(By locator) {
        log.debug("Right-clicking element: {}", locator);
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        new Actions(driver).contextClick(element).perform();
    }

    /**
     * Selects an option from a dropdown by visible text.
     */
    @Step("Select dropdown option '{visibleText}' from: {locator}")
    public void selectDropdownByText(By locator, String visibleText) {
        log.debug("Selecting '{}' from dropdown: {}", visibleText, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        new Select(element).selectByVisibleText(visibleText);
    }

    /**
     * Selects an option from a dropdown by value.
     */
    @Step("Select dropdown option by value '{value}' from: {locator}")
    public void selectDropdownByValue(By locator, String value) {
        log.debug("Selecting value '{}' from dropdown: {}", value, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        new Select(element).selectByValue(value);
    }

    /**
     * Selects an option from a dropdown by index.
     */
    @Step("Select dropdown option by index {index} from: {locator}")
    public void selectDropdownByIndex(By locator, int index) {
        log.debug("Selecting index '{}' from dropdown: {}", index, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        new Select(element).selectByIndex(index);
    }

    /**
     * Gets the currently selected text from a dropdown.
     */
    @Step("Get selected dropdown text from: {locator}")
    public String getSelectedDropdownText(By locator) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        return new Select(element).getFirstSelectedOption().getText();
    }

    /**
     * Gets all options from a dropdown as a list of strings.
     */
    @Step("Get all dropdown options from: {locator}")
    public List<String> getDropdownOptions(By locator) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        return new Select(element).getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Gets the page title.
     */
    @Step("Get page title")
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current URL.
     */
    @Step("Get current URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Refreshes the current page.
     */
    @Step("Refresh page")
    public void refreshPage() {
        log.debug("Refreshing page");
        driver.navigate().refresh();
        WaitUtils.waitForPageLoad(driver);
    }

    /**
     * Switches to a frame by locator.
     */
    @Step("Switch to frame: {locator}")
    public void switchToFrame(By locator) {
        log.debug("Switching to frame: {}", locator);
        WebElement frame = WaitUtils.waitForVisible(driver, locator);
        driver.switchTo().frame(frame);
    }

    /**
     * Switches back to the default content from a frame.
     */
    @Step("Switch to default content")
    public void switchToDefaultContent() {
        log.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }

    /**
     * Accepts an alert.
     */
    @Step("Accept alert")
    public void acceptAlert() {
        log.debug("Accepting alert");
        WaitUtils.waitForAlert(driver).accept();
    }

    /**
     * Dismisses an alert.
     */
    @Step("Dismiss alert")
    public void dismissAlert() {
        log.debug("Dismissing alert");
        WaitUtils.waitForAlert(driver).dismiss();
    }

    /**
     * Gets the text from an alert.
     */
    @Step("Get alert text")
    public String getAlertText() {
        return WaitUtils.waitForAlert(driver).getText();
    }

    /**
     * Finds an element (no wait, quick check).
     */
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Finds a list of elements.
     */
    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Waits for the page to fully load.
     */
    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        WaitUtils.waitForPageLoad(driver);
    }
}
