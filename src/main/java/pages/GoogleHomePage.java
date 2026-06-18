package pages;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object representing the Google homepage.
 */
@Slf4j
public class GoogleHomePage extends BasePage {

    private static final String GOOGLE_URL = "https://www.google.com";

    // Locators
    private static final By SEARCH_INPUT = By.name("q");
    private static final By COOKIE_ACCEPT_BUTTON = By.id("L2AGLb");
    private static final By SUGGESTIONS_LIST = By.cssSelector("ul[role='listbox'] li");

    /**
     * Opens the Google homepage.
     */
    @Step("Open Google homepage")
    public GoogleHomePage open() {
        log.info("Opening Google homepage");
        navigateTo(GOOGLE_URL);
        waitForPageToLoad();
        return this;
    }

    /**
     * Performs a search with the given query and returns the results page.
     *
     * @param query the search query
     * @return the GoogleResultsPage
     */
    @Step("Search for: {query}")
    public GoogleResultsPage search(String query) {
        log.info("Searching for: {}", query);
        handleCookieConsent();
        type(SEARCH_INPUT, query);
        findElement(SEARCH_INPUT).sendKeys(Keys.ENTER);
        return new GoogleResultsPage();
    }

    /**
     * Types a partial query into the search box (without pressing Enter).
     */
    @Step("Type query: {query}")
    public GoogleHomePage typeQuery(String query) {
        log.debug("Typing query: {}", query);
        type(SEARCH_INPUT, query);
        return this;
    }

    /**
     * Gets the list of search suggestions.
     */
    @Step("Get search suggestions")
    public List<WebElement> getSuggestions() {
        log.debug("Getting search suggestions");
        return findElements(SUGGESTIONS_LIST);
    }

    /**
     * Gets the current page title.
     */
    @Step("Get Google homepage title")
    public String getHomePageTitle() {
        return getPageTitle();
    }

    private void handleCookieConsent() {
        try {
            if (isDisplayed(COOKIE_ACCEPT_BUTTON)) {
                click(COOKIE_ACCEPT_BUTTON);
            }
        } catch (Exception e) {
            // Cookie consent banner not present – continue
        }
    }
}
