package model;

import config.VerificationConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Customer model class to store customer information for form filling
 * Uses static final fields for default values
 */
public class Customer {

    // Instance fields
    private final String postalCode;
    private final String houseNumber;
    private final String electricityUsage;
    private final String gasUsage;
    private final String electricityProduction;
    private final String initials;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;
    private final String iban;
    private final String accountHolderName;
    private final String birthDay;
    private final String birthMonth;
    private final String birthYear;

    public Customer() {
        this.postalCode = "2572EP";
        this.houseNumber = "192";
        this.electricityUsage = "1000";
        this.gasUsage = "500";
        this.electricityProduction = "100";

        this.initials = "Mr.";
        this.firstName = "First";
        this.middleName = "Middle";
        this.lastName = "Last";
        this.phoneNumber = "0804536060";
        this.email = "testertesting087@gmail.com";
        this.iban = "CZ5808000000003107350143";
        this.accountHolderName = "Tai Phat Pham";
        this.birthDay = "9";
        this.birthMonth = "8";
        this.birthYear = "1950";
    }

    // Getters
    public String getPostalCode() {
        return postalCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getElectricityUsage() {
        return electricityUsage;
    }

    public String getGasUsage() {
        return gasUsage;
    }

    public String getElectricityProduction() {
        return electricityProduction;
    }

    public String getInitials() {
        return initials;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getIban() {
        return iban;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getMiddleName() {
        return middleName;
    }


    /**
     * Returns a map of all customer attributes for easy comparison
     */
    public Map<String, String> getAttributesMap() {
        Map<String, String> attributes = new java.util.HashMap<>();

        attributes.put("Postal Code", postalCode);
        attributes.put("House Number", houseNumber);
        attributes.put("Electricity Usage", electricityUsage);
        attributes.put("Gas Usage", gasUsage);
        attributes.put("Electricity Production", electricityProduction);
        attributes.put(VerificationConfig.INITIALS, initials);
        attributes.put(VerificationConfig.FIRST_NAME, firstName);
        attributes.put(VerificationConfig.MIDDLE_NAME, middleName);
        attributes.put(VerificationConfig.LAST_NAME, lastName);
        attributes.put(VerificationConfig.PHONE_NUMBER, phoneNumber);
        attributes.put(VerificationConfig.EMAIL, email);
        attributes.put(VerificationConfig.IBAN, iban);
        attributes.put(VerificationConfig.ACCOUNT_HOLDER, accountHolderName);
        attributes.put(VerificationConfig.BIRTHDAY, String.format("%02d-%02d-%s", Integer.parseInt(birthDay), Integer.parseInt(birthMonth), birthYear));

        LocalDate date = LocalDate.of(Integer.parseInt(birthYear), Integer.parseInt(birthMonth),
                Integer.parseInt(birthDay));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String formattedDate = date.format(formatter);
        attributes.put("Date of Birth", formattedDate);

        return attributes;
    }

}