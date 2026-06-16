package org.example.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.driver.DriverManager;
import org.example.pages.BasePage;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory for creating Page Object instances.
 * Automatically injects the current thread's WebDriver into page objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageFactory {

    /**
     * Creates a new instance of the specified page class.
     * The page object will automatically use the current thread's WebDriver.
     *
     * @param pageClass the page class to instantiate
     * @param <T>       the page type
     * @return a new page instance
     */
    public static <T extends BasePage> T createPage(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create page: " + pageClass.getSimpleName(), e);
        }
    }

    /**
     * Creates a new instance of the specified page class using
     * Selenium's PageFactory for @FindBy annotation support.
     *
     * @param pageClass the page class to instantiate
     * @param <T>       the page type
     * @return a new page instance with initialized WebElements
     */
    public static <T extends BasePage> T createPageWithElements(Class<T> pageClass) {
        T page = createPage(pageClass);
        org.openqa.selenium.support.PageFactory.initElements(
                DriverManager.getDriver(), page);
        return page;
    }
}
