package helper;

import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    public static String[] parseElement(String text) {
        String[] result = text.split(":");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }

    public static String stringPatter(String regexPattern, String text) {
        String value = null;
        Matcher m = Pattern.compile(regexPattern).matcher(text);
        if (m.find()) value = m.group();
        return value;
    }

    public static String[] extractAddressCodes(String address) {
        logger.info("🔍 Extracting address codes from: '{}'", address);

        String postalCodeRegex = "\\b\\d{4}\\s?[A-Z]{2}\\b";
        String postalCode = stringPatter(postalCodeRegex, address);

        String houseNumberRegex = "\\b\\d{1,4}[A-Za-z0-9\\-]{0,3}\\b";
        String houseNumber = stringPatter(houseNumberRegex, address);

        logger.info("🔍 Extracted - Postal Code: '{}', House Number: '{}'", postalCode, houseNumber);
        return new String[]{postalCode, houseNumber};
    }
}
