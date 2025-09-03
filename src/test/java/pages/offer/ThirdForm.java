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
        super(context, userActions);
    }

    //Fill fields
    private void fillInitials(String input) {
        fill(Locators.INITIALS, input);
    }

    private void fillFirstName(String input) {
        fill(Locators.FIRST_NAME, input);
    }

    private void fillMiddleName(String input) {
        fill(Locators.MIDDLE_NAME, input);
    }

    private void fillLastName(String input) {
        fill(Locators.LAST_NAME, input);
    }

    private void fillEmail(String input) {
        fill(Locators.EMAIL, input);
    }

    private void fillPhoneNumber(String input) {
        fill(Locators.PHONE_NUMBER, input);
    }

    private void fillIBAN(String input) {
        fill(Locators.IBAN, input);
    }

    private void fillAccountHolderName(String input) {
        fill(Locators.ACCOUNT_HOLDER, input);
    }

    //Select fields
    private void selectDateOfBirth(String day, String month, String year) {
        click(Locators.BIRTHDAY);

        select(Locators.YEAR_SELECT, year);

        Month monthObject = Month.of(Integer.parseInt(month));
        select(Locators.MONTH_SELECT, monthObject.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

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
