package pages.admin;

import helper.UserActions;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminPage {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    UiContext uiContext;
    protected final UserActions userActions;
    public AdminPage(UiContext context) {
        this.uiContext = context;
        this.userActions = new UserActions(context);
    }

    private final String  CUSTOMER_NAME = "Customer Name";
    private final String  ID = "ID";
    private final String  COMPANY_NAME = "Company Name";
    private final String  STATUS = "Status";

    private By formLocator(String placeholder){return By.cssSelector("input[placeholder='" + placeholder + "']");}

    protected void click(By locator) {
        WebElement element = userActions.prepareElementForInteraction(locator);
        userActions.clickWebElement(element);
    }

    protected void fill(By locator, String input) {
        WebElement element = userActions.prepareElementForInteraction(locator);
        userActions.fillWebElement(element, input);
    }

    protected void select(By locator, String input) {
        WebElement element = userActions.prepareElementForInteraction(locator);
        userActions.selectWebElement(element, input);
    }

}
