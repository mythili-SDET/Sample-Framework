package com.automation.hooks;

import com.automation.core.*;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.sql.Connection;

/**
 * Cucumber hooks for test setup and teardown
 * Supports mixed scenarios with UI, API, and Database components
 */
public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    
    // Context for sharing data between steps
    private final TestContext testContext;
    
    // Component managers
    private WebDriver driver;
    private Connection dbConnection;
    private String apiToken;
    
    public TestHooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting test setup for scenario: {}", scenario.getName());
        
        // Initialize components based on scenario tags
        initializeComponentsByTags(scenario);
        
        logger.info("Test setup completed for scenario: {}", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Starting test teardown for scenario: {}", scenario.getName());
        
        // Capture screenshot on failure for UI tests
        if (scenario.isFailed() && testContext.hasWebDriver()) {
            captureScreenshotOnFailure(scenario);
        }
        
        // Clean up components
        cleanupComponents();
        
        logger.info("Test teardown completed for scenario: {}", scenario.getName());
    }

    @BeforeAll
    public static void globalSetup() {
        logger.info("Starting global test setup");
        
        // Global initialization if needed
        ConfigManager.getInstance().loadConfiguration();
        
        logger.info("Global test setup completed");
    }

    @AfterAll
    public static void globalTeardown() {
        logger.info("Starting global test teardown");
        
        // Close all database connections
        DatabaseBaseTest.closeAllConnections();
        
        // Clear all tokens
        TokenManager.getInstance().clearAllTokens();
        
        // Quit all drivers
        DriverManager.quitAllDrivers();
        
        logger.info("Global test teardown completed");
    }

    /**
     * Initialize components based on scenario tags
     * @param scenario Current scenario
     */
    private void initializeComponentsByTags(Scenario scenario) {
        // Check for UI tags
        if (hasUITags(scenario)) {
            initializeWebDriver();
        }
        
        // Check for API tags
        if (hasAPITags(scenario)) {
            initializeAPI();
        }
        
        // Check for Database tags
        if (hasDBTags(scenario)) {
            initializeDatabase();
        }
    }

    /**
     * Initialize WebDriver for UI testing
     */
    private void initializeWebDriver() {
        logger.info("Initializing WebDriver for UI testing");
        
        try {
            DriverManager.initializeDriver();
            driver = DriverManager.getDriver();
            testContext.setWebDriver(driver);
            
            // Navigate to base URL if configured
            String baseUrl = ConfigManager.getInstance().getBaseUrl();
            if (baseUrl != null && !baseUrl.isEmpty()) {
                driver.get(baseUrl);
            }
            
            logger.info("WebDriver initialized successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing WebDriver", e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }

    /**
     * Initialize API testing components
     */
    private void initializeAPI() {
        logger.info("Initializing API testing components");
        
        try {
            // Set up API base URI
            String apiBaseUrl = ConfigManager.getInstance().getApiBaseUrl();
            testContext.setApiBaseUrl(apiBaseUrl);
            
            // Initialize authentication token
            apiToken = TokenManager.getInstance().getToken();
            testContext.setAuthToken(apiToken);
            
            logger.info("API components initialized successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing API components", e);
            throw new RuntimeException("API initialization failed", e);
        }
    }

    /**
     * Initialize database connection
     */
    private void initializeDatabase() {
        logger.info("Initializing Database connection");
        
        try {
            // Create database base test instance to get connection
            DatabaseBaseTest dbTest = new DatabaseBaseTest();
            dbTest.dbSetUp();
            dbConnection = dbTest.getConnection();
            testContext.setDatabaseConnection(dbConnection);
            
            logger.info("Database connection initialized successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing Database connection", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Check if scenario has UI tags
     * @param scenario Current scenario
     * @return true if has UI tags
     */
    private boolean hasUITags(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .anyMatch(tag -> tag.contains("@ui") || tag.contains("@web") || tag.contains("@selenium"));
    }

    /**
     * Check if scenario has API tags
     * @param scenario Current scenario
     * @return true if has API tags
     */
    private boolean hasAPITags(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .anyMatch(tag -> tag.contains("@api") || tag.contains("@rest") || tag.contains("@service"));
    }

    /**
     * Check if scenario has Database tags
     * @param scenario Current scenario
     * @return true if has Database tags
     */
    private boolean hasDBTags(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .anyMatch(tag -> tag.contains("@db") || tag.contains("@database") || tag.contains("@sql"));
    }

    /**
     * Capture screenshot on test failure
     * @param scenario Failed scenario
     */
    private void captureScreenshotOnFailure(Scenario scenario) {
        try {
            if (driver != null) {
                // Use existing screenshot method from UIBaseTest
                UIBaseTest uiBase = new UIBaseTest();
                String screenshotPath = uiBase.captureScreenshot(scenario.getName());
                
                if (screenshotPath != null) {
                    // Attach screenshot to Cucumber report
                    byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                            .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Screenshot");
                    
                    logger.info("Screenshot captured and attached to scenario: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Error capturing screenshot", e);
        }
    }

    /**
     * Clean up all initialized components
     */
    private void cleanupComponents() {
        // Clean up WebDriver
        if (testContext.hasWebDriver()) {
            try {
                DriverManager.quitDriver();
                testContext.clearWebDriver();
                logger.info("WebDriver cleaned up");
            } catch (Exception e) {
                logger.error("Error cleaning up WebDriver", e);
            }
        }
        
        // Clean up API token (per thread)
        if (testContext.hasAuthToken()) {
            try {
                // Don't clear token here as it might be reused in same thread
                // TokenManager.getInstance().clearToken();
                testContext.clearAuthToken();
                logger.info("API token cleaned up");
            } catch (Exception e) {
                logger.error("Error cleaning up API token", e);
            }
        }
        
        // Clean up Database connection (resources only, not connection)
        if (testContext.hasDatabaseConnection()) {
            try {
                // Connection cleanup is handled by DatabaseBaseTest
                testContext.clearDatabaseConnection();
                logger.info("Database connection cleaned up");
            } catch (Exception e) {
                logger.error("Error cleaning up Database connection", e);
            }
        }
    }

    // Environment-specific hooks
    
    @Before("@qa")
    public void setUpQAEnvironment() {
        logger.info("Setting up QA environment");
        ConfigManager.getInstance().setEnvironment("qa");
    }
    
    @Before("@uat")
    public void setUpUATEnvironment() {
        logger.info("Setting up UAT environment");
        ConfigManager.getInstance().setEnvironment("uat");
    }
    
    @Before("@stage")
    public void setUpStageEnvironment() {
        logger.info("Setting up STAGE environment");
        ConfigManager.getInstance().setEnvironment("stage");
    }
    
    @Before("@prod")
    public void setUpProdEnvironment() {
        logger.info("Setting up PRODUCTION environment");
        ConfigManager.getInstance().setEnvironment("prod");
    }

    // Component-specific hooks for fine-grained control
    
    @Before("@ui")
    public void setUpUIOnly() {
        logger.info("Setting up UI-only components");
        initializeWebDriver();
    }
    
    @Before("@api")
    public void setUpAPIOnly() {
        logger.info("Setting up API-only components");
        initializeAPI();
    }
    
    @Before("@db")
    public void setUpDatabaseOnly() {
        logger.info("Setting up Database-only components");
        initializeDatabase();
    }

    // Mixed scenario hooks
    
    @Before("@ui and @api")
    public void setUpUIAndAPI() {
        logger.info("Setting up UI and API components for mixed scenario");
        initializeWebDriver();
        initializeAPI();
    }
    
    @Before("@api and @db")
    public void setUpAPIAndDB() {
        logger.info("Setting up API and Database components for mixed scenario");
        initializeAPI();
        initializeDatabase();
    }
    
    @Before("@ui and @db")
    public void setUpUIAndDB() {
        logger.info("Setting up UI and Database components for mixed scenario");
        initializeWebDriver();
        initializeDatabase();
    }
    
    @Before("@ui and @api and @db")
    public void setUpAllComponents() {
        logger.info("Setting up all components (UI, API, Database) for comprehensive scenario");
        initializeWebDriver();
        initializeAPI();
        initializeDatabase();
    }

    // Data-driven test hooks
    
    @Before("@excel")
    public void setUpExcelDataSource() {
        logger.info("Setting up Excel data source");
        testContext.setDataSource("excel");
    }
    
    @Before("@json")
    public void setUpJSONDataSource() {
        logger.info("Setting up JSON data source");
        testContext.setDataSource("json");
    }
    
    @Before("@csv")
    public void setUpCSVDataSource() {
        logger.info("Setting up CSV data source");
        testContext.setDataSource("csv");
    }
}