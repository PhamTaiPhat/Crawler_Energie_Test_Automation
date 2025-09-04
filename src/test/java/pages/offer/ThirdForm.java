package pages.offer;

import config.Locators;
import helper.UserActions;
import logger.LoggerMessages;
import model.Customer;
import model.UiContext;
import org.openqa.selenium.By;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class ThirdForm extends Form {
    public ThirdForm(UiContext context, UserActions userActions) {
        super(context);
    }

    private static final By INITIALS = By.id("contractInfo.initials");
    private static final By FIRST_NAME = By.id("contractInfo.firstName");
    private static final By MIDDLE_NAME = By.id("contractInfo.middleName");
    private static final By LAST_NAME = By.id("contractInfo.lastName");
    private static final By EMAIL = By.id("contractInfo.email");
    private static final By PHONE_NUMBER = By.id("contractInfo.contactNumber");
    private static final By BIRTHDAY = By.id("contractInfo.dateOfBirth");

    // Banking information
    private static final By IBAN = By.id("iban");
    private static final By ACCOUNT_HOLDER = By.id("accountHolderName");

    // Birthday related locators
    private static final By MONTH_SELECT = By.xpath("//select[@aria-label='Choose the Month']");
    private static final By YEAR_SELECT = By.xpath("//select[@aria-label='Choose the Year']");
    //Fill fields
    private void fillInitials(String input) {
        fill(INITIALS, input);
    }

    private void fillFirstName(String input) {
        fill(FIRST_NAME, input);
    }

    private void fillMiddleName(String input) {
        fill(MIDDLE_NAME, input);
    }

    private void fillLastName(String input) {
        fill(LAST_NAME, input);
    }

    private void fillEmail(String input) {
        fill(EMAIL, input);
    }

    private void fillPhoneNumber(String input) {
        fill(PHONE_NUMBER, input);
    }

    private void fillIBAN(String input) {
        fill(IBAN, input);
    }

    private void fillAccountHolderName(String input) {
        fill(ACCOUNT_HOLDER, input);
    }

    //Select fields
    private void selectDateOfBirth(String day, String month, String year) {
        click(BIRTHDAY);

        select(YEAR_SELECT, year);

        Month monthObject = Month.of(Integer.parseInt(month));
        select(MONTH_SELECT, monthObject.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        By dayLocator = By.xpath("//button[text()='" + day + "']");
        click(dayLocator);
    }

    public boolean fill(Customer customer) {
        // Fill second form (Page 3: personal & banking)
        try {
            fillInitials(customer.getInitials());
            fillFirstName(customer.getFirstName());

            if (!customer.getMiddleName().isEmpty()) {
                fillMiddleName(customer.getMiddleName());
            }

            fillLastName(customer.getLastName());
            fillPhoneNumber(customer.getPhoneNumber());
            fillEmail(customer.getEmail());
            selectDateOfBirth(customer.getBirthDay(), customer.getBirthMonth(), customer.getBirthYear());
            fillIBAN(customer.getIban());
            fillAccountHolderName(customer.getAccountHolderName());
            successLog();
            return true;
        } catch (Exception e) {
            // Form filling crashed - will be logged as test crash
            crashLog();
            return false;
        }
    }
}
