package tests;

import base.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.google.GoogleHomePage;
import pages.google.GoogleResultsPage;

/**
 * Example test class demonstrating fluent Page Object style.
 * TestNG groups: smoke, search, google
 * Parallel execution safe via ThreadLocal WebDriver in TestBase.
 */
@Epic("Search")
@Feature("Google Search")
@Test(groups = {"google", "search", "smoke"})
public class GoogleSearchTest extends TestBase {

    @Test(description = "Verify Google homepage loads successfully")
    @Story("Google Search - Homepage")
    @Description("Verify that the Google homepage loads with the correct title")
    public void testGoogleHomePageLoads() {
        new GoogleHomePage().open();
    }

    @Test(description = "Verify search functionality with a keyword")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Google Search - Results")
    @Description("Verify that searching for a keyword returns relevant results")
    public void testSearchWithKeyword() {
        GoogleResultsPage results = new GoogleHomePage()
                .open()
                .search("Selenium WebDriver");

        Assert.assertTrue(results.getResultCount() > 0,
                "Search results should not be empty");
        Assert.assertTrue(results.isFirstResultDisplayed(),
                "First search result should be visible");
        Assert.assertNotNull(results.getFirstResultTitle(),
                "First result text should not be null");
        Assert.assertFalse(results.getFirstResultTitle().isBlank(),
                "First result text should not be blank");
    }
}