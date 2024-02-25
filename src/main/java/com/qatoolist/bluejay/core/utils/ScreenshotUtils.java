package com.qatoolist.bluejay.core.utils;

import com.qatoolist.bluejay.core.listeners.retry.RetryAnalyzer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils provides utility methods for capturing screenshots of WebDriver instances.
 */
public class ScreenshotUtils {

    private static final String SCREENSHOT_DIRECTORY = "screenshots";

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    /**
     * Captures a screenshot from the WebDriver, saves it to the 'screenshots' directory,
     * and returns the absolute path. Uses a dynamic filename with a timestamp.
     *
     * @param driver The WebDriver instance
     * @return The absolute path of the captured screenshot, or null if an error occurs.
     */
    public static String captureScreenshot(WebDriver driver) {
        if (!(driver instanceof TakesScreenshot)) {
            logger.error("Driver does not support screenshots.");
            return null;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "screenshot_" + timestamp + ".png";

            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File targetFile = new File(SCREENSHOT_DIRECTORY, fileName);

            // Ensure screenshot directory exists
            FileUtils.forceMkdir(targetFile.getParentFile());

            FileUtils.copyFile(screenshotFile, targetFile);
            return targetFile.getAbsolutePath();

        } catch (IOException e) {
            String error = "Failed to capture or save screenshot: " + e.getMessage();
            logger.error(error);
            return null;
        }
    }
}
