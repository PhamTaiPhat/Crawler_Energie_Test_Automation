package pages.offer;

import config.Locators;
import helper.UserActions;
import model.Customer;
import model.UiContext;
import org.openqa.selenium.By;

public class FirstForm extends Form {

    public FirstForm(UiContext context, UserActions userActions) {
        super(context);
    }


    // Navigation buttons
    private static final By PREVIOUS_BUTTON = By.id("back-button");

    // First form - Address and energy information
    private static final By POSTCODE = By.id("postalCode-1");
    private static final By HOUSE_NUMBER = By.id("houseNumber-1");
    private static final By ADDITIONAL_INFO = By.id("additionalInfo-1");

    // Energy type selection buttons
    private static final By ELECTRICITY_BUTTON = By.id("electric-1");
    private static final By GAS_BUTTON = By.id("gas-1");
    private static final By ELECTRICITY_GAS_BUTTON = By.id("electric_gas-1");

    // Energy usage fields
    private static final By ELECTRICITY_USAGE = By.id("electricityUsage-1");
    private static final By GAS_USAGE = By.id("gasUsage-1");

    // Solar panels
    private static final By SOLAR_PANELS_BUTTON = By.id("solarPanels-1");
    private static final By ELECTRICITY_PRODUCE = By.id("electricityProcedure-1");

    // Buttons
    private void clickElectricityButton() {
        click(ELECTRICITY_BUTTON);
    }

    private void clickGasButton() {
        click(GAS_BUTTON);
    }

    private void clickElectricityGasButton() {

        click(ELECTRICITY_GAS_BUTTON);
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
        click(SOLAR_PANELS_BUTTON);
    }

    // Fill fields
    private void fillPostalCode(String input) {
        fill(POSTCODE, input);
    }

    private void fillHouseNumber(String input) {
        fill(HOUSE_NUMBER, input);
    }

    private void fillElectricityUsage(String input) {
        fill(ELECTRICITY_USAGE, input);
    }

    private void fillGasUsage(String input) {
        fill(GAS_USAGE, input);
    }

    private void fillElectricityProduce(String input) {
        fill(ELECTRICITY_PRODUCE, input);
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
