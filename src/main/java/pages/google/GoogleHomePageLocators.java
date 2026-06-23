package pages.google;

import org.openqa.selenium.By;

import basePageFactory.BasePageFactory;

/**
 * Locator definitions for the Google homepage.
 * <p>
 * Contains all By selectors used by GoogleHomePage.
 * Extends BasePageFactory to inherit action delegates and desktop test initialization.
 */
public class GoogleHomePageLocators extends BasePageFactory {

    public static final String GOOGLE_URL = "https://www.google.com";

    public static final By SEARCH_INPUT = By.name("q");
    public static final By COOKIE_ACCEPT_BUTTON = By.id("L2AGLb");
    public static final By SUGGESTIONS_LIST = By.cssSelector("ul[role='listbox'] li");
}