package config;

import org.openqa.selenium.By;

/**
 * Locators for CrawlerEnergie page elements
 * This class contains all web element locators organized by category for better maintainability
 */
public class Locators {

    // Page URL
    public static final String URL = "https://dev.crawlerenergie.nl/platform";

    // Basic page elements
    public static final By BODY_TAG = By.tagName("body");
    public static final By ENGLISH = By.xpath("//button[text()='EN']");

    // Navigation buttons
    public static final By PREVIOUS_BUTTON = By.id("back-button");
    public static final By NEXT_BUTTON = By.id("next-button");

    // First form - Address and energy information
    public static final By POSTCODE = By.id("postalCode-1");
    public static final By HOUSE_NUMBER = By.id("houseNumber-1");
    public static final By ADDITIONAL_INFO = By.id("additionalInfo-1");

    // Energy type selection buttons
    public static final By ELECTRICITY_BUTTON = By.id("electric-1");
    public static final By GAS_BUTTON = By.id("gas-1");
    public static final By ELECTRICITY_GAS_BUTTON = By.id("electric_gas-1");

    // Energy usage fields
    public static final By ELECTRICITY_USAGE = By.id("electricityUsage-1");
    public static final By GAS_USAGE = By.id("gasUsage-1");

    // Solar panels
    public static final By SOLAR_PANELS_BUTTON = By.id("solarPanels-1");
    public static final By ELECTRICITY_PRODUCE = By.id("electricityProcedure-1");

    // Second form - Personal information
    public static final By INITIALS = By.id("contractInfo.initials");
    public static final By FIRST_NAME = By.id("contractInfo.firstName");
    public static final By MIDDLE_NAME = By.id("contractInfo.middleName");
    public static final By LAST_NAME = By.id("contractInfo.lastName");
    public static final By EMAIL = By.id("contractInfo.email");
    public static final By PHONE_NUMBER = By.id("contractInfo.contactNumber");
    public static final By BIRTHDAY = By.id("contractInfo.dateOfBirth");

    // Banking information
    public static final By IBAN = By.id("iban");
    public static final By ACCOUNT_HOLDER = By.id("accountHolderName");

    // Birthday related locators
    public static final By MONTH_SELECT = By.xpath("//select[@aria-label='Choose the Month']");
    public static final By YEAR_SELECT = By.xpath("//select[@aria-label='Choose the Year']");
}
