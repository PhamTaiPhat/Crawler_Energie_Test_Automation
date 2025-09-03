package pages.offer;

import config.Locators;
import helper.Highlighter;
import helper.UserActions;
import logger.LoggerMessages;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Form {
    protected final UserActions userActions;
    protected final Highlighter highlighter;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public Form(UiContext context, UserActions userActions) {
        this.driver = context.driver();
        this.wait = context.waiter();
        this.userActions = userActions;
        this.highlighter = new Highlighter(context);
    }

    protected void click(By locator) {
        WebElement element = highlighter.prepareElementForInteraction(locator);
        userActions.clickWebElement(element);
    }

    protected void fill(By locator, String input) {
        WebElement element = highlighter.prepareElementForInteraction(locator);
        userActions.fillWebElement(element, input);
    }

    protected void select(By locator, String input) {
        WebElement element = highlighter.prepareElementForInteraction(locator);
        userActions.selectWebElement(element, input);
    }

    protected void navigateToPlatformPage() {
        driver.get(Locators.URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(Locators.BODY_TAG));
    }

    protected void clickEnglishButton() {
        click(Locators.ENGLISH);
    }

    protected void clickNextButton() {
        click(Locators.NEXT_BUTTON);
    }

    protected void successLog() {
        logger.info(LoggerMessages.sectionSuccess(getClass().getName()));
    }

    protected void crashLog() {
        logger.debug(LoggerMessages.sectionCrash(getClass().getName()));
    }

    protected void failLog() {
        logger.info(LoggerMessages.sectionFailure(getClass().getName()));
    }
}
