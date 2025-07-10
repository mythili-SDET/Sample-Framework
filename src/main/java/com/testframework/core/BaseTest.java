package com.testframework.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.testframework.api.ApiManager;
import com.testframework.config.ConfigManager;
import com.testframework.database.DatabaseManager;
import com.testframework.ui.WebDriverManager;
import com.testframework.utils.CsvDataProvider;
import com.testframework.utils.ExcelDataProvider;
import com.testframework.utils.JsonDataProvider;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base Test Class providing common functionality for all test classes
 * Handles WebDriver, API, Database, and reporting setup
 */
public abstract class BaseTest {
    
    protected WebDriver driver;
    protected WebDriverManager webDriverManager;
    protected ApiManager apiManager;
    protected DatabaseManager databaseManager;
    protected ConfigManager configManager;
    
    // Data Providers
    protected ExcelDataProvider excelDataProvider;
    protected CsvDataProvider csvDataProvider;
    protected JsonDataProvider jsonDataProvider;
    
    // Reporting
    protected static ExtentReports extentReports;
    protected ExtentTest test;
    
    @BeforeSuite
    public void setUpSuite(ITestContext context) {
        configManager = ConfigManager.getInstance();
        initializeExtentReports();
    }
    
    @BeforeClass
    public void setUpClass() {
        // Initialize managers
        webDriverManager = WebDriverManager.getInstance();
        apiManager = new ApiManager();
        databaseManager = new DatabaseManager();
        
        // Initialize data providers
        excelDataProvider = new ExcelDataProvider();
        csvDataProvider = new CsvDataProvider();
        jsonDataProvider = new JsonDataProvider();
    }
    
    @BeforeMethod
    public void setUpMethod(ITestResult result) {
        // Create test in extent report
        test = extentReports.createTest(result.getMethod().getMethodName());
        test.log(Status.INFO, "Starting test: " + result.getMethod().getMethodName());
        
        // Initialize WebDriver if needed for UI tests
        if (isUITest()) {
            driver = webDriverManager.getDriver();
            test.log(Status.INFO, "WebDriver initialized");
        }
    }
    
    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        // Log test result
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
        } else if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
            test.log(Status.FAIL, "Error: " + result.getThrowable().getMessage());
            
            // Take screenshot on failure for UI tests
            if (isUITest() && driver != null && configManager.isScreenshotOnFailure()) {
                takeScreenshot(result.getMethod().getMethodName());
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        }
        
        // Close WebDriver if it was initialized
        if (driver != null && isUITest()) {
            webDriverManager.quitDriver();
            driver = null;
        }
    }
    
    @AfterClass
    public void tearDownClass() {
        // Close database connection
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }
    
    @AfterSuite
    public void tearDownSuite() {
        // Flush and close extent reports
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    /**
     * Initialize Extent Reports
     */
    private void initializeExtentReports() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            
            // Create reports directory if it doesn't exist
            String reportPath = configManager.getExtentReportPath();
            createDirectoryIfNotExists(reportPath);
            
            // Set report file path
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportFile = reportPath + "TestReport_" + timestamp + ".html";
            
            // Configure extent reports
            extentReports.setSystemInfo("Framework", "Robust Test Automation Framework");
            extentReports.setSystemInfo("Browser", configManager.getBrowserName());
            extentReports.setSystemInfo("Environment", "Test");
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
    }
    
    /**
     * Take screenshot on test failure
     */
    private void takeScreenshot(String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File screenshot = ts.getScreenshotAs(OutputType.FILE);
                
                // Create screenshots directory
                String screenshotPath = configManager.getScreenshotPath();
                createDirectoryIfNotExists(screenshotPath);
                
                // Save screenshot with timestamp
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String fileName = testName + "_" + timestamp + ".png";
                Path destination = Paths.get(screenshotPath, fileName);
                
                Files.copy(screenshot.toPath(), destination);
                test.log(Status.INFO, "Screenshot saved: " + destination.toString());
            }
        } catch (IOException e) {
            test.log(Status.WARNING, "Failed to take screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Create directory if it doesn't exist
     */
    private void createDirectoryIfNotExists(String path) {
        try {
            Path dirPath = Paths.get(path);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + path);
        }
    }
    
    /**
     * Check if current test is a UI test
     * Override this method in specific test classes
     */
    protected boolean isUITest() {
        return false;
    }
    
    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        if (driver != null) {
            driver.get(url);
            test.log(Status.INFO, "Navigated to: " + url);
        }
    }
    
    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return driver != null ? driver.getTitle() : "";
    }
    
    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return driver != null ? driver.getCurrentUrl() : "";
    }
    
    /**
     * Wait for page to load
     */
    protected void waitForPageLoad() {
        if (driver != null) {
            // Simple wait - can be enhanced with explicit waits
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Log information to extent report
     */
    protected void logInfo(String message) {
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log warning to extent report
     */
    protected void logWarning(String message) {
        if (test != null) {
            test.log(Status.WARNING, message);
        }
    }
    
    /**
     * Log error to extent report
     */
    protected void logError(String message) {
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
    
    /**
     * Log success to extent report
     */
    protected void logSuccess(String message) {
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }
    
    /**
     * Assert with logging
     */
    protected void assertTrue(boolean condition, String message) {
        if (condition) {
            logSuccess("Assertion passed: " + message);
        } else {
            logError("Assertion failed: " + message);
            throw new AssertionError(message);
        }
    }
    
    /**
     * Assert equals with logging
     */
    protected void assertEquals(Object actual, Object expected, String message) {
        if (java.util.Objects.equals(actual, expected)) {
            logSuccess("Assertion passed: " + message + " - Expected: " + expected + ", Actual: " + actual);
        } else {
            logError("Assertion failed: " + message + " - Expected: " + expected + ", Actual: " + actual);
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }
}