package main.pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * Page Object representing a Login page.
 * Demonstrates the Page Object Model pattern with the framework.
 */
@Slf4j
public class LoginPage extends BasePage {

    // Locators
    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".success-message");
    private static final By REMEMBER_ME_CHECKBOX = By.id("rememberMe");
    private static final By FORGOT_PASSWORD_LINK = By.linkText("Forgot Password?");
    private static final By LOGIN_FORM = By.cssSelector(".login-box");
    private static final By USERNAME_LABEL = By.cssSelector("input[placeholder='Username']");
    private static final By PASSWORD_LABEL = By.cssSelector("input[placeholder='Password']");

    /**
     * Navigates to the login page.
     */
    @Step("Open login page")
    public void open() {
        log.info("Opening login page");
        navigateToBaseUrl();
        waitForPageToLoad();
    }

    /**
     * Performs login with the given credentials.
     *
     * @param username the username
     * @param password the password
     */
    @Step("Login with username: {username}")
    public void login(String username, String password) {
        log.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Enters the username into the username field.
     */
    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        log.debug("Entering username");
        type(USERNAME_INPUT, username);
    }

    /**
     * Enters the password into the password field.
     */
    @Step("Enter password")
    public void enterPassword(String password) {
        log.debug("Entering password");
        type(PASSWORD_INPUT, password);
    }

    /**
     * Clicks the login button.
     */
    @Step("Click login button")
    public void clickLoginButton() {
        log.debug("Clicking login button");
        click(LOGIN_BUTTON);
    }

    /**
     * Gets the error message text (if any).
     *
     * @return the error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        log.debug("Getting error message");
        return getText(ERROR_MESSAGE);
    }

    /**
     * Gets the success message text after login.
     *
     * @return the success message text
     */
    @Step("Get success message")
    public String getSuccessMessage() {
        log.debug("Getting success message");
        return getText(SUCCESS_MESSAGE);
    }

    /**
     * Checks if the error message is displayed.
     *
     * @return true if the error message is visible
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    /**
     * Checks if the login form is displayed.
     *
     * @return true if the login form is visible
     */
    @Step("Check if login form is displayed")
    public boolean isLoginFormDisplayed() {
        return isDisplayed(LOGIN_FORM);
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Step("Get login page title")
    public String getLoginPageTitle() {
        return getPageTitle();
    }

    /**
     * Checks if the "Remember Me" checkbox is selected.
     *
     * @return true if selected
     */
    @Step("Check if 'Remember Me' is selected")
    public boolean isRememberMeSelected() {
        return isSelected(REMEMBER_ME_CHECKBOX);
    }

    /**
     * Toggles the "Remember Me" checkbox.
     */
    @Step("Toggle 'Remember Me' checkbox")
    public void toggleRememberMe() {
        log.debug("Toggling Remember Me checkbox");
        click(REMEMBER_ME_CHECKBOX);
    }

    /**
     * Clicks the "Forgot Password" link.
     */
    @Step("Click 'Forgot Password' link")
    public void clickForgotPassword() {
        log.debug("Clicking Forgot Password link");
        click(FORGOT_PASSWORD_LINK);
    }

    /**
     * Clears the username field.
     */
    @Step("Clear username field")
    public void clearUsername() {
        log.debug("Clearing username field");
        type(USERNAME_INPUT, "");
    }

    /**
     * Clears the password field.
     */
    @Step("Clear password field")
    public void clearPassword() {
        log.debug("Clearing password field");
        type(PASSWORD_INPUT, "");
    }

    /**
     * Performs login with empty credentials (boundary test).
     */
    @Step("Attempt login with empty credentials")
    public void loginWithEmptyCredentials() {
        log.info("Attempting login with empty credentials");
        clickLoginButton();
    }

    /**
     * Checks if the username field label is displayed.
     */
    @Step("Check if username label is displayed")
    public boolean isUsernameLabelDisplayed() {
        return isDisplayed(USERNAME_LABEL);
    }

    /**
     * Checks if the password field label is displayed.
     */
    @Step("Check if password label is displayed")
    public boolean isPasswordLabelDisplayed() {
        return isDisplayed(PASSWORD_LABEL);
    }
}
