//package org.example.test.tests;
//
//import io.qameta.allure.Description;
//import io.qameta.allure.Epic;
//import io.qameta.allure.Feature;
//import io.qameta.allure.Severity;
//import io.qameta.allure.SeverityLevel;
//import io.qameta.allure.Story;
//import org.example.pages.LoginPage;
//import org.example.test.base.TestBase;
//import org.example.test.data.TestDataProvider;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Test class for Login functionality.
// * Demonstrates Page Object Model, DataProvider, Allure reporting,
// * AssertJ assertions, and TestNG conventions.
// * <p>
// * TestNG groups: smoke, login, authentication
// * Parallel execution safe via ThreadLocal WebDriver in TestBase.
// */
//@Epic("Authentication")
//@Feature("Login")
//@Test(groups = {"login", "authentication", "smoke"})
//public class LoginTest extends TestBase {
//
//    private LoginPage loginPage;
//
//    @BeforeMethod(alwaysRun = true)
//    public void initPages() {
//        loginPage = new LoginPage();
//        loginPage.open();
//    }
//
//    @Test(description = "Verify login page elements are displayed")
//    @Severity(SeverityLevel.CRITICAL)
//    @Story("Login UI Verification")
//    @Description("Verify that all elements on the login page are properly displayed")
//    public void testLoginPageElements() {
//        assertThat(loginPage.isLoginFormDisplayed())
//                .as("Login form should be displayed")
//                .isTrue();
//
//        assertThat(loginPage.isUsernameLabelDisplayed())
//                .as("Username label should be displayed")
//                .isTrue();
//
//        assertThat(loginPage.isPasswordLabelDisplayed())
//                .as("Password label should be displayed")
//                .isTrue();
//
//        assertThat(loginPage.getLoginPageTitle())
//                .as("Page title should not be empty")
//                .isNotEmpty();
//    }
//
//    @Test(description = "Verify successful login with valid credentials",
//            dataProvider = "validLoginData",
//            dataProviderClass = TestDataProvider.class)
//    @Severity(SeverityLevel.BLOCKER)
//    @Story("Successful Login")
//    @Description("Verify that users can login successfully with valid credentials")
//    public void testSuccessfulLogin(String username, String password) {
//        loginPage.login(username, password);
//
//        assertThat(loginPage.isLoginFormDisplayed())
//                .as("Login form should no longer be displayed after successful login")
//                .isFalse();
//    }
//
//    @Test(description = "Verify error messages for invalid login attempts",
//            dataProvider = "invalidLoginData",
//            dataProviderClass = TestDataProvider.class)
//    @Severity(SeverityLevel.CRITICAL)
//    @Story("Login Validation")
//    @Description("Verify that appropriate error messages are shown for invalid login attempts")
//    public void testLoginWithInvalidCredentials(String username, String password,
//                                                 String expectedErrorMessage) {
//        loginPage.login(username, password);
//
//        assertThat(loginPage.isErrorMessageDisplayed())
//                .as("Error message should be displayed for invalid login")
//                .isTrue();
//
//        assertThat(loginPage.getErrorMessage())
//                .as("Error message should contain expected text")
//                .contains(expectedErrorMessage);
//    }
//
//    @Test(description = "Verify login with empty credentials")
//    @Severity(SeverityLevel.NORMAL)
//    @Story("Login Validation")
//    @Description("Verify that login fails when both username and password are empty")
//    public void testLoginWithEmptyCredentials() {
//        loginPage.loginWithEmptyCredentials();
//
//        assertThat(loginPage.isErrorMessageDisplayed())
//                .as("Error message should be displayed for empty credentials")
//                .isTrue();
//    }
//
//    @Test(description = "Verify Remember Me checkbox functionality",
//            groups = {"regression"})
//    @Severity(SeverityLevel.NORMAL)
//    @Story("Login UI Features")
//    @Description("Verify that the Remember Me checkbox can be toggled")
//    public void testRememberMeCheckbox() {
//        boolean initialState = loginPage.isRememberMeSelected();
//        loginPage.toggleRememberMe();
//        assertThat(loginPage.isRememberMeSelected())
//                .as("Remember Me checkbox state should change after clicking")
//                .isNotEqualTo(initialState);
//    }
//
//    @Test(description = "Verify Forgot Password link navigation",
//            groups = {"regression"})
//    @Severity(SeverityLevel.NORMAL)
//    @Story("Password Recovery")
//    @Description("Verify that clicking Forgot Password link navigates to the correct page")
//    public void testForgotPasswordLink() {
//        loginPage.clickForgotPassword();
//
//        String currentUrl = loginPage.getCurrentUrl();
//        assertThat(currentUrl)
//                .as("URL should contain 'forgot-password' after clicking the link")
//                .containsIgnoringCase("forgot-password");
//    }
//
//    @Test(description = "Flaky test example with retry analyzer",
//            groups = {"flaky"})
//    @Severity(SeverityLevel.TRIVIAL)
//    @Story("Retry Mechanism")
//    @Description("Demonstrates the retry mechanism for flaky tests (retries up to configured count)")
//    public void testFlakyTestExample() {
//        loginPage.isLoginFormDisplayed();
//        assertThat(loginPage.getLoginPageTitle())
//                .as("Page title should not be null")
//                .isNotNull();
//    }
//}
