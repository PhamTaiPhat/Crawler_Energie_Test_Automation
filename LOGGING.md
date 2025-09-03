# Logging Configuration

This project has been configured to keep only **success**, **fail**, and **crash** logs for test execution.

## What Gets Logged

### ✅ Test Results (Success/Fail/Crash)
- **Success**: When a test passes successfully
- **Failure**: When a test fails due to assertions or validations
- **Crash**: When a test crashes due to unexpected exceptions or system errors
- **Summary**: Test execution duration and final status

### 📁 Log Files
- **`logs/test-results.log`**: Contains only test outcome logs
- **Daily rolling**: Files are rolled daily with format `test-results-YYYY-MM-DD.i.log`
- **Size limit**: Each log file is limited to 1MB before rolling

## What Does NOT Get Logged

- General execution details
- Form filling steps
- Verification details
- Selenium operations
- Debug information
- Test start/completion messages

## Log Format

```
2025-01-27 10:30:15.123 -- ✅ TEST PASSED: [START] [Fill both forms with default customer]
2025-01-27 10:30:15.456 -- 📊 TEST SUMMARY: [START] [Fill both forms with default customer] - ✅ PASSED (5000ms)
```

## Configuration

The logging is controlled by `src/test/resources/logback-test.xml`:

- **Console**: Shows only test results
- **File**: Saves test results to `logs/test-results.log`
- **Levels**: 
  - `test.results`: INFO (shows all test outcomes)
  - Other loggers: ERROR only (minimal noise)

## Benefits

1. **Clean logs**: Only essential test outcomes are recorded
2. **Easy analysis**: Quick identification of test status
3. **Reduced noise**: No overwhelming execution details
4. **Focused debugging**: Concentrate on test failures and crashes

## Running Tests

Tests will automatically:
1. Clean up previous log files
2. Execute the test automation
3. Log only the final results (success/fail/crash)
4. Provide execution duration and status

## Example Output

```
✅ TEST PASSED: [START] [Fill both forms with default customer]
📊 TEST SUMMARY: [START] [Fill both forms with default customer] - ✅ PASSED (5000ms)
```

Or in case of failure:

```
❌ TEST FAILED: [START] [Fill both forms with default customer] - Form validation failed
📊 TEST SUMMARY: [START] [Fill both forms with default customer] - ❌ FAILED (3000ms)
```

Or in case of crash:

```
💥 TEST CRASHED: [START] [Fill both forms with default customer] - Test execution crashed - Exception: Element not found
📊 TEST SUMMARY: [START] [Fill both forms with default customer] - 💥 CRASHED (2000ms)
```

