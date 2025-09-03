package logger;

import org.openqa.selenium.WebElement;

/**
 * Centralized logging messages for the application.
 * This class provides consistent and maintainable logging messages.
 */
public class LoggerMessages {

    // General operation messages
    private static final String SUCCESS = "[✅]";
    private static final String FAILURE = "[❌]";
    private static final String CRASH = "[⚠️]";

    private static final String VERIFICATION = "[VERIFICATION]";
    private static final String ACTION = "[ACTION]";
    private static final String SECTION = "Section";
    private static final String SUBSECTION = "Subsection";

    private static final String BUTTON = "BUTTON";
    // Generic
    private static String success(String string) {
        return String.format("%s %s", SUCCESS, string);
    }

    private static String failure(String string) {
        return String.format("%s %s", FAILURE, string);
    }

    private static String crash(String string){
        return String.format("%s %s", CRASH, string);
    }

    // Sections
    public static String sectionCrash(String input) {
        String rest = String.format("%s: %s", SECTION,input);
        return crash(input);
    }
    public static String sectionSuccess(String input) {
        String rest = String.format("%s: %s", SECTION,input);
        return success(input);
    }

    public static String sectionFailure(String input) {
        String rest = String.format("%s: %s", SECTION,input);
        return failure(input);
    }

    //Subsection
    public static String subsectionSuccess(String input) {
        String rest = String.format("%s: %s", SUBSECTION,input);
        return success(rest);
    }

    public static String subsectionFailure(String input) {
        String rest = String.format("%s: %s", SUBSECTION,input);
        return failure(rest);
    }

    public static String subsectionCrash(String input) {
        String rest = String.format("%s: %s", SUBSECTION,input);
        return crash(rest);
    }

    // Verification
    public static String verificationSuccess(String key, String expected, String found) {
        String rest = String.format("%s [Key: %s] [Expected: %s] [Found: %s]", VERIFICATION, key, expected, found);
        return success(rest);
    }

    public static String verificationFailure(String key, String expected, String found) {
        String rest = String.format("%s [Key: %s] [Expected: %s] [Found: %s]", VERIFICATION, key, expected, found);
        return failure(rest);
    }

    public static String verificationCrash(String key, String expected, String found) {
        String rest = String.format("%s [Key: %s] [Expected: %s] [Found: %s]", VERIFICATION, key, expected, found);
        return crash(rest);
    }

    // Unified action messages
    public static String actionSuccess(WebElement element, String input) {
        String inp = String.format("Input: %s", input);
        if(element.getTagName().equalsIgnoreCase(BUTTON)) {
            inp = "";
        }
        String rest = String.format("%s %s [%s] [%s]",ACTION , element.getTagName(), element.getAttribute("id") , inp);
        return success(rest);
    }

    public static String actionFailure(WebElement element, String input) {
        String inp = String.format("Input: %s", input);
        if(element.getTagName().equalsIgnoreCase(BUTTON)) {
            inp = "";
        }
        String rest = String.format("%s %s [%s] [%s]",ACTION , element.getTagName(), element.getAttribute("id") , inp);
        return failure(rest);
    }

    public static String actionCrash(WebElement element, String input) {
        String inp = String.format("Input: %s", input);
        if(element.getTagName().equalsIgnoreCase(BUTTON)) {
            inp = "";
        }
        String rest = String.format("%s %s [%s] [%s]",ACTION , element.getTagName(), element.getAttribute("id") , inp);
        return crash(rest);
    }


}
