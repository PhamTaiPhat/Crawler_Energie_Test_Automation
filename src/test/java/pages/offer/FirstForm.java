package pages.offer;

import config.Locators;
import helper.UserActions;
import logger.LoggerMessages;
import model.Customer;
import model.UiContext;

public class FirstForm extends Form {

    public FirstForm(UiContext context, UserActions userActions) {
        super(context, userActions);
    }

    // Buttons
    private void clickElectricityButton() {
        click(Locators.ELECTRICITY_BUTTON);
    }

    private void clickGasButton() {
        click(Locators.GAS_BUTTON);
    }

    private void clickElectricityGasButton() {

        click(Locators.ELECTRICITY_GAS_BUTTON);
    }

    public void clickUsageButton(Customer customer) {
        if (!customer.getGasUsage().isEmpty() && !customer.getElectricityUsage().isEmpty()) {
            clickElectricityGasButton();
        } else if (!customer.getElectricityUsage().isEmpty()) {
            clickElectricityButton();
        } else {
            clickGasButton();
        }
    }

    public void clickSolarPanelsButton() {
        click(Locators.SOLAR_PANELS_BUTTON);
    }

    // Fill fields
    private void fillPostalCode(String input) {
        fill(Locators.POSTCODE, input);
    }

    private void fillHouseNumber(String input) {
        fill(Locators.HOUSE_NUMBER, input);
    }

    private void fillElectricityUsage(String input) {
        fill(Locators.ELECTRICITY_USAGE, input);
    }

    private void fillGasUsage(String input) {
        fill(Locators.GAS_USAGE, input);
    }

    private void fillElectricityProduce(String input) {
        fill(Locators.ELECTRICITY_PRODUCE, input);
    }

    private void fillUsage(Customer customer) {
        if (!customer.getElectricityUsage().isEmpty()) {
            fillElectricityUsage(customer.getElectricityUsage());
        }
        if (!customer.getGasUsage().isEmpty()) {
            fillGasUsage(customer.getGasUsage());
        }
    }

    public boolean fill(Customer customer) {
        try {
            fillPostalCode(customer.getPostalCode());
            fillHouseNumber(customer.getHouseNumber());

            clickUsageButton(customer);
            fillUsage(customer);
            if (!customer.getElectricityProduction().isEmpty()) {
                clickSolarPanelsButton();
                fillElectricityProduce(customer.getElectricityProduction());
            }
            successLog();
            return true;
        } catch (Exception e) {
            // Form filling crashed - will be logged as test crash
            crashLog();
            return false;
        }
    }
}
