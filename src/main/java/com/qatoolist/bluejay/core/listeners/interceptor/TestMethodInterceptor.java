package com.qatoolist.bluejay.core.listeners.interceptor;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import java.util.ArrayList;
import java.util.List;

/**
 * TestMethodInterceptor intercepts TestNG test methods and conditionally executes them
 * based on the presence and value of the `RunIf` annotation.
 */
public class TestMethodInterceptor implements IMethodInterceptor {

    /**
     * Intercepts a list of TestNG method instances, filtering and modifying the list for execution
     * based on `RunIf` annotations and the current environment.
     *
     * @param methods  The original list of TestNG method instances
     * @param context  The TestNG test context
     * @return A list of IMethodInstance, potentially filtered, representing the methods to be executed
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        String currentEnvironment = System.getProperty("test.environment", "dev");

        List<IMethodInstance> result = new ArrayList<>();

        for (IMethodInstance method : methods) {
            RunIf runIfAnnotation = method.getMethod().getConstructorOrMethod().getMethod().getAnnotation(RunIf.class);

            if (runIfAnnotation != null) {
                if (runIfAnnotation.environment().equalsIgnoreCase(currentEnvironment)) {
                    result.add(method);
                }
            } else {
                result.add(method); // Include methods without RunIf by default
            }
        }

        return result;
    }
}