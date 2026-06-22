package listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Optional;

/**
 * TestNG listener for test lifecycle events.
 * Handles logging, screenshots on failure, and Allure attachments.
 */
@Slf4j
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        log.info("[SUITE] {} ({} tests)", context.getName(), context.getAllTestMethods().length);
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("[DONE] {} passed, {} failed, {} skipped",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String desc = Optional.ofNullable(result.getMethod().getDescription()).orElse("");
        log.info("  [RUN] {}.{} {}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                desc.isEmpty() ? "" : "- " + desc);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long ms = result.getEndMillis() - result.getStartMillis();
        log.info("  [PASS] {}.{} ({}ms)",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                ms);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long ms = result.getEndMillis() - result.getStartMillis();
        log.error("  [FAIL] {}.{} ({}ms)",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                ms);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("  [SKIP] {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("  [-] {}.{} partial fail",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }
}