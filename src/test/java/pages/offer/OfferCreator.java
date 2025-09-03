package pages.offer;

import config.BehaviourConfig;
import helper.UserActions;
import logger.TestResultLogger;
import model.Customer;
import model.UiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfferCreator {
    private final UiContext context;
    private final Logger logger = LoggerFactory.getLogger(OfferCreator.class);
    private UserActions userActions;
    private final FirstForm firstForm;
    private final SecondForm secondForm;
    private final ThirdForm thirdForm;
    private final FourthForm fourthForm;

    public OfferCreator(UiContext context, UserActions userActions) {
        this.context = context;
        this.firstForm = new FirstForm(context, userActions);
        this.secondForm = new SecondForm(context, userActions);
        this.thirdForm = new ThirdForm(context, userActions);
        this.fourthForm = new FourthForm(context, userActions);
    }

    private boolean fillFirstForm(Customer customer) {
        return firstForm.fill(customer);
    }

    private boolean verifySecondForm(Customer customer) {
        return secondForm.verify(customer);
    }

    private boolean fillThirdForm(Customer customer) {
        return thirdForm.fill(customer);
    }

    private boolean verifyFourthForm(Customer customer) {
        return fourthForm.verify(customer);
    }

    private void navigateToPlatformPage() {
        firstForm.navigateToPlatformPage();
    }

    private void clickEnglishButton() {
        firstForm.clickEnglishButton();
    }

    private void clickNextButton() {
        firstForm.clickNextButton();
    }

    private void setup() {
        try {
            Thread.sleep(BehaviourConfig.IDLE_TIME);
            context.driver().manage().window().maximize();
            Thread.sleep(BehaviourConfig.IDLE_TIME);
            navigateToPlatformPage();
            Thread.sleep(BehaviourConfig.IDLE_TIME);
            clickEnglishButton();
        } catch (Exception e) {
            throw new RuntimeException("[💥] Crashed during set up - RETRY TEST.", e);
        }
    }

    public boolean fillBothForms(Customer customer) {
        long startTime = System.currentTimeMillis();
        String testName = "Happy Flow 1- - Fill both forms";

        try {
            setup();

            // Fill out first form
            Boolean result = fillFirstForm(customer);
            clickNextButton();
            // Verify second form
            result = result && verifySecondForm(customer);
            clickNextButton();
            // Fill out third form
            result = result && fillThirdForm(customer);
            clickNextButton();
            // Verify fourth form
            result = result && verifyFourthForm(customer);
            //Send offer and check

            long duration = System.currentTimeMillis() - startTime;

            if (result) {
                TestResultLogger.logTestSuccess(testName);
            } else {
                TestResultLogger.logTestFailure(testName);
            }
            TestResultLogger.logTestSummary(testName,duration,result);
            return result;

        } catch (Exception e) {
            // Test crashed - logged via TestResultLogger

            // Log test crash
            long duration = System.currentTimeMillis() - startTime;
            TestResultLogger.logTestCrash(testName, "Test execution crashed", e);
            TestResultLogger.logTestSummary(testName, duration, "💥 CRASHED");

            throw new RuntimeException("[💥] [CRASHED] [Fill both forms with default customer]", e);
        }
    }
}
