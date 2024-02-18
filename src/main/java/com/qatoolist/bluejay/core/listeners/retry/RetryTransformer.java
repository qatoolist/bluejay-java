package com.qatoolist.bluejay.core.listeners.retry;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryTransformer acts as a TestNG annotation transformer. It identifies
 * test methods annotated with the custom `@Retry` annotation and automatically
 * sets the `RetryAnalyzer` for those methods.
 */
public class RetryTransformer implements IAnnotationTransformer {

    /**
     * Transforms a TestNG annotation, dynamically assigning the `RetryAnalyzer`
     * if the test method is annotated with `@Retry`.
     *
     * @param annotation      The TestNG annotation
     * @param testClass       The test class (unused)
     * @param testConstructor The test constructor (unused)
     * @param testMethod      The test method
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod.isAnnotationPresent(Retry.class)) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
