package pages.actions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.wait.WaitUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrowserActions {

    public static void navigateTo(WebDriver driver, String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public static void switchToFrame(WebDriver driver, By locator) {
        log.debug("Switching to frame: {}", locator);
        WebElement frame = WaitUtils.getDefaultWait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.switchTo().frame(frame);
    }

    public static void switchToDefaultContent(WebDriver driver) {
        log.debug("Switching to default content");
        driver.switchTo().defaultContent();
    }

    public static void acceptAlert(WebDriver driver) {
        log.debug("Accepting alert");
        alert(driver).accept();
    }

    public static void dismissAlert(WebDriver driver) {
        log.debug("Dismissing alert");
        alert(driver).dismiss();
    }

    public static String getAlertText(WebDriver driver) {
        return alert(driver).getText();
    }

    private static Alert alert(WebDriver driver) {
        return WaitUtils.getDefaultWait(driver).until(ExpectedConditions.alertIsPresent());
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void javascriptClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}