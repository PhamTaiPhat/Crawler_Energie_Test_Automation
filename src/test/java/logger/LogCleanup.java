package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Utility class to clean up log files before test execution.
 * Ensures fresh logs for each test run.
 * Now only cleans up test-results.log since we only keep success/fail/crash logs.
 */
public class LogCleanup {

    private static final Logger logger = LoggerFactory.getLogger(LogCleanup.class);
    private static final String LOGS_DIR = "logs";
    private static final String TEST_RESULTS_FILE = "test-results.log";

    // Toggles (read from -D system properties or environment variables)
    private static final boolean ENABLED_KEY = true;
    private static final boolean DELETE_MODE_KEY = true;

    /**
     * Cleans up test-results.log file and creates fresh logs directory.
     * Behavior is controlled by:
     * - LOG_CLEANUP_ENABLED: true/false (enable or skip cleanup)
     * - LOG_CLEANUP_DELETE: true/false (delete files vs truncate files)
     */
    public static void cleanupLogs() {
        if (!isCleanupEnabled()) {
            logger.info("[🔕] Log cleanup disabled ({})", ENABLED_KEY);
            return;
        }
        logger.info("[🧹] Starting log cleanup for test results...");

        try {
            Path logsPath = Paths.get(LOGS_DIR);

            // Ensure logs directory exists
            if (!Files.exists(logsPath)) {
                Files.createDirectories(logsPath);
                logger.info("[📁] Created logs directory: {}", logsPath.toAbsolutePath());
                return;
            }

            // Only clean up test-results.log
            Path testResultsPath = logsPath.resolve(TEST_RESULTS_FILE);

            if (isDeleteMode()) {
                // Attempt to delete the test results file
                if (Files.exists(testResultsPath)) {
                    try {
                        Files.delete(testResultsPath);
                        logger.info("[🗑️] Deleted test results log file: {}", TEST_RESULTS_FILE);
                    } catch (IOException e) {
                        logger.warn("[⚠️] Could not delete test results file: {} - {}", TEST_RESULTS_FILE, e.getMessage());
                    }
                }
                logger.info("[🗑️] Test results log cleanup (delete) completed");
            } else {
                // Truncate the test results file
                if (Files.exists(testResultsPath)) {
                    try {
                        Files.write(testResultsPath, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
                        logger.info("[🧹] Truncated test results log file: {}", TEST_RESULTS_FILE);
                    } catch (IOException e) {
                        logger.warn("[⚠️] Could not truncate test results file: {} - {}", TEST_RESULTS_FILE, e.getMessage());
                    }
                }
                logger.info("[🧹] Test results log cleanup (truncate) completed successfully");
            }

        } catch (IOException e) {
            logger.error("[❌] Error during log cleanup: {}", e.getMessage());
        }
    }

    private static boolean isCleanupEnabled() {
        return ENABLED_KEY;
    }

    private static boolean isDeleteMode() {
        return DELETE_MODE_KEY;
    }
}
