package config;


public class VerificationConfig {

    public static final String CONTRACT_INFO = "Contract Info";

    public static final String BILLING_ADDRESS = "Billing Address";
    public static final String INITIALS = "Initials";
    public static final String FIRST_NAME = "First Name";
    public static final String MIDDLE_NAME = "Middle Name";
    public static final String LAST_NAME = "Last Name";
    public static final String EMAIL = "Email";
    public static final String PHONE_NUMBER = "Phone Number";
    public static final String BIRTHDAY = "Date of Birth";
    public static final String IBAN = "IBAN";
    public static final String ACCOUNT_HOLDER = "Account Holder Name";

    public static final String[] CONTRACT_INFO_ATTRIBUTES = {INITIALS, FIRST_NAME, LAST_NAME, PHONE_NUMBER, MIDDLE_NAME, EMAIL,
            BIRTHDAY, ACCOUNT_HOLDER, IBAN};

    public static final String CONTACT_INFO = "Contact Info";

    public static final String[] CONTACT_INFO_ATTRIBUTES = {INITIALS, FIRST_NAME, LAST_NAME,
            PHONE_NUMBER, MIDDLE_NAME, EMAIL};

    public static final String DELIVERY_ADDRESS = "Delivery Address";


}
