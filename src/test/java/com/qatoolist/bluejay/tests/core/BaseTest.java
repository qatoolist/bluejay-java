package com.qatoolist.bluejay.tests.core;

import com.qatoolist.bluejay.core.drivers.WebDriverFactory;
import com.qatoolist.bluejay.core.listeners.TestNGListener;
import com.qatoolist.bluejay.core.listeners.interceptor.TestMethodInterceptor;
import com.qatoolist.bluejay.core.listeners.retry.RetryTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestNGListener.class, TestMethodInterceptor.class, RetryTransformer.class})
public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeMethod
    public void setUp(ITestContext context) {
        // Initialize the WebDriver instance
        driver = WebDriverFactory.getDriver();

        // You can also set the WebDriver instance in the context for use in listeners or elsewhere
        context.setAttribute("WebDriver", this.driver);
    }

    @AfterMethod
    public void tearDown() {
        // Quit the WebDriver and clean up resources
        WebDriverFactory.quitDriver();
    }

    @Test
    public void driverTest(){
        logger.info("Verifying that the Driver is not Empty");
        Assert.assertNotEquals(driver, null);
        logger.info("Verified Driver is not Null");
    }
}
