package destkop.driver;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.ByteArrayInputStream;

@Slf4j
public class DriverListener implements WebDriverListener {

    public void beforeClick(Object target, WebElement element) {
        log.debug("[{}] Clicking on: {}", threadName(), element.getTagName());
    }

    public void afterClick(Object target, WebElement element) {
        log.debug("[{}] Clicked on: {}", threadName(), element.getTagName());
    }

    public void beforeFindElement(Object target, By by) {
        log.debug("[{}] Finding element: {}", threadName(), by);
    }

    public void afterFindElement(Object target, By by, WebElement element) {
        log.debug("[{}] Found element: {}", threadName(), by);
    }

    public void error(Object target, Throwable throwable) {
        log.warn("[{}] WebDriver error: {}", threadName(), throwable.getMessage());
        Allure.attachment("WebDriver error",
                new ByteArrayInputStream(throwable.getMessage().getBytes()));
    }

    public void beforeNavigateTo(Object target, String url, WebDriver driver) {
        log.debug("[{}] Navigating to: {}", threadName(), url);
    }

    public void afterNavigateTo(Object target, String url, WebDriver driver) {
        log.debug("[{}] Navigated to: {}", threadName(), url);
    }

    private static String threadName() {
        return Thread.currentThread().getName();
    }
}