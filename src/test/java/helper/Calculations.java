package helper;

import config.CalculationConfig;
import model.Customer;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Calculations {
    private static BigDecimal ACCEPTABLE_DIFFERECE = new BigDecimal("0.1");
    private static final Logger logger = LoggerFactory.getLogger(Calculations.class);


    public static boolean areEqual(BigDecimal a, BigDecimal b) {
        BigDecimal difference = a.subtract(b).abs();
        return difference.compareTo(ACCEPTABLE_DIFFERECE) <= 0;
    }

    public static BigDecimal evaluateNumber(String number) {

        if (number.contains(",") && number.contains(".")) {
            number = number.replace(".", "");
        }
        number = number.replace(",", ".");
        number = number.replaceAll("[^0-9.-]", "");
        return new BigDecimal(number);
    }

    public static BigDecimal evaluateExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("expression cannot be null or empty");
        }

        try {
            // if it's just a number like "900"
            expression = expression.trim().replace(",", ".");
            Expression e = new ExpressionBuilder(expression).build();
            return BigDecimal.valueOf(e.evaluate()).setScale(CalculationConfig.ROUNDING_DIGITS, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            // handle invalid expressions gracefully
            return new BigDecimal("0.0");
        }
    }

    public static  BigDecimal sum(Map<String, BigDecimal> table) {
        BigDecimal result = new BigDecimal("0");
        for (String key : table.keySet()) {
            if (key.equals(CalculationConfig.AVG_YEAR)) {
                continue;
            }
            result = result.add(table.get(key));
        }
        return result;
    }
    private static BigDecimal computeVAT(Map<String, BigDecimal> table) {
        BigDecimal result = sum(table);
        result = result.multiply(CalculationConfig.VAT_NUMBER);
        return result;
    }

    private static BigDecimal tax(BigDecimal usage, BigDecimal[] taxTiers, BigDecimal[] taxThresholds){
        BigDecimal result = new BigDecimal("0");
        for (int i = 0; i < taxTiers.length; i++){
            BigDecimal threshold = taxThresholds[i+1];
            if(usage.compareTo(threshold) <= 0){
                BigDecimal amount = usage.subtract(taxThresholds[i]);
                result = result.add(amount.multiply(taxTiers[i]));
                return result;
            }
            BigDecimal amount = taxThresholds[i+1].subtract(taxThresholds[i]);
            result = result.add(amount.multiply(taxTiers[i]));
        }
        return result;
    }
    private static BigDecimal electricityTax(BigDecimal usage) {
        return tax(usage, CalculationConfig.ELECTRICITY_TAX_TIERS, CalculationConfig.ELECTRICITY_THRESHOLDS);
    }

    public static Map<String, BigDecimal> electricityCosts(Customer customer) {
        Map<String, BigDecimal> result = new HashMap<>();

        result.put(CalculationConfig.FIXED_COST, CalculationConfig.ELECTRICITY_SERVICE_FEE);

        BigDecimal finalUsage = new BigDecimal(customer.getElectricityUsage());
        result.put(CalculationConfig.PURCHASE_PRICE, new BigDecimal("0"));
        result.put(CalculationConfig.PURCHASE_FEE, new BigDecimal("0"));

        if(!customer.getElectricityProduction().isEmpty()){
            finalUsage =  finalUsage.subtract(new BigDecimal(customer.getElectricityProduction()));
        }

        if(finalUsage.compareTo(BigDecimal.ZERO) < 0) {
            finalUsage = finalUsage.negate();
            result.put(CalculationConfig.PRODUCTION_PRICE,
                    finalUsage.multiply(CalculationConfig.ELECTRICITY_PRODUCTION_WHOLESALE));
            result.put(CalculationConfig.PRODUCTION_FEE,
                    finalUsage.multiply(CalculationConfig.ELECTRICITY_PRODUCTION_FEE));
        } else {
            result.put(CalculationConfig.PURCHASE_PRICE,
                    finalUsage.multiply(CalculationConfig.ELECTRICITY_CONSUMPTION_WHOLESALE));
            result.put(CalculationConfig.PURCHASE_FEE,
                    finalUsage.multiply(CalculationConfig.ELECTRICITY_CONSUMPTION_FEE));
        }


        System.out.println("finalUsage: "+finalUsage);
        result.put(CalculationConfig.ENERGY_TAX, electricityTax(finalUsage));
        result.put(CalculationConfig.DISCOUNT, CalculationConfig.ELECTRICITY_DISCOUNT);
        result.put(CalculationConfig.ELECTRICITY_OPERATOR,
                CalculationConfig.DAYS.multiply(CalculationConfig.ELECTRICITY_GRID_COST_DAY));

        result.put(CalculationConfig.VAT_STRING,computeVAT(result));
        result.put(CalculationConfig.AVG_YEAR, sum(result));
        return result;
    }

    private static BigDecimal gasTax(BigDecimal usage){
        return tax(usage, CalculationConfig.GAS_TAX_TIERS, CalculationConfig.GAS_TAX_THRESHOLDS);
    }

    private static BigDecimal gasOperator(BigDecimal usage){
        BigDecimal multiplier = new BigDecimal("0");
        for (int i = 0; i < CalculationConfig.GAS_OPERATOR_TIERS.length; i++){
            if(usage.compareTo(CalculationConfig.GAS_OPERATOR_THRESHOLDS[i+1]) <= 0){
                return CalculationConfig.GAS_OPERATOR_TIERS[i];
            }
        }
        return multiplier;
    }

    public static Map<String, BigDecimal> gasCosts(Customer customer) {
        Map<String, BigDecimal> result = new HashMap<>();

        result.put(CalculationConfig.FIXED_COST, CalculationConfig.GAS_SERVICE_FEE);

        BigDecimal finalUsage = new BigDecimal(customer.getGasUsage());
        System.out.println("finalUsage: "+finalUsage);
        result.put(CalculationConfig.PURCHASE_PRICE,
                finalUsage.multiply(CalculationConfig.GAS_CONSUMPTION_WHOLESALE));
        result.put(CalculationConfig.PURCHASE_FEE,
                finalUsage.multiply(CalculationConfig.GAS_CONSUMPTION_FEE));


        result.put(CalculationConfig.ENERGY_TAX, gasTax(finalUsage));
        result.put(CalculationConfig.GAS_OPERATOR,
                CalculationConfig.DAYS.multiply(gasOperator(finalUsage)));

        result.put(CalculationConfig.VAT_STRING,computeVAT(result));
        result.put(CalculationConfig.AVG_YEAR, sum(result));
        return result;
    }
}
