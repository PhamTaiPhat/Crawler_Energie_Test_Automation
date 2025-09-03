package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging test results (success/failure/crash) to a dedicated log file.
 * This provides a clean, simple log of just test outcomes without detailed execution logs.
 * Only logs the essential test results: success, failure, and crash.
 */
public class TestResultLogger {

    private static final Logger testResultsLogger = LoggerFactory.getLogger("test.results");

    /**
     * Logs a successful test completion.
     *
     * @param testName The name of the test that passed
     */
    public static void logTestSuccess(String testName) {
        testResultsLogger.info("✅ TEST PASSED: {}", testName);
    }

    /**
     * Logs a failed test with error details.
     * A failure is when the test logic fails (assertions, validations, etc.)
     *
     * @param testName     The name of the test that failed
     */
    public static void logTestFailure(String testName) {
        testResultsLogger.info("❌ TEST FAILED: {} ", testName);
    }

    /**
     * Logs a test crash with crash details.
     * A crash is when the test execution is interrupted by an unexpected error
     * (exceptions, system errors, etc.)
     *
     * @param testName     The name of the test that crashed
     * @param crashReason  The reason for the crash
     * @param exception    The exception that caused the crash
     */
    public static void logTestCrash(String testName, String crashReason, Exception exception) {
        testResultsLogger.debug("💥 TEST CRASHED: {} - {} - Exception: {}", testName, crashReason, exception.getMessage());
    }

    /**
     * Logs a test execution summary with duration and status.
     * This provides a concise summary of the test execution.
     *
     * @param testName   The name of the test
     * @param durationMs The duration of the test in milliseconds
     * @param status     The status of the test (PASSED, FAILED, CRASHED)
     */
    public static void logTestSummary(String testName, long durationMs, String status) {
        testResultsLogger.info("📊 TEST SUMMARY: {} - {} ({}ms)", testName, status, durationMs);
    }

    /**
     * Logs a test execution summary with duration and success boolean.
     *
     * @param testName   The name of the test
     * @param durationMs The duration of the test in milliseconds
     * @param success    Whether the test was successful
     */
    public static void logTestSummary(String testName, long durationMs, boolean success) {
        String status = success ? "✅ PASSED" : "❌ FAILED";
        logTestSummary(testName, durationMs, status);
    }
}

