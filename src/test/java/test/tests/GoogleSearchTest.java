package test.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import test.base.TestBase;
import test.data.TestDataProvider;
import utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test class demonstrating Google Search functionality.
 * Shows real-world usage of the framework with a public website.
 * <p>
 * TestNG groups: smoke, search, google
 * Parallel execution safe via ThreadLocal WebDriver in TestBase.
 */
@Epic("Search")
@Feature("Google Search")
@Test(groups = {"google", "search", "smoke"})
public class GoogleSearchTest extends TestBase {

    private static final String GOOGLE_URL = "https://www.google.com";
    private static final By SEARCH_INPUT = By.name("q");
    private static final By RESULT_STATS = By.id("result-stats");
    private static final By FIRST_RESULT_TITLE = By.cssSelector("h3");
    private static final By SEARCH_RESULT_LINKS = By.cssSelector("div#search a[href^='http']");
    private static final By SUGGESTIONS_LIST = By.cssSelector("ul[role='listbox'] li");

    @Test(description = "Verify Google homepage loads successfully")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Google Search - Homepage")
    @Description("Verify that the Google homepage loads with the correct title")
    public void testGoogleHomePageLoads() {
        getDriver().get(GOOGLE_URL);
        WaitUtils.waitForPageLoad(getDriver());

        String pageTitle = getDriver().getTitle();
        assertThat(pageTitle)
                .as("Google homepage title should contain 'Google'")
                .containsIgnoringCase("Google");
    }

    @Test(description = "Verify search functionality with a keyword")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Google Search - Results")
    @Description("Verify that searching for a keyword returns relevant results")
    public void testSearchWithKeyword() {
        getDriver().get(GOOGLE_URL);
        WaitUtils.waitForPageLoad(getDriver());
        handleCookieConsent();

        performSearch("Selenium WebDriver");

        WaitUtils.waitForElementsCount(getDriver(), SEARCH_RESULT_LINKS, 1);

        List<WebElement> results = getDriver().findElements(SEARCH_RESULT_LINKS);
        assertThat(results)
                .as("Search results should not be empty")
                .isNotEmpty();

        WebElement firstResult = getDriver().findElement(FIRST_RESULT_TITLE);
        assertThat(firstResult.isDisplayed())
                .as("First search result should be visible")
                .isTrue();
        assertThat(firstResult.getText())
                .as("First result text should not be empty")
                .isNotBlank();
    }

    // @Test(description = "Verify multiple search queries",
    //         dataProvider = "searchKeywords",
    //         dataProviderClass = TestDataProvider.class)
    // @Severity(SeverityLevel.NORMAL)
    // @Story("Google Search - Multiple Queries")
    // @Description("Verify that different search queries return relevant results")
    // public void testMultipleSearchQueries(String searchKeyword) {
    //     getDriver().get(GOOGLE_URL);
    //     WaitUtils.waitForPageLoad(getDriver());
    //     handleCookieConsent();

    //     performSearch(searchKeyword);

    //     WaitUtils.waitForVisible(getDriver(), RESULT_STATS);

    //     String stats = getDriver().findElement(RESULT_STATS).getText();
    //     assertThat(stats)
    //             .as("Search result stats should be displayed for: " + searchKeyword)
    //             .isNotBlank();
    // }

    // @Test(description = "Verify Google search suggestions appear")
    // @Severity(SeverityLevel.NORMAL)
    // @Story("Google Search - Suggestions")
    // @Description("Verify that search suggestions appear when typing in the search box")
    // public void testSearchSuggestions() {
    //     getDriver().get(GOOGLE_URL);
    //     WaitUtils.waitForPageLoad(getDriver());
    //     handleCookieConsent();

    //     WebElement searchBox = getDriver().findElement(SEARCH_INPUT);
    //     searchBox.sendKeys("Selenium");

    //     List<WebElement> suggestionList = WaitUtils.waitForElementsCount(
    //             getDriver(), SUGGESTIONS_LIST, 1);
    //     assertThat(suggestionList)
    //             .as("Search suggestions should appear")
    //             .isNotEmpty();
    // }

    // @Test(description = "Verify Google search results page URL")
    // @Severity(SeverityLevel.NORMAL)
    // @Story("Google Search - URL Verification")
    // @Description("Verify that the URL changes after performing a search")
    // public void testSearchResultsUrl() {
    //     getDriver().get(GOOGLE_URL);
    //     WaitUtils.waitForPageLoad(getDriver());
    //     handleCookieConsent();

    //     performSearch("Test Automation Framework");

    //     String currentUrl = getDriver().getCurrentUrl();
    //     assertThat(currentUrl)
    //             .as("URL should contain the search query")
    //             .contains("q=Test+Automation+Framework");
    // }

    // ========================================================================
    // Helper methods
    // ========================================================================

    private void handleCookieConsent() {
        try {
            WaitUtils.waitForCondition(getDriver(), d -> {
                try {
                    List<WebElement> buttons = d.findElements(By.xpath(
                            "//button//span[text()='Accept all']" +
                            " | //button//span[text()='Zaakceptuj wszystko']" +
                            " | //div[text()='Accept all']" +
                            " | //button[contains(., 'Accept all')]"));
                    if (!buttons.isEmpty()) {
                        buttons.get(0).click();
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }, 3);
        } catch (Exception e) {
            // Cookie consent not present, continue
        }
    }

    private void performSearch(String query) {
        WebElement searchBox = WaitUtils.waitForClickable(getDriver(), SEARCH_INPUT);
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.ENTER);
    }
}
