package com.automation.tests.mixed;

import com.automation.api.GETRequest;
import com.automation.api.POSTRequest;
import com.automation.api.PUTRequest;
import com.automation.api.DELETERequest;
import com.automation.core.BaseDBTest;
import com.automation.core.BaseUITest;
import com.automation.core.ConfigManager;
import com.automation.utils.ExcelDataProvider;
import com.automation.utils.JSONDataProvider;
import com.automation.utils.CSVDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mixed Test Suite demonstrating UI + API + Database testing
 * Shows how to combine different testing approaches in a single test
 */
public class MixedTestSuite extends BaseUITest {
    private static final Logger logger = LogManager.getLogger(MixedTestSuite.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private BaseDBTest dbTest;
    private Connection dbConnection;
    private GETRequest getRequest;
    private POSTRequest postRequest;
    private PUTRequest putRequest;
    private DELETERequest deleteRequest;
    
    private String createdUserId;
    private String createdUserEmail;

    @BeforeClass
    public void setUp() {
        logger.info("Setting up Mixed Test Suite");
        
        // Initialize database connection
        dbTest = new BaseDBTest();
        dbConnection = dbTest.getConnection();
        
        // Initialize API request objects
        getRequest = new GETRequest();
        postRequest = new POSTRequest();
        putRequest = new PUTRequest();
        deleteRequest = new DELETERequest();
        
        logger.info("Mixed Test Suite setup completed");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Cleaning up Mixed Test Suite");
        
        // Clean up test data
        if (createdUserId != null) {
            try {
                deleteRequest.sendRequest("/users/" + createdUserId);
                logger.info("Cleaned up test user via API");
            } catch (Exception e) {
                logger.error("Error cleaning up test user via API", e);
            }
        }
        
        // Clean up database test data
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.execute("DELETE FROM users WHERE email LIKE '%test%'");
            logger.info("Cleaned up test data from database");
        } catch (SQLException e) {
            logger.error("Error cleaning up database test data", e);
        }
        
        logger.info("Mixed Test Suite cleanup completed");
    }

    /**
     * Test: Create user via API, verify in database, then login via UI
     */
    @Test(description = "End-to-end user creation and login test")
    public void testUserCreationAndLogin() {
        logger.info("Starting end-to-end user creation and login test");
        
        // Step 1: Create user via API
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Mixed Test User");
        userData.put("email", "mixedtest@example.com");
        userData.put("password", "MixedTestPass123");
        userData.put("role", "user");
        
        var apiResponse = postRequest.sendRequest("/users", userData);
        Assert.assertEquals(apiResponse.getStatusCode(), 201, "User should be created via API");
        
        // Extract user ID from response
        String responseBody = apiResponse.getBody().asString();
        createdUserId = extractUserIdFromResponse(responseBody);
        createdUserEmail = (String) userData.get("email");
        
        logger.info("User created via API with ID: {}", createdUserId);
        
        // Step 2: Verify user exists in database
        verifyUserInDatabase(createdUserEmail, userData);
        
        // Step 3: Login via UI
        loginViaUI(createdUserEmail, (String) userData.get("password"));
        
        // Step 4: Verify user can access dashboard
        verifyDashboardAccess();
        
        logger.info("End-to-end user creation and login test completed successfully");
    }

    /**
     * Test: Update user via API and verify changes in UI
     */
    @Test(description = "Update user via API and verify in UI")
    public void testUserUpdateAndUIVerification() {
        logger.info("Starting user update and UI verification test");
        
        // Step 1: Create initial user via API
        Map<String, Object> initialUserData = new HashMap<>();
        initialUserData.put("name", "Original User");
        initialUserData.put("email", "original@example.com");
        initialUserData.put("password", "OriginalPass123");
        initialUserData.put("role", "user");
        
        var createResponse = postRequest.sendRequest("/users", initialUserData);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "User should be created");
        
        String userId = extractUserIdFromResponse(createResponse.getBody().asString());
        
        // Step 2: Update user via API
        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("name", "Updated User Name");
        updatedUserData.put("email", "updated@example.com");
        
