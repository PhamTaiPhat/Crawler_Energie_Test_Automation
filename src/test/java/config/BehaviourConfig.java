package config;

/**
 * Configuration class for human-like behavior settings
 */
public class BehaviourConfig {

    public static final Boolean HEADLESS = false;
    public static final Boolean BLOCK_OFFER = false;
    /**
     * Constants
     */
    public static final int WAITING_TIME_SECONDS = 5;
    public static final int IDLE_TIME = 100;

    // Typing delay
    public static final int MIN_TYPING_DELAY_MS = 50;
    public static final int MAX_TYPING_DELAY_MS = 50;
    // Typing clicking
    public static final int MIN_CLICKING_DELAY_MS = 50;
    public static final int MAX_CLICKING_DELAY_MS = 50;
    public static final int CLICK_MAX_RETRIES = 3;

    public static final String COLOR_PROCESSING = "orange";

    public static final int BLINK_COUNT = 1;
    public static final int BLINK_TIME = 100;
} 