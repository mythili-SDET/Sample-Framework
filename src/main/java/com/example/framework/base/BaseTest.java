package com.example.framework.base;

import com.example.framework.config.ConfigManager;
import com.example.framework.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Parent class for all test classes. Handles WebDriver lifecycle and navigates to base URL for UI tests.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setUp(String browser) {
        // override browser via TestNG parameter
        if (browser != null && !browser.isBlank()) {
            System.setProperty("browser", browser);
        }
        driver = DriverFactory.getDriver();
        // Navigate to base URL if UI test (driver may not be required for API/DB tests)
        if (shouldOpenBaseUrl()) {
            String baseUrl = ConfigManager.getInstance().getString("base.url");
            driver.get(baseUrl);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /**
     * Subclasses for non-UI tests can override this to false.
     */
    protected boolean shouldOpenBaseUrl() {
        return true;
    }
}