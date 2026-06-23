package pages.google;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.List;

import static pages.google.GoogleHomePageLocators.*;

/**
 * Page Object representing the Google homepage.
 */
@Slf4j
public class GoogleHomePage extends BasePage {

    /**
     * Opens the Google homepage.
     */
    @Step("Open Google homepage")
    public GoogleHomePage open() {
        log.info("Opening Google homepage");
        navigateTo(GoogleHomePageLocators.GOOGLE_URL);
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
        sendKeys(SEARCH_INPUT, Keys.ENTER);
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