package com.testframework.cucumber;

import com.testframework.core.ConfigManager;
import com.testframework.core.WebDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cucumber Hooks for additional setup and teardown functionality
 * Provides global hooks that run before and after scenarios
 */
public class CucumberHooks {
    
    private static final ConfigManager configManager = ConfigManager.getInstance();
    private static final WebDriverManager webDriverManager = WebDriverManager.getInstance();
    
    /**
     * Global setup before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("=== Starting Scenario: " + scenario.getName() + " ===");
        System.out.println("Scenario Tags: " + scenario.getSourceTagNames());
        
        // Set up test environment
        setupTestEnvironment(scenario);
    }
    
    /**
     * Global teardown after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        System.out.println("=== Ending Scenario: " + scenario.getName() + " ===");
        System.out.println("Scenario Status: " + (scenario.isFailed() ? "FAILED" : "PASSED"));
        
        // Clean up test environment
        cleanupTestEnvironment(scenario);
        
        System.out.println("=== Scenario Completed ===");
    }
    
    /**
     * Setup test environment based on scenario tags
     */
    private void setupTestEnvironment(Scenario scenario) {
        // Check if this is a UI test
        if (scenario.getSourceTagNames().contains("@ui")) {
            setupUIEnvironment();
        }
        
        // Check if this is an API test
        if (scenario.getSourceTagNames().contains("@api")) {
            setupAPIEnvironment();
        }
        
        // Check if this is a database test
        if (scenario.getSourceTagNames().contains("@database")) {
            setupDatabaseEnvironment();
        }
        
        // Setup for smoke tests
        if (scenario.getSourceTagNames().contains("@smoke")) {
            setupSmokeTestEnvironment();
        }
        
        // Setup for regression tests
        if (scenario.getSourceTagNames().contains("@regression")) {
            setupRegressionTestEnvironment();
        }
    }
    
    /**
     * Setup UI test environment
     */
    private void setupUIEnvironment() {
        System.out.println("Setting up UI test environment...");
        
        // Initialize WebDriver if not already done
        if (webDriverManager.getDriver() == null) {
            webDriverManager.getDriver();
        }
        
        // Set window size
        WebDriver driver = webDriverManager.getDriver();
        if (driver != null) {
            driver.manage().window().maximize();
        }
    }
    
    /**
     * Setup API test environment
     */
    private void setupAPIEnvironment() {
        System.out.println("Setting up API test environment...");
        
        // Set base URI for API tests
        String baseUri = configManager.getApiBaseUrl();
        System.out.println("API Base URL: " + baseUri);
    }
    
    /**
     * Setup database test environment
     */
    private void setupDatabaseEnvironment() {
        System.out.println("Setting up database test environment...");
        
        // Verify database connection
        String dbUrl = configManager.getDatabaseUrl();
        System.out.println("Database URL: " + dbUrl);
    }
    
    /**
     * Setup smoke test environment
     */
    private void setupSmokeTestEnvironment() {
        System.out.println("Setting up smoke test environment...");
        
        // Additional setup for smoke tests
        // This could include setting up test data, clearing caches, etc.
    }
    
    /**
     * Setup regression test environment
     */
    private void setupRegressionTestEnvironment() {
        System.out.println("Setting up regression test environment...");
        
        // Additional setup for regression tests
        // This could include more comprehensive environment setup
    }
    
    /**
     * Cleanup test environment
     */
    private void cleanupTestEnvironment(Scenario scenario) {
        // Take screenshot on failure for UI tests
        if (scenario.isFailed() && scenario.getSourceTagNames().contains("@ui")) {
            takeScreenshot(scenario);
        }
        
        // Clean up WebDriver for UI tests
        if (scenario.getSourceTagNames().contains("@ui")) {
            cleanupUIEnvironment();
        }
        
        // Clean up API environment
        if (scenario.getSourceTagNames().contains("@api")) {
            cleanupAPIEnvironment();
        }
        
        // Clean up database environment
        if (scenario.getSourceTagNames().contains("@database")) {
            cleanupDatabaseEnvironment();
        }
    }
    
    /**
     * Take screenshot and attach to scenario
     */
    private void takeScreenshot(Scenario scenario) {
        try {
            WebDriver driver = webDriverManager.getDriver();
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");
                
                // Save screenshot to file
                saveScreenshotToFile(screenshot, scenario.getName());
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Save screenshot to file
     */
    private void saveScreenshotToFile(byte[] screenshot, String scenarioName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshot_" + scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            
            Path screenshotDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotDir);
            
            Path screenshotPath = screenshotDir.resolve(fileName);
            Files.write(screenshotPath, screenshot);
            
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Cleanup UI environment
     */
    private void cleanupUIEnvironment() {
        System.out.println("Cleaning up UI test environment...");
        
        // Close WebDriver
        webDriverManager.quitDriver();
    }
    
    /**
     * Cleanup API environment
     */
    private void cleanupAPIEnvironment() {
        System.out.println("Cleaning up API test environment...");
        
        // Reset API manager state if needed
        // This could include clearing headers, cookies, etc.
    }
    
    /**
     * Cleanup database environment
     */
    private void cleanupDatabaseEnvironment() {
        System.out.println("Cleaning up database test environment...");
        
        // Close database connections
        // This would be handled by the DatabaseManager in the step definitions
    }
    
    /**
     * Hook for before all scenarios
     */
    @Before(order = 1)
    public void beforeAll() {
        System.out.println("=== Test Execution Started ===");
        System.out.println("Framework Version: 1.0");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
    }
    
    /**
     * Hook for after all scenarios
     */
    @After(order = 1)
    public void afterAll() {
        System.out.println("=== Test Execution Completed ===");
    }
}