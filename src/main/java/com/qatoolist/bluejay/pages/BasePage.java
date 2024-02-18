package com.qatoolist.bluejay.pages;

import com.qatoolist.bluejay.config.ConfigManager;
import org.apache.hc.core5.util.Timeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BasePage abstract class serves as a foundation for page object classes within a
 * Selenium-based test framework. It provides essential setup for WebDriver instances,
 * wait configuration, common utility methods for element interactions, and mechanisms
 * to facilitate page load waits and verification.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String url;
    protected String title;

    protected long DEFAULT_TIMEOUT;

    /**
     * Constructor initializes the WebDriver, establishes a default wait, and uses
     * PageFactory to locate and initialize WebElements defined within concrete page classes.
     *
     * @param driver The active WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.DEFAULT_TIMEOUT = ConfigManager.getDefaultTimeout();
        this.wait = new WebDriverWait(driver, Timeout.ofSeconds(DEFAULT_TIMEOUT).toDuration());
        PageFactory.initElements(driver, this);
    }

    /**
     * Blocks execution until the given WebElement becomes visible using an explicit wait.
     *
     * @param element The WebElement to wait for.
     */
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Constructs the full URL for the page, combining the base URL from the ConfigManager
     * with the relative URL specific to the page object.
     *
     * @return The complete URL for the page.
     */
    protected String getURL() {
        return ConfigManager.getBaseUrl() + url;
    }

    /**
     * Waits for the page to load and verifies that the expected page title is displayed.
     * Throws a timeout exception if the page does not load within the specified wait time
     * or if the title does not match the expected value.
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.titleIs(title));
    }

    /**
     * Combines page load wait with explicit verification of the current URL against the
     * expected URL associated with the page object.
     *
     * @throws IllegalStateException if the current URL does not match the page object's expected URL.
     */
    public void ensurePageLoaded() {
        waitForPageLoad();
        if (!driver.getCurrentUrl().equals(getURL())) {
            throw new IllegalStateException("Incorrect page loaded. Expected " + getURL());
        }
    }
}
