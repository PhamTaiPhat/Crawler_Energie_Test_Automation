package pages.offer;

import config.CalculationConfig;
import helper.Calculations;
import helper.Highlighter;
import helper.Parser;
import helper.UserActions;
import logger.LoggerMessages;
import model.Customer;
import model.UiContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SecondForm extends Form {

    public SecondForm(UiContext context, UserActions userActions) {
        super(context);
    }

    // ---------------------------
    // Parsing helpers
    // ---------------------------

    // Parsing
    private boolean verifyDeliveryAddress(Customer customer) {
        try {
            String postalCode = customer.getPostalCode();
            String houseNumber = customer.getHouseNumber();


            By locator = By.xpath("//table[thead//th[normalize-space(.)='Delivery Address']]");
            WebElement table = userActions.prepareReadOnlyElement(locator);

            WebElement addressElement = userActions.prepareChildElement(table, By.xpath("./tbody/tr[1]/td[2]"));
            String addressText = addressElement.getText();

            String[] extractedCodes = Parser.extractAddressCodes(addressText);

            boolean result = true;
            if (!Objects.equals(extractedCodes[0], postalCode)) {
                logger.info(LoggerMessages.verificationFailure("Postal Code", postalCode, extractedCodes[0]));
                result = false;
            }
            if (!Objects.equals(extractedCodes[1], houseNumber)) {
                logger.info(LoggerMessages.verificationFailure("House Number", houseNumber, extractedCodes[1]));
                result = false;
            }

            if (result) {
                logger.info(LoggerMessages.verificationSuccess("Postal Code", postalCode, extractedCodes[0]));
                logger.info(LoggerMessages.verificationSuccess("House Number", houseNumber, extractedCodes[1]));
            }
            return result;
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Delivery Address"), e.getMessage());
            throw e;
        }
    }

    // ---------------------------
    // Tables & calculations
    // ---------------------------

    private Map<String, String[]> getSpecificTable(List<WebElement> tables, int start, int end) {
        try {

            Map<String, String[]> result = new HashMap<>();
            tables = tables.subList(start, Math.min(tables.size(), end + 1));
            for (WebElement table : tables) {
                By locator = By.tagName("tbody");
                WebElement tbody = userActions.prepareChildElement(table, locator);
                for (WebElement row : tbody.findElements(By.tagName("tr"))) {
                    userActions.highlight(row);
                    List<WebElement> cells = userActions.prepareChildren(row, By.tagName("td"));
                    if (cells.size() < 4) continue;

                    String key = cells.get(1).getText();
                    String left = cells.get(2).getText();
                    String right = cells.get(3).getText();

                    result.put(key, new String[]{left, right});
                }
            }
            logger.info(LoggerMessages.subsectionSuccess("Table Extraction"));
            return result;
        } catch (Exception e) {
            logger.debug(LoggerMessages.subsectionCrash("Table Extraction {}"), e.getMessage());
            return null;
        }
    }

    private Map<String, BigDecimal> transformTable(Map<String, String[]> table) {
        try {
            Map<String, BigDecimal> result = new HashMap<>();
            for (String key : table.keySet()) {
                String[] values = table.get(key);
                BigDecimal number = Calculations.evaluateNumber(values[1]);
                if (values[0] != null && !values[0].isEmpty()) {
                    BigDecimal expression = Calculations.evaluateExpression(values[0]);
                    if (Calculations.areEqual(expression, number)) {
                        logger.info(LoggerMessages.verificationSuccess(key, expression.toString(), number.toString()));
                    } else {
                        logger.info(LoggerMessages.verificationFailure(key, expression.toString(), number.toString()));
                    }
                }
                result.put(key, number);
            }
            logger.debug(LoggerMessages.subsectionSuccess("Transforming table"));
            return result;
        } catch (Exception e) {
            logger.error("Error transforming table", e);
            throw e;
        }
    }

    private boolean checkTable(Map<String, BigDecimal> table) {
        try{
            BigDecimal sum = Calculations.sum(table);
            logger.info("{}", sum);
            return Calculations.areEqual(sum, table.get(CalculationConfig.AVG_YEAR));
        } catch (Exception e) {
            logger.error(LoggerMessages.subsectionCrash("Table checking"), e);
            return false;
        }
    }

    private List<WebElement> getAllTables() {
        WebElement bigTable = userActions.prepareReadOnlyElement(By.tagName("table"));

        By buttonLocator = By.tagName("button");
        WebElement button = userActions.prepareChildElement(bigTable, buttonLocator);

        userActions.clickWebElement(button);

        return userActions.prepareChildren(bigTable, By.tagName("table"));
    }

    private boolean compareElectricity(Customer customer, Map<String, BigDecimal> inputTable) {
        Map<String, BigDecimal> expectedTable = Calculations.electricityCosts(customer);
        boolean result = true;
        for (String key : expectedTable.keySet()) {
            if (!inputTable.containsKey(key)) {
                result = false;
                logger.info("Expected key {} not found", key);
                continue;
            }
            if (!Objects.equals(inputTable.get(key), expectedTable.get(key))) {
                result = false;
                logger.info(LoggerMessages.verificationFailure(key, expectedTable.get(key).toString(), inputTable.get(key).toString()));
            } else {
                logger.info(LoggerMessages.verificationSuccess(key, expectedTable.get(key).toString(), inputTable.get(key).toString()));
            }
        }
        if (result) {
            logger.info(LoggerMessages.subsectionSuccess("ELECTRICITY CHECK"));
        } else {
            logger.info(LoggerMessages.subsectionFailure("ELECTRICITY CHECK"));
        }
        return result;
    }


    private boolean checkCalculations(Customer customer) {
        try {
            List<WebElement> allTables = getAllTables();
            boolean electricityCheck = true;
            boolean gasCheck = true;

            if (customer.getElectricityProduction() != null && !customer.getElectricityProduction().isEmpty()) {
                Map<String, BigDecimal> electricity = transformTable(getSpecificTable(allTables, 0, 1));
                electricityCheck = checkTable(electricity);
                if (allTables.size() >= 2) {
                    allTables.subList(0, 2).clear();
                }
            }
            if (customer.getGasUsage() != null && !customer.getGasUsage().isEmpty()) {
                Map<String, BigDecimal> gas = transformTable(getSpecificTable(allTables, 0, 1));
                gasCheck = checkTable(gas);
            }
            return electricityCheck && gasCheck;
        } catch (Exception e) {
            String report = LoggerMessages.subsectionFailure("Caclculations");
            logger.debug(report);
            throw new RuntimeException(report, e);
        }
    }

    public boolean verify(Customer customer) {
        try {
            boolean result = verifyDeliveryAddress(customer);
            result = result && checkCalculations(customer);
            if (result) successLog(); else failLog();
            return result;
        } catch (Exception e) {
            crashLog();
            return false;
        }
    }
}