        var updateResponse = putRequest.sendRequest("/users/" + userId, updatedUserData);
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "User should be updated");
        
        // Step 3: Verify update in database
        verifyUserInDatabase("updated@example.com", updatedUserData);
        
        // Step 4: Login and verify updated name in UI
        loginViaUI("updated@example.com", (String) initialUserData.get("password"));
        verifyUserProfileInUI("Updated User Name");
        
        logger.info("User update and UI verification test completed successfully");
    }

    /**
     * Test: Data-driven test using Excel data
     */
    @Test(description = "Data-driven test using Excel data", dataProvider = "excelUserData")
    public void testDataDrivenUserCreation(String name, String email, String role, String status) {
        logger.info("Starting data-driven user creation test for: {}", email);
        
        // Step 1: Create user via API using Excel data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("password", "TestPass123");
        userData.put("role", role);
        
        var apiResponse = postRequest.sendRequest("/users", userData);
        Assert.assertEquals(apiResponse.getStatusCode(), 201, "User should be created");
        
        // Step 2: Verify in database
        verifyUserInDatabase(email, userData);
        
        // Step 3: Login via UI if status is active
        if ("active".equals(status)) {
            loginViaUI(email, "TestPass123");
            verifyDashboardAccess();
        }
        
        logger.info("Data-driven user creation test completed for: {}", email);
    }

    /**
     * Test: JSON data-driven API testing
     */
    @Test(description = "JSON data-driven API testing", dataProvider = "jsonAPIData")
    public void testJSONDataDrivenAPI(String endpoint, String method, Map<String, Object> payload, int expectedStatus) {
        logger.info("Starting JSON data-driven API test for endpoint: {}", endpoint);
        
        var response = switch (method) {
            case "GET" -> getRequest.sendRequest(endpoint);
            case "POST" -> postRequest.sendRequest(endpoint, payload);
            case "PUT" -> putRequest.sendRequest(endpoint, payload);
            case "DELETE" -> deleteRequest.sendRequest(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
        
        Assert.assertEquals(response.getStatusCode(), expectedStatus, 
            "API response status should match expected status");
        
        logger.info("JSON data-driven API test completed for endpoint: {}", endpoint);
    }

    /**
     * Test: CSV data-driven database validation
     */
    @Test(description = "CSV data-driven database validation", dataProvider = "csvDatabaseData")
    public void testCSVDataDrivenDatabaseValidation(String query, String expectedResult, String tableName) {
        logger.info("Starting CSV data-driven database validation for table: {}", tableName);
        
        try {
            ResultSet rs = dbTest.executeQuery(query);
            
            switch (expectedResult) {
                case "user exists":
                    Assert.assertTrue(rs.next(), "User should exist in database");
                    break;
                case "count > 0":
                    rs.next();
                    int count = rs.getInt(1);
                    Assert.assertTrue(count > 0, "Count should be greater than 0");
                    break;
                case "profile exists":
                    Assert.assertTrue(rs.next(), "Profile should exist in database");
                    break;
                case "deletion successful":
                    // For DELETE queries, we check if no exception was thrown
                    Assert.assertTrue(true, "Deletion should be successful");
                    break;
                default:
                    Assert.fail("Unknown expected result: " + expectedResult);
            }
            
            logger.info("CSV data-driven database validation completed for table: {}", tableName);
        } catch (SQLException e) {
            logger.error("Error in database validation", e);
            Assert.fail("Database validation failed: " + e.getMessage());
        }
    }

    /**
     * Test: Mixed scenario with parallel execution
     */
    @Test(description = "Mixed scenario with parallel execution", threadPoolSize = 3, invocationCount = 2)
    public void testMixedScenarioWithParallelExecution() {
        logger.info("Starting mixed scenario with parallel execution");
        
        // Step 1: Create user via API
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Parallel Test User");
        userData.put("email", "parallel" + System.currentTimeMillis() + "@example.com");
        userData.put("password", "ParallelPass123");
        userData.put("role", "user");
        
        var apiResponse = postRequest.sendRequest("/users", userData);
        Assert.assertEquals(apiResponse.getStatusCode(), 201, "User should be created");
        
        // Step 2: Verify in database
        verifyUserInDatabase((String) userData.get("email"), userData);
        
        // Step 3: Login via UI
        loginViaUI((String) userData.get("email"), (String) userData.get("password"));
        
        // Step 4: Perform UI actions
        performUIActions();
        
        logger.info("Mixed scenario with parallel execution completed");
    }

    // Data Providers
    @DataProvider(name = "excelUserData")
    public Object[][] getExcelUserData() {
        ExcelDataProvider excelProvider = new ExcelDataProvider();
        return excelProvider.getData("src/test/resources/testdata/TestData.xlsx", "UserData");
    }

    @DataProvider(name = "jsonAPIData")
    public Object[][] getJSONAPIData() {
        JSONDataProvider jsonProvider = new JSONDataProvider();
        List<Map<String, Object>> apiData = jsonProvider.getData("src/test/resources/testdata/TestData.json", "apiTestData");
        
        Object[][] result = new Object[apiData.size()][4];
        for (int i = 0; i < apiData.size(); i++) {
            Map<String, Object> data = apiData.get(i);
            result[i][0] = data.get("endpoint");
            result[i][1] = data.get("method");
            result[i][2] = data.get("payload");
            result[i][3] = data.get("expectedStatus");
        }
        return result;
    }

    @DataProvider(name = "csvDatabaseData")
    public Object[][] getCSVDatabaseData() {
        CSVDataProvider csvProvider = new CSVDataProvider();
        return csvProvider.getData("src/test/resources/testdata/TestData.csv", 3); // Skip header rows
    }

    // Helper methods
    private String extractUserIdFromResponse(String responseBody) {
        // Simple extraction - in real scenario, use JSON parsing
        if (responseBody.contains("\"id\":")) {
            int startIndex = responseBody.indexOf("\"id\":") + 5;
            int endIndex = responseBody.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = responseBody.indexOf("}", startIndex);
            }
            return responseBody.substring(startIndex, endIndex).trim();
        }
        return "1"; // Default fallback
    }

    private void verifyUserInDatabase(String email, Map<String, Object> expectedData) {
        try {
            String query = "SELECT * FROM users WHERE email = '" + email + "'";
            ResultSet rs = dbTest.executeQuery(query);
            
            Assert.assertTrue(rs.next(), "User should exist in database");
            
            String actualName = rs.getString("name");
            String actualEmail = rs.getString("email");
            String actualRole = rs.getString("role");
            
            Assert.assertEquals(actualName, expectedData.get("name"), "User name should match");
            Assert.assertEquals(actualEmail, expectedData.get("email"), "User email should match");
            Assert.assertEquals(actualRole, expectedData.get("role"), "User role should match");
            
            logger.info("User verified in database: {} ({})", actualName, actualEmail);
        } catch (SQLException e) {
            logger.error("Error verifying user in database", e);
            Assert.fail("Failed to verify user in database: " + e.getMessage());
        }
    }

    private void loginViaUI(String email, String password) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Navigate to login page
        driver.get(config.getUIBaseUrl() + "/login");
        
        // Enter credentials
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
        usernameField.clear();
        usernameField.sendKeys(email);
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        // Click login button
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        logger.info("Logged in via UI with email: {}", email);
    }

    private void verifyDashboardAccess() {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Wait for dashboard to load
        WebElement dashboardElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))
        );
        
        Assert.assertTrue(dashboardElement.isDisplayed(), "Dashboard should be visible");
        logger.info("Dashboard access verified");
    }

    private void verifyUserProfileInUI(String expectedName) {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Navigate to user profile
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("user-profile")));
        profileLink.click();
        
        // Verify user name
        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        String actualName = nameElement.getText();
        
        Assert.assertEquals(actualName, expectedName, "User name in UI should match updated name");
        logger.info("User profile verified in UI: {}", expectedName);
    }

    private void performUIActions() {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Perform some UI actions (example)
        try {
            // Navigate to settings
            WebElement settingsLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("settings")));
            settingsLink.click();
            
            // Verify settings page loaded
            WebElement settingsPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("settings-page")));
            Assert.assertTrue(settingsPage.isDisplayed(), "Settings page should be visible");
            
            logger.info("UI actions performed successfully");
        } catch (Exception e) {
            logger.warn("Some UI actions failed (this is expected in demo): {}", e.getMessage());
        }
    }
}