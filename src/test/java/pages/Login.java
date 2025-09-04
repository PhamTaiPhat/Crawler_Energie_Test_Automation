package pages;

import config.BehaviourConfig;
import config.Locators;
import helper.UserActions;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.offer.Form;

public class Login extends Form {

    public Login(UiContext context) {super(context);}

    private static final String  USERNAME = "00001";
    private static final String PASSWORD = "admin";
    private static final String URL_LOGIN = "https://dev.crawlerenergie.nl/login";
    private static final By USERNAME_LOC = By.id("username");
    private static final By PASSWORD_LOC = By.id("password");
    private static final By LOGIN_BUTTON = By.xpath("//button[text()='Login']");

    protected void navigateToLoginPage() {
        uiContext.driver().get(URL_LOGIN);
        uiContext.waiter().until(ExpectedConditions.presenceOfElementLocated(Locators.BODY_TAG));
    }

    public void login(){

        try {
            navigateToLoginPage();
            Thread.sleep(BehaviourConfig.IDLE_TIME);
            fill(USERNAME_LOC, USERNAME);
            Thread.sleep(BehaviourConfig.IDLE_TIME);
            fill(PASSWORD_LOC, PASSWORD);
            click(LOGIN_BUTTON);
        } catch (Exception e) {
            throw new RuntimeException("[💥] Crashed during set up - RETRY TEST.", e);
        }
    }


}
