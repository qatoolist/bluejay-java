package com.qatoolist.bluejay.core.drivers;

import com.qatoolist.bluejay.core.config.ConfigManager;
import com.qatoolist.bluejay.core.exceptions.UnsupportedBrowserException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Properties;

/**
 * WebDriverFactory provides a simple interface for creating and managing WebDriver instances.
 * It utilizes a ThreadLocal for thread-safe storage of WebDriver objects.
 */
public class WebDriverFactory {

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private WebDriverFactory() {} // Prevent instantiation

    /**
     * Returns a WebDriver instance based on the system property "browser" (default: "chrome").
     * Supported browsers: chrome, firefox.
     *
     * @return WebDriver instance
     * @throws UnsupportedBrowserException if an invalid or unsupported browser type is specified
     */
    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            String browser = System.getProperty("browser", "chrome").toLowerCase();
            Properties browserProperties = ConfigManager.getBrowserProperties(browser);

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    // Example of using browserProperties to configure ChromeDriver
                    ChromeOptions chromeOptions = new ChromeOptions();
                    browserProperties.forEach((key, value) -> chromeOptions.setExperimentalOption((String) key, value));
                    driverPool.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    // Example of using browserProperties to configure FirefoxDriver
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    browserProperties.forEach((key, value) -> firefoxOptions.addPreference((String) key, value.toString()));
                    driverPool.set(new FirefoxDriver(firefoxOptions));
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    browserProperties.forEach((key, value) -> edgeOptions.setCapability((String) key, value));
                    driverPool.set(new EdgeDriver(edgeOptions));
                    break;
                case "safari":
                    // SafariDriver setup is typically simpler as Safari has fewer user-configurable options
                    SafariOptions safariOptions = new SafariOptions();
                    // Example: Configuring SafariOptions if needed. Safari doesn't support the same level of customization.
                    // Note: This example does not directly use browserProperties due to the limited options in Safari.
                    driverPool.set(new SafariDriver(safariOptions));
                    break;
                default:
                    throw new UnsupportedBrowserException("Browser '" + browser + "' is not supported.");
            }
        }
        return driverPool.get();
    }

    /**
     * Quits the WebDriver instance associated with the current thread (if any) and
     * removes it from the pool.
     */
    public static void quitDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
