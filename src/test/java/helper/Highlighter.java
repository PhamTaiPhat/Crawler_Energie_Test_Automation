package helper;

import logger.LoggerMessages;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.BehaviourConfig;

import java.time.Duration;
import java.util.List;


public class Highlighter {

    private static final Logger logger = LoggerFactory.getLogger(Highlighter.class);
    // Constants moved from BehaviourConfig to avoid circular dependency
    private static final String COLOR_PROCESSING = "orange";
    protected WebDriver driver;
    protected WebDriverWait wait;

    public Highlighter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public Highlighter(UiContext context) {
        this.driver = context.driver();
        this.wait = context.waiter();
    }

    private void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});", element);
            Thread.sleep(BehaviourConfig.BLINK_TIME); // Small delay for smooth scrolling
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("SCROLL"), e.getMessage());
        }
    }

    private void outlineElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String originalStyle = (String) js.executeScript("return arguments[0].style.border;", element);

            for (int i = 0; i < BehaviourConfig.BLINK_COUNT; i++) {
                js.executeScript("arguments[0].style.border = '5px solid " + Highlighter.COLOR_PROCESSING + "';", element);
                Thread.sleep(BehaviourConfig.BLINK_TIME);
                js.executeScript("arguments[0].style.border = arguments[1];", element, originalStyle);
                Thread.sleep(BehaviourConfig.BLINK_TIME);
            }

        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("OUTLINE"), e.getMessage());
        }
    }

    /**
     * Ensure element is visible and scrollable before interaction
     */
    public WebElement prepareElementForInteraction(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            highlight(element);
            return element;
        }catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Prepare Interaction"), locator);
            return null;
        }

    }

    public WebElement prepareReadOnlyElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            highlight(element);
            logger.info(LoggerMessages.subsectionSuccess("Prepare read only"));
            return element;
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Prepare read only"), locator);
            return null;
        }
    }

    public WebElement prepareChildElement(WebElement parent, By childLocator) {
        try {
            WebElement result =  wait.until(driver -> {
                try {
                    WebElement child = parent.findElement(childLocator);
                    return child.isDisplayed() ? child : null;
                } catch (Exception ignored) {
                    return null;
                }
            });
            highlight(result);
            logger.info(LoggerMessages.subsectionSuccess("Prepare child"));
            return result;
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Prepare child"), childLocator);
            return null;
        }
    }

    public List<WebElement> prepareChildren(WebElement parent, By childLocator) {
        try {
            List<WebElement> result = wait.until(d -> {
            List<WebElement> children = parent.findElements(childLocator);
            return children.isEmpty() ? null : children;
        });
            logger.info(LoggerMessages.subsectionSuccess("Prepare children"));
            return result;
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Prepare children"), childLocator);
            return null;
        }
    }


    public void highlight(WebElement element) {
        if (BehaviourConfig.HEADLESS) {
            return;
        }
        try {
            scrollToElement(element);
            outlineElement(element);
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("HIGHLIGHT"), element);
        }

    }
}
