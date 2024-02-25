package com.qatoolist.bluejay.core.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qatoolist.bluejay.core.drivers.WebDriverFactory;
import com.qatoolist.bluejay.core.reporting.ExtentReportManager;
import com.qatoolist.bluejay.core.utils.ScreenshotUtils;
import org.testng.*;

/**
 * TestNGListener implements TestNG's ITestListener interface to interact with test events
 * and provide enhanced reporting using ExtentReports. Features include:
 * * Detailed execution logs printed to the console
 * * Comprehensive ExtentReports test logs
 * * Screenshot capture on test failures
 */
public class TestNGListener implements ITestListener, ISuiteListener {

    private static final ExtentReports extentReports = ExtentReportManager.getReportInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    static {
        extentTest.remove();
    }

    private ExtentTest suiteTest;

    /**
     * Invoked before the SuiteRunner starts.
     */
    @Override
    public void onStart(ISuite suite) {
        suiteTest = extentReports.createTest("Suite: " + suite.getName());
        suiteTest.log(Status.INFO, "Suite Initialized");
    }

    /**
     * Invoked after the SuiteRunner has run all the test suites.
     */
    @Override
    public void onFinish(ISuite suite) {
        suiteTest.log(Status.INFO, "Suite Completion");
        // Note: Flushing extentReports here may cause premature finalization if multiple suites are involved.
        // It's better to flush in ITestContext#onFinish to ensure all tests are accounted for.
    }

    /**
     * Invoked at the start of a TestNG test suite.
     *
     * @param context The TestNG context
     */
    @Override
    public void onStart(ITestContext context) {
        ExtentTest suite = extentReports.createTest("Test Suite: " + context.getName());
        suite.log(Status.INFO, "Test Suite ["+context.getName()+"] started on: " + context.getStartDate());
    }

    /**
     * Invoked at the end of a TestNG test suite.
     *
     * @param context The TestNG context
     */
    @Override
    public void onFinish(ITestContext context) {
        suiteTest.log(Status.INFO, "Finishing Test Suite: " + context.getName());
        extentReports.flush();
    }

    /**
     * Invoked at the start of a test method.
     *
     * @param result The TestNG result
     */
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports.createTest(result.getMethod().getMethodName());
        test.log(Status.INFO, "Starting Test: " + result.getMethod().getDescription());
        extentTest.set(test);
    }

    /**
     * Invoked when a test method successfully completes.
     *
     * @param result The TestNG result
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    /**
     * Invoked when a test method fails.
     *
     * @param result The TestNG result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable()); // Log the throwable (stack trace)

        try {
            String screenshotPath = ScreenshotUtils.captureScreenshot(WebDriverFactory.getDriver());
            extentTest.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
        } catch (Exception e) {
            extentTest.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
        }

        // Additional context logging in ExtentReports
        extentTest.get().log(Status.INFO, "Test Class: " + result.getTestClass().getName());
        extentTest.get().log(Status.INFO, "Test Method: " + result.getMethod().getMethodName());
    }

    /**
     * Invoked when a test method is skipped.
     *
     * @param result The TestNG result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName() + " due to: " + result.getThrowable());
    }


}
