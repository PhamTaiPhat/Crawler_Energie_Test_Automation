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
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected UiContext uiContext;

    public Form(UiContext context) {
        this.uiContext = context;
        this.userActions = new  UserActions(context);
    }

    private static final By ENGLISH = By.xpath("//button[text()='EN']");
    private static final String URL_OFFER = "https://dev.crawlerenergie.nl/platform";
    private static final By NEXT_BUTTON = By.id("next-button");

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

    protected void navigateToPlatformPage() {
        uiContext.driver().get(URL_OFFER);
        uiContext.waiter().until(ExpectedConditions.presenceOfElementLocated(Locators.BODY_TAG));
    }

    protected void clickEnglishButton() {
        click(ENGLISH);
    }

    protected void clickNextButton() {
        click(NEXT_BUTTON);
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
