package actions;

import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.wait.WaitUtils;

@Slf4j
@NoArgsConstructor()
@SuppressWarnings("null")
public final class BrowserActions {

    public static void navigateTo(WebDriver driver, String url) {
        log.info("Navigating to: {}", url);
        Allure.step("Navigate to " + url, () -> driver.get(url));
    }

    public static void switchToFrame(WebDriver driver, By locator) {
        log.debug("Switching to frame: {}", locator);
        Allure.step("Switch to frame " + locator, () -> {
            WebElement frame = WaitUtils.getDefaultWait(driver)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            driver.switchTo().frame(frame);
        });
    }

    public static void switchToDefaultContent(WebDriver driver) {
        log.debug("Switching to default content");
        Allure.step("Switch back to default content", () -> driver.switchTo().defaultContent());
    }

    public static void acceptAlert(WebDriver driver) {
        log.debug("Accepting alert");
        Allure.step("Accept alert", () -> alert(driver).accept());
    }

    public static void dismissAlert(WebDriver driver) {
        log.debug("Dismissing alert");
        Allure.step("Dismiss alert", () -> alert(driver).dismiss());
    }

    public static String getAlertText(WebDriver driver) {
        return alert(driver).getText();
    }

    private static Alert alert(WebDriver driver) {
        return WaitUtils.getDefaultWait(driver).until(ExpectedConditions.alertIsPresent());
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        Allure.step("Scroll element into view", () ->
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element));
    }

    public static void javascriptClick(WebDriver driver, WebElement element) {
        Allure.step("Click with JavaScript", () ->
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element));
    }
}