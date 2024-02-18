package com.qatoolist.bluejay.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * ExtentReportManager provides a centralized way to initialize, manage, and flush
 * an ExtentReports instance for test reporting. It follows a Singleton-like pattern
 * ensuring only a single instance of ExtentReports is used.
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;

    // Private constructor to enforce non-instantiability
    private ExtentReportManager() {}

    /**
     * Retrieves the ExtentReports instance (creating it if necessary).
     * Generates a report file with a dynamic timestamp in the /reports directory.
     *
     * @return The ExtentReports instance
     */
    public static synchronized ExtentReports getReportInstance() {
        if (extentReports == null) {
            String reportName = System.getProperty("user.dir") + "/reports/AutomationReport_" + System.currentTimeMillis() + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportName);

            sparkReporter.config().setDocumentTitle("Automation Report");
            sparkReporter.config().setReportName("My Company's Automation Report");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extentReports = new ExtentReports(); // Use 'instance' for clarity
            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo("Environment", "Staging");
            extentReports.setSystemInfo("Browser", "Chrome");

        }
        return extentReports;
    }

    /**
     * Flushes the current ExtentReports instance, writing out any remaining report data.
     */
    public static void flushReport() {
        if (extentReports != null) { // Guard against null extentReports
            extentReports.flush();
        }
    }
}
