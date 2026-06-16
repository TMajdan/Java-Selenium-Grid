package main.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG annotation transformer that dynamically adds the RetryAnalyzer
 * to every test method. This avoids having to add @Test(retryAnalyzer=...)
 * on every test.
 */
@Slf4j
public class RetryListener implements IAnnotationTransformer {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        // Only set retryAnalyzer if not already set at the method level
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
