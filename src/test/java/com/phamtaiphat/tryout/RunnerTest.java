package com.phamtaiphat.tryout;

import config.BehaviourConfig;
import helper.Highlighter;
import helper.JsonReader;
import helper.UserActions;
import logger.LogCleanup;
import logger.TestResultLogger;
import model.Customer;
import model.UiContext;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Login;
import pages.offer.OfferCreator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class RunnerTest {
    private static final Logger logger = LoggerFactory.getLogger(RunnerTest.class);

    private static WebDriver driver;
    private static OfferCreator offerCreator;

    private static Path userDataDir;              // only used for LOCAL Chrome
    private static boolean isRemote = false;      // true when using SELENIUM_URL

    @BeforeAll
    public static void setUp() {
        // Clean any previous session
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }

        // Build Chrome options (shared for local & remote)
        ChromeOptions options = new ChromeOptions();
        if (BehaviourConfig.HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--window-size=1920,1080",
                "--disable-gpu",
                "--force-device-scale-factor=1",
                "--lang=en-US",
                "--hide-scrollbars",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        // Decide LOCAL ChromeDriver vs REMOTE (Selenium Standalone/Grid)
        String grid = System.getenv("SELENIUM_URL"); // e.g. http://selenium:4444/wd/hub
        isRemote = grid != null && !grid.isBlank();

        if (!isRemote) {
            // LOCAL: optional custom Chrome binary + temp profile dir
            String chromeBin = System.getenv("CHROME_BIN");
            if (chromeBin != null && !chromeBin.isEmpty()) {
                options.setBinary(chromeBin);
            }
            try {
                userDataDir = Files.createTempDirectory("chrome-user-data-");
                options.addArguments("--user-data-dir=" + userDataDir.toAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp Chrome profile directory", e);
            }

            driver = new ChromeDriver(options);
        } else {
            // REMOTE: do NOT pass local file paths like --user-data-dir
            try {
                driver = new RemoteWebDriver(new URL(grid), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Bad SELENIUM_URL: " + grid, e);
            }
        }

        // Make viewport deterministic (headless often ignores --window-size)
        try {
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } catch (Exception ignored) {
            // Some remote providers may not support setWindowRect; it's fine to continue.
        }

        // Use explicit waits only (implicit waits cause flakiness)
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        } catch (Exception ignored) {}

        // Sanity log of actual viewport
        try {
            Object iw = ((JavascriptExecutor) driver).executeScript("return window.innerWidth");
            Object ih = ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
            Object dpr = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
            logger.info("Browser ready. remote={} viewport={}x{} dpr={}", isRemote, iw, ih, dpr);
        } catch (Exception ignored) {}

        // Wire up test helpers
        WebDriverWait waiter = new WebDriverWait(driver, Duration.ofSeconds(BehaviourConfig.WAITING_TIME_SECONDS));
        UiContext uiContext = new UiContext(driver, waiter);
        offerCreator = new OfferCreator(uiContext);

        // Ensure clean teardown even if tests abort
        Runtime.getRuntime().addShutdownHook(new Thread(RunnerTest::tearDown));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }
        // Clean temp Chrome profile only for LOCAL runs
        if (!isRemote && userDataDir != null) {
            try {
                Files.walk(userDataDir)
                        .sorted((a, b) -> b.compareTo(a)) // delete children first
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
            } catch (Exception ignored) {}
            userDataDir = null;
        }
    }

    @BeforeEach
    public void setUpTest() {
        // Fresh logs per test
        LogCleanup.cleanupLogs();
    }

    @Test
    @DisplayName("Run automation test")
    public void runAutomationTest() {
        long startTime = System.currentTimeMillis();
        String testName = "Happy Flow 1- - Fill both forms";
        try{
            JsonReader jsonReader = new JsonReader();
            Customer customer = jsonReader.getFirstCustomer();
            Boolean result = offerCreator.fillBothForms(customer);

            long duration = System.currentTimeMillis() - startTime;

            if (result) {
                TestResultLogger.logTestSuccess(testName);
            } else {
                TestResultLogger.logTestFailure(testName);
            }
            TestResultLogger.logTestSummary(testName,duration,result);
            assert result;


        } catch (RuntimeException e) {
            // Log test crash
            long duration = System.currentTimeMillis() - startTime;
            TestResultLogger.logTestCrash(testName, "Test execution crashed", e);
            TestResultLogger.logTestSummary(testName, duration, "💥 CRASHED");
            throw new RuntimeException(e);
        }

    }
}
