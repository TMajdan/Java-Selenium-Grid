package destkop.pages.google;

import org.openqa.selenium.By;

import destkop.basePageFactory.BasePageFactory;

/**
 * Locator definitions for the Google search results page.
 * <p>
 * Contains all By selectors used by GoogleResultsPage.
 * Extends BasePageFactory to inherit action delegates and desktop test initialization.
 */
public class GoogleResultsPageLocators extends BasePageFactory {

    public static final By RESULT_STATS = By.id("result-stats");
    public static final By FIRST_RESULT_TITLE = By.cssSelector("h3");
    public static final By SEARCH_RESULT_LINKS = By.cssSelector("div#search a[href^='http']");
}