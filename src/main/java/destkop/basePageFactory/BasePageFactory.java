package destkop.basePageFactory;

import destkop.actions.BaseActions;
import destkop.actions.BrowserActions;
import destkop.actions.CheckActions;
import destkop.actions.ClickActions;
import destkop.actions.GetActions;
import destkop.actions.SendActions;
import destkop.driver.BaseDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePageFactory {

    public CheckActions check;
    public ClickActions click;
    public SendActions send;
    public BrowserActions browser;
    public GetActions get;
    public BaseActions base;

    protected BasePageFactory() {
        initActions();
        initElements();
    }

    private void initActions() {
        this.check = new CheckActions();
        this.click = new ClickActions();
        this.send = new SendActions();
        this.browser = new BrowserActions();
        this.get = new GetActions();
        this.base = new BaseActions();
    }

    private void initElements() {
        String framework = System.getProperty("framework", "selenium");
        switch (framework) {
            case "selenium" -> PageFactory.initElements(BaseDriver.getWebDriver(), this);
            case "api" -> { /* API tests don't need PageFactory */ }
            default -> PageFactory.initElements(BaseDriver.getWebDriver(), this);
        }
    }
}