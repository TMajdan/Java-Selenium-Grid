package test.data;

import models.User;
import utils.TestDataFactory;
import org.testng.annotations.DataProvider;

import java.util.stream.Stream;

/**
 * Centralized TestNG DataProvider for test data.
 * Provides reusable test data sets across all test classes.
 */
public class TestDataProvider {

    /**
     * Provides valid login credentials.
     */
    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"}
        };
    }

    /**
     * Provides invalid login credentials.
     */
    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {"", "", "Username is required"},
                {"standard_user", "", "Password is required"},
                {"", "secret_sauce", "Username is required"},
                {"invalid_user", "wrong_pass", "Username and password do not match"},
                {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out"}
        };
    }

    /**
     * Provides random user data for registration tests.
     */
    @DataProvider(name = "randomUserData")
    public static Object[][] randomUserData() {
        return Stream.generate(() -> {
            User user = TestDataFactory.createRandomUser();
            return new Object[]{user};
        }).limit(3).toArray(Object[][]::new);
    }

    /**
     * Provides search keywords for search tests.
     */
    @DataProvider(name = "searchKeywords")
    public static Object[][] searchKeywords() {
        return new Object[][]{
                {"Selenium WebDriver"},
                {"Test Automation Framework"},
                {"Java Programming"},
                {"Page Object Model"}
        };
    }

    /**
     * Provides boundary test data for input fields.
     */
    @DataProvider(name = "boundaryTestData")
    public static Object[][] boundaryTestData() {
        return new Object[][]{
                {0, "Empty input"},
                {1, "Single character"},
                {100, "Normal input"},
                {255, "Maximum length"},
                {256, "Exceeding maximum length"}
        };
    }

    /**
     * Provides browser configurations for cross-browser tests.
     */
    @DataProvider(name = "browserConfigurations")
    public static Object[][] browserConfigurations() {
        return new Object[][]{
                {"CHROME"},
                {"CHROME"}
        };
    }

    /**
     * Provides user data for CRUD operations testing.
     */
    @DataProvider(name = "userCrudData")
    public static Object[][] userCrudData() {
        return new Object[][]{
                {TestDataFactory.createUserWithCredentials("user_create", "Pass123!"), "CREATE"},
                {TestDataFactory.createUserWithCredentials("user_update", "Pass123!"), "UPDATE"},
                {TestDataFactory.createUserWithCredentials("user_delete", "Pass123!"), "DELETE"}
        };
    }
}
