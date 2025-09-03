package helper;

import model.UiContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.BehaviourConfig;
import logger.LoggerMessages;

import java.util.Random;

public class UserActions {
    private static final Logger logger = LoggerFactory.getLogger(UserActions.class);
    private static final Random random = new Random();
    private static final int MAX_CLICK_RETRIES = BehaviourConfig.CLICK_MAX_RETRIES;
    private final Highlighter highlighter;
    protected WebDriver driver;
    protected WebDriverWait wait;

    public UserActions(UiContext context, Highlighter highlighter) {
        this.driver = context.driver();
        this.wait = context.waiter();
        this.highlighter = highlighter;
    }

    /**
     * Creates a delay in a range of milliseconds.
     */
    public static void randomDelay(int minMs, int maxMs) {
        int delay = random.nextInt(maxMs - minMs + 1) + minMs;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Generic interaction methods FILL, CLICK, SELECT
     */

    // FILL
    public void fillWebElement(WebElement element, String input) {

        if (input == null ||  input.isEmpty()) {
            return;
        }

        try {
            element.clear();

            // Type character by character with delays for human-like behavior
            for (int i = 0; i < input.length(); i++) {
                char character = input.charAt(i);
                element.sendKeys(String.valueOf(character));
                UserActions.randomDelay(BehaviourConfig.MIN_TYPING_DELAY_MS, BehaviourConfig.MAX_TYPING_DELAY_MS);
            }
            logger.info(LoggerMessages.actionSuccess(element, input));
        } catch (Exception e) {
            String text = LoggerMessages.actionFailure(element, input);
            logger.debug(LoggerMessages.actionCrash(element, Thread.currentThread().getStackTrace()[1].getMethodName()));
            throw new RuntimeException(text);
        }
    }

    // JavaScript CLICK - by WebElement with retries
    public void clickWebElement(WebElement element) {
        for (int attempt = 1; attempt <= MAX_CLICK_RETRIES; attempt++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

                Boolean toggled = getAriaState(element);
                if (toggled == null || toggled) {
                    logger.info(LoggerMessages.actionSuccess(element, ""));
                    return;
                }
            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                // Cannot re-find element here; caller should re-provide a fresh reference if needed
            } catch (Exception jsException) {
                if (attempt == MAX_CLICK_RETRIES) {
                    String text = LoggerMessages.actionFailure(element, "");
                    logger.debug(LoggerMessages.actionCrash(element, Thread.currentThread().getStackTrace()[1].getMethodName()));
                    throw new RuntimeException(text);
                }
            }
            UserActions.randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);
        }
    }

    // Removed By-based click; callers should pass WebElement

    private Boolean getAriaState(WebElement element) {
        try {
            String value = element.getAttribute("aria-checked");
            if (value == null) {
                value = element.getAttribute("aria-expanded");
            }
            if (value == null) {
                value = element.getAttribute("aria-selected");
            }
            if (value == null) {
                return null;
            }
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            logger.debug(LoggerMessages.actionCrash(element, Thread.currentThread().getStackTrace()[1].getMethodName()));
            return null;
        }
    }

    // SELECT
    public void selectWebElement(WebElement element, String input) {
        try {

            randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);
            element.click();
            randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);
            element.sendKeys(input);
            randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);
            element.click();
            randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);

            logger.info(LoggerMessages.actionSuccess(element, input));
        } catch (Exception e) {
            String text = LoggerMessages.actionFailure(element, input);
            logger.debug(LoggerMessages.actionCrash(element, Thread.currentThread().getStackTrace()[1].getMethodName()));
            throw new RuntimeException(text);
        }
    }
}


