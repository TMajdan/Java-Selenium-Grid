package tests.desktop;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import destkop.pages.google.GoogleHomePage;
import destkop.pages.google.GoogleResultsPage;

/**
 * Example test class demonstrating fluent Page Object style.
 * TestNG groups: smoke, search, google
 * Parallel execution safe via ThreadLocal WebDriver in TestBase.
 */
public class GoogleSearchTest extends TestBase {

    @Test(description = "Verify Google homepage loads successfully")
    public void testGoogleHomePageLoads() {
        new GoogleHomePage().open();
    }

    @Test(description = "Verify search functionality with a keyword")
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