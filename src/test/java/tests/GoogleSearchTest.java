package tests;

import base.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.GoogleHomePage;
import pages.GoogleResultsPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test class demonstrating Google Search functionality using Page Object Model.
 * <p>
 * TestNG groups: smoke, search, google
 * Parallel execution safe via ThreadLocal WebDriver in TestBase.
 */
@Epic("Search")
@Feature("Google Search")
@Test(groups = {"google", "search", "smoke"})
@SuppressWarnings("null")
public class GoogleSearchTest extends TestBase {

    @Test(description = "Verify Google homepage loads successfully")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Google Search - Homepage")
    @Description("Verify that the Google homepage loads with the correct title")
    public void testGoogleHomePageLoads() {
        GoogleHomePage homePage = new GoogleHomePage().open();

        String pageTitle = homePage.getHomePageTitle();
        assertThat(pageTitle)
                .as("Google homepage title should contain 'Google'")
                .containsIgnoringCase("Google");
    }

    @Test(description = "Verify search functionality with a keyword")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Google Search - Results")
    @Description("Verify that searching for a keyword returns relevant results")
    public void testSearchWithKeyword() {
        GoogleResultsPage results = new GoogleHomePage()
                .open()
                .search("Selenium WebDriver");

        assertThat(results.getResultCount())
                .as("Search results should not be empty")
                .isGreaterThan(0);

        assertThat(results.isFirstResultDisplayed())
                .as("First search result should be visible")
                .isTrue();

        assertThat(results.getFirstResultTitle())
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
    //     GoogleResultsPage results = new GoogleHomePage()
    //             .open()
    //             .search(searchKeyword);
    //
    //     String stats = results.getResultStats();
    //     assertThat(stats)
    //             .as("Search result stats should be displayed for: " + searchKeyword)
    //             .isNotBlank();
    // }

    // @Test(description = "Verify Google search suggestions appear")
    // @Severity(SeverityLevel.NORMAL)
    // @Story("Google Search - Suggestions")
    // @Description("Verify that search suggestions appear when typing in the search box")
    // public void testSearchSuggestions() {
    //     GoogleHomePage homePage = new GoogleHomePage().open();
    //     List<WebElement> suggestions = homePage.typeQuery("Selenium").getSuggestions();
    //
    //     assertThat(suggestions)
    //             .as("Search suggestions should appear")
    //             .isNotEmpty();
    // }

    // @Test(description = "Verify Google search results page URL")
    // @Severity(SeverityLevel.NORMAL)
    // @Story("Google Search - URL Verification")
    // @Description("Verify that the URL changes after performing a search")
    // public void testSearchResultsUrl() {
    //     GoogleResultsPage results = new GoogleHomePage()
    //             .open()
    //             .search("Test Automation Framework");
    //
    //     String currentUrl = results.getResultsPageUrl();
    //     assertThat(currentUrl)
    //             .as("URL should contain the search query")
    //             .contains("q=Test+Automation+Framework");
    // }
}