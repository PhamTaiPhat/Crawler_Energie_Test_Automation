package pages.offer;

import config.BehaviourConfig;
import config.VerificationConfig;
import helper.Parser;
import helper.UserActions;
import logger.LoggerMessages;
import model.Customer;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FourthForm extends Form {
    private final SecondForm secondForm;

    public FourthForm(UiContext context, UserActions userActions) {
        super(context, userActions);
        this.secondForm = new SecondForm(context, userActions);
    }

    // ---------------------------
    // Address verification
    // ---------------------------


    private WebElement findDescendant(WebElement root, String tag, String attribute, String section) {
        // Scope to root with .// and trim text for robustness
        By locator = By.xpath(".//" + tag + "[normalize-space(text())='" + attribute + "']");
        List<WebElement> elements = highlighter.prepareChildren(root, locator);
        if (VerificationConfig.CONTRACT_INFO.equals(section)) {
            return elements.get(0).findElement(By.xpath(".."));
        }
        return elements.get(1).findElement(By.xpath(".."));
    }

    private boolean verifyAddress(WebElement root, String postalCode, String houseNumber) {
        String attribute = VerificationConfig.BILLING_ADDRESS;

        // Find & highlight the address node within the given root
        WebElement descendant = findDescendant(root, "span", attribute, VerificationConfig.CONTRACT_INFO);
        String address = descendant.getText();

        String[] parsedAddress = Parser.parseElement(address);
        if (parsedAddress.length < 2) {
            logger.debug(LoggerMessages.subsectionFailure("ADDRESS PARSING"), address);
            return false;
        }

        if (!Objects.equals(attribute, parsedAddress[0])) {
            logger.info(LoggerMessages.verificationFailure("Label", attribute, parsedAddress[0]));
            return false;
        }

        String[] extractedCodes = Parser.extractAddressCodes(parsedAddress[1]);

        boolean ok = true;
        if (!Objects.equals(extractedCodes[0], postalCode)) {
            logger.info(LoggerMessages.verificationFailure("Postal Code", postalCode, extractedCodes[0]));
            ok = false;
        } else {
            logger.info(LoggerMessages.verificationSuccess("Postal Code", postalCode, extractedCodes[0]));
        }

        if (!Objects.equals(extractedCodes[1], houseNumber)) {
            logger.info(LoggerMessages.verificationFailure("House Number", houseNumber, extractedCodes[1]));
            ok = false;
        } else {
            logger.info(LoggerMessages.verificationSuccess("House Number", houseNumber, extractedCodes[1]));
        }

        return ok;
    }

    private boolean checkElement(WebElement root, String expected, String attribute, String section) {
        WebElement descendant = findDescendant(root, "span", attribute, section);

        String descendantText = descendant.getText(); // headless-safe
        String[] parsedText = Parser.parseElement(descendantText);

        if (!Objects.equals(attribute, parsedText[0])) {
            logger.info(LoggerMessages.verificationFailure("Label", attribute, parsedText[0]));
            return false;
        }
        boolean ok = Objects.equals(expected, parsedText[1]);
        logger.info(ok
                ? LoggerMessages.verificationSuccess(parsedText[0], expected, parsedText[1])
                : LoggerMessages.verificationFailure(parsedText[0], expected, parsedText[1]));
        return ok;
    }

    private boolean verifyContractInfo(Customer customer) {
        try {
            By contractLocator = By.xpath("//h2[text()='" + VerificationConfig.CONTRACT_INFO + "']");
            WebElement h2Element = driver.findElement(contractLocator);
            highlighter.highlight(h2Element);
            WebElement parent = h2Element.findElement(By.xpath(".."));

            boolean result = verifyAddress(parent, customer.getPostalCode(), customer.getHouseNumber());

            Map<String, String> customerMap = customer.getAttributesMap();
            for (String attribute : VerificationConfig.CONTRACT_INFO_ATTRIBUTES) {
                result = result && checkElement(parent, customerMap.get(attribute), attribute, VerificationConfig.CONTRACT_INFO);
            }

            if (!result) {
                logger.info(LoggerMessages.subsectionFailure(VerificationConfig.CONTRACT_INFO));
                return false;
            }

            logger.info(LoggerMessages.subsectionSuccess(VerificationConfig.CONTRACT_INFO));
            return true;
        } catch (Exception e) {
            logger.info("CRASH verify {}", VerificationConfig.CONTRACT_INFO);
            throw e;
        }
    }

    private boolean verifyContactInfo(Customer customer) {
        try {
            By contactLocator = By.xpath("//h2[text()='" + VerificationConfig.CONTACT_INFO + "']");
            WebElement h2Element = highlighter.prepareReadOnlyElement(contactLocator);

            WebElement parent = highlighter.prepareChildElement(h2Element, By.xpath(".."));

            boolean result = true;
            Map<String, String> customerMap = customer.getAttributesMap();
            for (String attribute : VerificationConfig.CONTACT_INFO_ATTRIBUTES) {
                result = result && checkElement(parent, customerMap.get(attribute), attribute, VerificationConfig.CONTACT_INFO);
            }

            if (!result) {
                logger.info(LoggerMessages.subsectionFailure(VerificationConfig.CONTACT_INFO));
                return false;
            }

            logger.info(LoggerMessages.subsectionSuccess(VerificationConfig.CONTACT_INFO));
            return true;
        } catch (Exception e) {
            logger.info(LoggerMessages.subsectionCrash(VerificationConfig.CONTACT_INFO));
            throw e;
        }
    }


    public Boolean verifyLastPage(Customer customer) {
        try {
            boolean result = verifyContractInfo(customer) && verifyContactInfo(customer) && secondForm.verify(customer);
            logger.info("Last page verification result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("CRASH during verification: {}", e.getMessage());
            return false;
        }
    }

    public boolean verifyCreatedOffer() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                By contactLocator = By.xpath("//h2[text()='" + VerificationConfig.CONTACT_INFO + "']");
                WebElement h2Element = highlighter.prepareReadOnlyElement(contactLocator);
                WebElement parent = highlighter.prepareChildElement(h2Element, By.xpath(".."));

                WebElement text = highlighter.prepareChildElement(parent, By.xpath(".//*[text()='Offer created successfully']"));
                if (text.isDisplayed()) {
                    logger.info(LoggerMessages.sectionSuccess(("Confirmation message")));
                    return true;
                } else {
                    logger.info(LoggerMessages.subsectionFailure("Confirmation message"));
                    return false;
                }
            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
            } catch (Exception jsException) {
                if (attempt == 3) {
                    logger.error(LoggerMessages.subsectionCrash("Confirmation message"));
                    throw jsException;
                }
            }
            UserActions.randomDelay(BehaviourConfig.MIN_CLICKING_DELAY_MS, BehaviourConfig.MAX_CLICKING_DELAY_MS);
        }
        return false;
    }

    public boolean verify(Customer customer) {
        try{
            Boolean result = verifyLastPage((customer));
            if(!BehaviourConfig.BLOCK_OFFER){
                clickNextButton();
                result = result && verifyCreatedOffer();
            }
            if (result) successLog(); else failLog();
            return result;
        } catch ( Exception e ) {
            crashLog();
            return false;
        }
    }
}
