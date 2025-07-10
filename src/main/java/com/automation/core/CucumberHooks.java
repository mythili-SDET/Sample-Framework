package com.automation.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for managing test lifecycle
 * Handles WebDriver, API, and Database setup/teardown
 */
public class CucumberHooks {
    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Setup before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        
        // Initialize configuration
        config = ConfigManager.getInstance();
        
        // Log scenario details
        logger.info("Scenario Tags: {}", scenario.getSourceTagNames());
        logger.info("Environment: {}", config.getEnvironment());
        logger.info("Browser: {}", config.getBrowser());
        logger.info("API Base URL: {}", config.getAPIBaseUrl());
        logger.info("UI Base URL: {}", config.getUIBaseUrl());
    }

    /**
     * Cleanup after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        logger.info("Finishing scenario: {}", scenario.getName());
        
        // Quit WebDriver for current thread
        if (WebDriverManager.hasDriver()) {
            WebDriverManager.quitDriver();
        }
        
        // Log scenario result
        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());
            // Take screenshot on failure
            if (WebDriverManager.hasDriver()) {
                String screenshotPath = takeScreenshot(scenario.getName());
                if (screenshotPath != null) {
                    scenario.attach(screenshotPath, "image/png", "Screenshot");
                }
            }
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }
    }

    /**
     * Setup before all scenarios (runs once)
     */
    @Before(order = 1)
    public void setUpFramework() {
        logger.info("Initializing automation framework");
        
        // Initialize configuration
        config = ConfigManager.getInstance();
        
        // Log framework configuration
        logger.info("Framework Configuration:");
        logger.info("- Environment: {}", config.getEnvironment());
        logger.info("- Browser: {}", config.getBrowser());
        logger.info("- Headless: {}", config.isHeadless());
        logger.info("- Parallel Execution: {}", config.isParallelExecution());
        logger.info("- Thread Count: {}", config.getThreadCount());
        logger.info("- Retry Count: {}", config.getRetryCount());
    }

    /**
     * Cleanup after all scenarios (runs once)
     */
    @After(order = 999)
    public void tearDownFramework() {
        logger.info("Cleaning up automation framework");
        
        // Quit all WebDriver instances
        WebDriverManager.quitAllDrivers();
        
        logger.info("Framework cleanup completed");
    }

    /**
     * Setup for UI tests
     */
    @Before("@UI")
    public void setUpUI() {
        logger.info("Setting up UI test environment");
        
        // WebDriver will be initialized when first accessed
        // This ensures thread-safe WebDriver management
    }

    /**
     * Cleanup for UI tests
     */
    @After("@UI")
    public void tearDownUI() {
        logger.info("Cleaning up UI test environment");
        
        // WebDriver cleanup is handled in main tearDown method
    }

    /**
     * Setup for API tests
     */
    @Before("@API")
    public void setUpAPI() {
        logger.info("Setting up API test environment");
        
        // API setup is handled in BaseAPITest constructor
        // This ensures proper REST Assured configuration
    }

    /**
     * Cleanup for API tests
     */
    @After("@API")
    public void tearDownAPI() {
        logger.info("Cleaning up API test environment");
        
        // API cleanup is minimal as REST Assured is stateless
    }

    /**
     * Setup for Database tests
     */
    @Before("@Database")
    public void setUpDatabase() {
        logger.info("Setting up Database test environment");
        
        // Database connection will be established when first accessed
        // This ensures proper connection management
    }

    /**
     * Cleanup for Database tests
     */
    @After("@Database")
    public void tearDownDatabase() {
        logger.info("Cleaning up Database test environment");
        
        // Database connection cleanup is handled in BaseDBTest
    }

    /**
     * Setup for mixed tests (UI + API + Database)
     */
    @Before("@Mixed")
    public void setUpMixed() {
        logger.info("Setting up mixed test environment (UI + API + Database)");
        
        // All environments will be initialized as needed
        // This allows for hybrid testing scenarios
    }

    /**
     * Cleanup for mixed tests
     */
    @After("@Mixed")
    public void tearDownMixed() {
        logger.info("Cleaning up mixed test environment");
        
        // All cleanup is handled in respective tearDown methods
    }

    /**
     * Setup for parallel execution
     */
    @Before("@Parallel")
    public void setUpParallel() {
        logger.info("Setting up parallel test execution");
        
        // Parallel execution is handled by TestNG configuration
        // This hook can be used for parallel-specific setup
    }

    /**
     * Cleanup for parallel execution
     */
    @After("@Parallel")
    public void tearDownParallel() {
        logger.info("Cleaning up parallel test execution");
        
        // Parallel cleanup is handled by TestNG configuration
    }

    /**
     * Take screenshot on failure
     */
    private String takeScreenshot(String testName) {
        try {
            if (WebDriverManager.hasDriver()) {
                return WebDriverManager.getDriver().getScreenshotAs(org.openqa.selenium.OutputType.FILE).getAbsolutePath();
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
        return null;
    }
}