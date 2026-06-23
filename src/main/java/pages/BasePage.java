package pages;

import config.TestProperties;
import driver.BaseDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import actions.BaseActions;
import actions.BrowserActions;
import actions.CheckActions;
import actions.ClickActions;
import actions.GetActions;
import actions.SendActions;

import java.util.List;

/**
 * Base class for all Page Objects.
 * Provides common web interaction methods with automatic waiting,
 * logging, and Allure @Step annotations.
 *
 * All methods are designed to be thread-safe for parallel execution.
 */
@Slf4j
@SuppressWarnings("null")
public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage() {
        this.driver = BaseDriver.getWebDriver();
    }

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to URL: {url}")
    public void navigateTo(String url) {
        BrowserActions.navigateTo(driver, url);
    }

    @Step("Navigate to base URL")
    public void navigateToBaseUrl() {
        String baseUrl = TestProperties.getBaseUrl();
        navigateTo(baseUrl);
    }

    @Step("Click on element: {locator}")
    public void click(By locator) {
        ClickActions.click(driver, locator);
    }

    @Step("Click on element")
    public void click(WebElement element) {
        ClickActions.clickOnElement(driver, element);
    }

    @Step("Type '{text}' into element: {locator}")
    public void type(By locator, String text) {
        SendActions.sendKeys(locator, text, driver);
    }

    @Step("Type '{text}' into element")
    public void type(WebElement element, String text) {
        SendActions.sendKeys(element, text, driver);
    }

    @Step("Send key {key} to element: {locator}")
    public void sendKeys(By locator, Keys key) {
        SendActions.sendKeys(locator, key, driver);
    }

    @Step("Get text from element: {locator}")
    public String getText(By locator) {
        return GetActions.getText(driver, locator);
    }

    @Step("Get text from element")
    public String getText(WebElement element) {
        return GetActions.getText(driver, element);
    }

    @Step("Get attribute '{attribute}' from element: {locator}")
    public String getAttribute(By locator, String attribute) {
        return GetActions.getAttribute(driver, locator, attribute);
    }

    @Step("Check if element is displayed: {locator}")
    public boolean isDisplayed(By locator) {
        return CheckActions.isElementDisplayed(driver, locator);
    }

    @Step("Check if element is displayed")
    public boolean isDisplayed(WebElement element) {
        return CheckActions.isElementDisplayed(driver, element);
    }

    @Step("Check if element is enabled: {locator}")
    public boolean isEnabled(By locator) {
        return CheckActions.isEnabled(driver, locator);
    }

    @Step("Check if element is selected: {locator}")
    public boolean isSelected(By locator) {
        return CheckActions.isSelected(driver, locator);
    }

    @Step("Wait for element to be visible: {locator}")
    public WebElement waitForVisible(By locator) {
        return CheckActions.waitForVisible(driver, locator);
    }

    @Step("Wait for element to be visible: {locator} (timeout: {timeout}s)")
    public WebElement waitForVisible(By locator, int timeout) {
        return CheckActions.waitForVisible(driver, locator, timeout);
    }

    @Step("Wait for element to be clickable: {locator}")
    public WebElement waitForClickable(By locator) {
        return ClickActions.waitForClickable(driver, locator);
    }

    @Step("Scroll element into view: {locator}")
    public void scrollIntoView(By locator) {
        log.debug("Scrolling element into view: {}", locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        scrollIntoView(element);
    }

    @Step("Scroll element into view")
    public void scrollIntoView(WebElement element) {
        BrowserActions.scrollIntoView(driver, element);
    }

    @Step("JavaScript click on element: {locator}")
    public void javascriptClick(By locator) {
        log.debug("JavaScript clicking element: {}", locator);
        WebElement element = CheckActions.waitForVisible(driver, locator);
        BrowserActions.javascriptClick(driver, element);
    }

    @Step("JavaScript click on element")
    public void javascriptClick(WebElement element) {
        BrowserActions.javascriptClick(driver, element);
    }

    @Step("Hover over element: {locator}")
    public void hover(By locator) {
        ClickActions.hover(driver, locator);
    }

    @Step("Double-click on element: {locator}")
    public void doubleClick(By locator) {
        ClickActions.doubleClick(driver, locator);
    }

    @Step("Right-click on element: {locator}")
    public void rightClick(By locator) {
        ClickActions.rightClick(driver, locator);
    }

    @Step("Select dropdown option '{visibleText}' from: {locator}")
    public void selectDropdownByText(By locator, String visibleText) {
        SendActions.selectDropdownByText(driver, locator, visibleText);
    }

    @Step("Select dropdown option by value '{value}' from: {locator}")
    public void selectDropdownByValue(By locator, String value) {
        SendActions.selectDropdownByValue(driver, locator, value);
    }

    @Step("Select dropdown option by index {index} from: {locator}")
    public void selectDropdownByIndex(By locator, int index) {
        SendActions.selectDropdownByIndex(driver, locator, index);
    }

    @Step("Get selected dropdown text from: {locator}")
    public String getSelectedDropdownText(By locator) {
        return GetActions.getSelectedDropdownText(driver, locator);
    }

    @Step("Get all dropdown options from: {locator}")
    public List<String> getDropdownOptions(By locator) {
        return GetActions.getDropdownOptions(driver, locator);
    }

    @Step("Get page title")
    public String getPageTitle() {
        return GetActions.getPageTitle(driver);
    }

    @Step("Get current URL")
    public String getCurrentUrl() {
        return GetActions.getCurrentUrl(driver);
    }

    @Step("Refresh page")
    public void refreshPage() {
        BaseActions.refreshPage(driver);
    }

    @Step("Switch to frame: {locator}")
    public void switchToFrame(By locator) {
        BrowserActions.switchToFrame(driver, locator);
    }

    @Step("Switch to default content")
    public void switchToDefaultContent() {
        BrowserActions.switchToDefaultContent(driver);
    }

    @Step("Accept alert")
    public void acceptAlert() {
        BrowserActions.acceptAlert(driver);
    }

    @Step("Dismiss alert")
    public void dismissAlert() {
        BrowserActions.dismissAlert(driver);
    }

    @Step("Get alert text")
    public String getAlertText() {
        return BrowserActions.getAlertText(driver);
    }

    @Step("Find element: {locator}")
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    @Step("Find elements: {locator}")
    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        BaseActions.waitForPageLoaded(driver);
    }
}