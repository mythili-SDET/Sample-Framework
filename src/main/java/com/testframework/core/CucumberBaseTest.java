package com.testframework.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cucumber Base Test Class providing BDD testing capabilities
 * Extends BaseTest and adds Cucumber-specific functionality
 */
public abstract class CucumberBaseTest extends BaseTest {
    
    protected Scenario scenario;
    
    /**
     * Setup method called before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        logInfo("Starting scenario: " + scenario.getName());
        
        // Initialize managers if not already done
        if (webDriverManager == null) {
            webDriverManager = WebDriverManager.getInstance();
        }
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        if (excelDataProvider == null) {
            excelDataProvider = new ExcelDataProvider();
        }
        if (csvDataProvider == null) {
            csvDataProvider = new CsvDataProvider();
        }
        if (jsonDataProvider == null) {
            jsonDataProvider = new JsonDataProvider();
        }
    }
    
    /**
     * Teardown method called after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        // Log scenario result
        if (scenario.isFailed()) {
            logError("Scenario failed: " + scenario.getName());
            
            // Take screenshot on failure for UI tests
            if (isUITest() && driver != null && configManager.isScreenshotOnFailure()) {
                takeScreenshot(scenario.getName());
                attachScreenshotToScenario(scenario);
            }
        } else {
            logSuccess("Scenario passed: " + scenario.getName());
        }
        
        // Close WebDriver if it was initialized
        if (driver != null && isUITest()) {
            webDriverManager.quitDriver();
            driver = null;
        }
        
        // Close database connection
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }
    
    /**
     * Attach screenshot to Cucumber scenario
     */
    private void attachScreenshotToScenario(Scenario scenario) {
        if (driver instanceof TakesScreenshot) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot");
        }
    }
    
    /**
     * Log step information to scenario
     */
    protected void logStep(String stepDescription) {
        if (scenario != null) {
            scenario.log("STEP: " + stepDescription);
        }
        logInfo("STEP: " + stepDescription);
    }
    
    /**
     * Log data information to scenario
     */
    protected void logData(String dataDescription, String data) {
        if (scenario != null) {
            scenario.log("DATA: " + dataDescription + " = " + data);
        }
        logInfo("DATA: " + dataDescription + " = " + data);
    }
    
    /**
     * Log verification information to scenario
     */
    protected void logVerification(String verificationDescription) {
        if (scenario != null) {
            scenario.log("VERIFICATION: " + verificationDescription);
        }
        logInfo("VERIFICATION: " + verificationDescription);
    }
    
    /**
     * Assert with scenario logging
     */
    protected void assertTrueWithLog(boolean condition, String message) {
        if (condition) {
            logVerification("PASSED: " + message);
        } else {
            logError("FAILED: " + message);
            if (scenario != null) {
                scenario.log("ASSERTION FAILED: " + message);
            }
            throw new AssertionError(message);
        }
    }
    
    /**
     * Assert equals with scenario logging
     */
    protected void assertEqualsWithLog(Object actual, Object expected, String message) {
        if (java.util.Objects.equals(actual, expected)) {
            logVerification("PASSED: " + message + " - Expected: " + expected + ", Actual: " + actual);
        } else {
            logError("FAILED: " + message + " - Expected: " + expected + ", Actual: " + actual);
            if (scenario != null) {
                scenario.log("ASSERTION FAILED: " + message + " - Expected: " + expected + ", Actual: " + actual);
            }
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    /**
     * Wait for specified time and log
     */
    protected void waitAndLog(int seconds, String reason) {
        logStep("Waiting " + seconds + " seconds: " + reason);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Navigate to URL with logging
     */
    protected void navigateToWithLog(String url) {
        logStep("Navigating to: " + url);
        if (driver != null) {
            driver.get(url);
        }
    }
    
    /**
     * Get page title with logging
     */
    protected String getPageTitleWithLog() {
        String title = getPageTitle();
        logData("Page Title", title);
        return title;
    }
    
    /**
     * Get current URL with logging
     */
    protected String getCurrentUrlWithLog() {
        String url = getCurrentUrl();
        logData("Current URL", url);
        return url;
    }
    
    /**
     * Check if current test is a UI test
     * Override this method in specific test classes
     */
    @Override
    protected boolean isUITest() {
        return false;
    }
}