package config;

import java.math.BigDecimal;

public interface CalculationConfig {

    int ROUNDING_DIGITS = 10;
    String AVG_YEAR = "Average yearly (incl.VAT)";
    String PURCHASE_PRICE = "Purchase Price";
    String PURCHASE_FEE = "Margin Price";
    String ENERGY_TAX = "Energy Tax";
    String FIXED_COST = "Fixed Delivery Cost";
    String ELECTRICITY_OPERATOR = "Network Operator Costs (3x25 Stedin)";
    String GAS_OPERATOR = "Network Operator Costs (G4 Stedin)";
    String DISCOUNT = "Electricity Discount";
    String VAT_STRING = "VAT 21%";
    String PRODUCTION_PRICE = "Production Market Price";
    String PRODUCTION_FEE = "Production Fee";

    // Electricity
    BigDecimal ELECTRICITY_SERVICE_FEE = new BigDecimal("60");

    BigDecimal ELECTRICITY_CONSUMPTION_WHOLESALE = new BigDecimal("0.07");
    BigDecimal ELECTRICITY_CONSUMPTION_FEE = new BigDecimal("0.02");
    BigDecimal ELECTRICITY_PRODUCTION_WHOLESALE = new BigDecimal("-0.04");
    BigDecimal ELECTRICITY_PRODUCTION_FEE = new BigDecimal("0.02");

    BigDecimal ELECTRICITY_TAX_TIER_1 = new BigDecimal("0.10154");
    BigDecimal ELECTRICITY_TAX_TIER_2 = new BigDecimal("0.10154");
    BigDecimal ELECTRICITY_TAX_TIER_3 = new BigDecimal("0.06937");
    BigDecimal ELECTRICITY_TAX_TIER_4 = new BigDecimal("0.003868");
    BigDecimal[] ELECTRICITY_TAX_TIERS = {ELECTRICITY_TAX_TIER_1, ELECTRICITY_TAX_TIER_2,
            ELECTRICITY_TAX_TIER_3, ELECTRICITY_TAX_TIER_4};

    BigDecimal ELECTRICITY_THRESHOLD_TIER_1 = new BigDecimal("2900");
    BigDecimal ELECTRICITY_THRESHOLD_TIER_2 = new BigDecimal("10000");
    BigDecimal ELECTRICITY_THRESHOLD_TIER_3 = new BigDecimal("50000");
    BigDecimal ELECTRICITY_THRESHOLD_TIER_4 = new BigDecimal("10000000");
    BigDecimal[] ELECTRICITY_THRESHOLDS = {new BigDecimal("0"), ELECTRICITY_THRESHOLD_TIER_1, ELECTRICITY_THRESHOLD_TIER_2,
            ELECTRICITY_THRESHOLD_TIER_3, ELECTRICITY_THRESHOLD_TIER_4};

    BigDecimal ELECTRICITY_DISCOUNT = new BigDecimal("-524.95");
    BigDecimal ELECTRICITY_GRID_COST_DAY = new BigDecimal("1.0727");
    BigDecimal DAYS = new BigDecimal("365");
    BigDecimal VAT_NUMBER = new BigDecimal("0.21");

    // Gas
    BigDecimal GAS_SERVICE_FEE = new BigDecimal("60");

    BigDecimal GAS_CONSUMPTION_WHOLESALE = new BigDecimal("0.31");
    BigDecimal GAS_CONSUMPTION_FEE = new BigDecimal("0.08");

    BigDecimal GAS_TAX_TIER_1 = new BigDecimal("0.57816");
    BigDecimal GAS_TAX_TIER_2 = new BigDecimal("0.57816");
    BigDecimal GAS_TAX_TIER_3 = new BigDecimal("0.31573");
    BigDecimal[] GAS_TAX_TIERS = {GAS_TAX_TIER_1, GAS_TAX_TIER_2,
            GAS_TAX_TIER_3};

    BigDecimal GAS_TAX_THRESHOLD_TIER_1 = new BigDecimal("1000");
    BigDecimal GAS_TAX_THRESHOLD_TIER_2 = new BigDecimal("170000");
    BigDecimal GAS_TAX_THRESHOLD_TIER_3 = new BigDecimal("1000000");
    BigDecimal[] GAS_TAX_THRESHOLDS = {new BigDecimal("0"), GAS_TAX_THRESHOLD_TIER_1,
            GAS_TAX_THRESHOLD_TIER_2, GAS_TAX_THRESHOLD_TIER_3};

    BigDecimal GAS_OPERATOR_TIER_1 = new BigDecimal("0.4218");
    BigDecimal GAS_OPERATOR_TIER_2 = new BigDecimal("0.5874");
    BigDecimal GAS_OPERATOR_TIER_3 = new BigDecimal("0.9185");
    BigDecimal[] GAS_OPERATOR_TIERS = {GAS_TAX_TIER_1, GAS_TAX_TIER_2,
            GAS_TAX_TIER_3};

    BigDecimal GAS_OPERATOR_THRESHOLD_TIER_1 = new BigDecimal("500");
    BigDecimal GAS_OPERATOR_THRESHOLD_TIER_2 = new BigDecimal("4000");
    BigDecimal GAS_OPERATOR_THRESHOLD_TIER_3 = new BigDecimal("40000");
    BigDecimal[] GAS_OPERATOR_THRESHOLDS = {new BigDecimal("0"), GAS_OPERATOR_THRESHOLD_TIER_1,
            GAS_OPERATOR_THRESHOLD_TIER_2, GAS_OPERATOR_THRESHOLD_TIER_3};
}
