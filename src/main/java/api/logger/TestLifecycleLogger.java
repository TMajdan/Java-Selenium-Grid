package api.logger;

import lombok.extern.slf4j.Slf4j;
import org.testng.IClassListener;
import org.testng.ITestClass;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Use ony with classes which are based on dependency injection
 */
@Slf4j
public class TestLifecycleLogger implements ITestListener, IClassListener {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onBeforeClass(ITestClass testClass) {
        System.out.println("================== TEST CLASS =====================");
        System.out.println("Running test class: " + testClass.getRealClass().getSimpleName());
        System.out.println("Started at: " + LocalDateTime.now().format(DATE_FORMATTER));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        String formattedTestData = formatTestNameWithParameters(testName, params);

        System.out.println("================== TEST =====================");
        System.out.println("Running test: " + formattedTestData);
        System.out.println("Started at: " + LocalDateTime.now().format(DATE_FORMATTER));
    }

    private String formatTestNameWithParameters(String testName, Object[] parameters) {
        Object[] filteredParameters;
        if (parameters == null || parameters.length == 0) {
            return testName;
        } else {
            filteredParameters = Arrays.stream(parameters).filter(Objects::nonNull).toArray();
        }

        return testName + "[" + Arrays.stream(filteredParameters).map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }
}