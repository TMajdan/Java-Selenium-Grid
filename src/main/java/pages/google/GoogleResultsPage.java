package pages.google;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.List;

import static pages.google.GoogleResultsPageLocators.*;

/**
 * Page Object representing the Google search results page.
 */
@Slf4j
public class GoogleResultsPage extends BasePage {

    /**
     * Gets the number of search result links.
     */
    @Step("Get search result count")
    public int getResultCount() {
        log.debug("Getting search result count");
        waitForVisible(SEARCH_RESULT_LINKS);
        return findElements(SEARCH_RESULT_LINKS).size();
    }

    /**
     * Gets all search result links as WebElements.
     */
    @Step("Get all search result links")
    public List<WebElement> getResultLinks() {
        log.debug("Getting search result links");
        waitForVisible(SEARCH_RESULT_LINKS);
        return findElements(SEARCH_RESULT_LINKS);
    }

    /**
     * Gets the title text of the first search result.
     */
    @Step("Get first result title")
    public String getFirstResultTitle() {
        log.debug("Getting first result title");
        return getText(FIRST_RESULT_TITLE);
    }

    /**
     * Checks if the first result is displayed.
     */
    @Step("Check if first result is displayed")
    public boolean isFirstResultDisplayed() {
        return isDisplayed(FIRST_RESULT_TITLE);
    }

    /**
     * Gets the result statistics text (e.g. "About 123,000 results").
     */
    @Step("Get result statistics")
    public String getResultStats() {
        log.debug("Getting result statistics");
        return getText(RESULT_STATS);
    }

    /**
     * Gets the current page URL.
     */
    @Step("Get results page URL")
    public String getResultsPageUrl() {
        return getCurrentUrl();
    }

    /**
     * Gets the results page title.
     */
    @Step("Get results page title")
    public String getResultsPageTitle() {
        return getPageTitle();
    }
}