package api.logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class LoggerListener implements ITestListener {

    private static final InheritableThreadLocal<TestData> testData = new InheritableThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String testName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName();
        testData.set(new TestData(methodName, testName != null ? testName : "", result.getParameters()));
    }

    @Override
    public void onFinish(ITestContext context) {
        testData.remove();
    }

    public static String getCurrentTestMethodName() {
        return testData.get() == null ? "" : testData.get().methodName;
    }

    public static String getCurrentTestName() {
        return testData.get() == null ? "" : testData.get().testName;
    }

    public static Object[] getCurrentTestParameters() {
        return testData.get() == null ? new Object[0] : testData.get().parameters;
    }

    private record TestData(String methodName, String testName, Object[] parameters) {
    }
}