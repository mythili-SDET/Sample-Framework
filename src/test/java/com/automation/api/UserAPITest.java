package com.automation.api;

import com.automation.api.APIBaseTest;
import com.automation.utils.CSVDataReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Sample API test class demonstrating user management API testing
 * Shows data-driven API testing using CSV data
 */
public class UserAPITest extends APIBaseTest {

    private String createdUserId;
    
    @Test(groups = {"smoke", "api"}, description = "Test GET all users endpoint")
    public void testGetAllUsers() {
        logger.info("Starting GET all users test");
        
        Response response = performGet("/users");
        
        // Validate response
        validateStatusCode(response, 200);
        Assert.assertNotNull(response.getBody(), "Response body should not be null");
        
        // Validate response structure
        Assert.assertTrue(response.jsonPath().getList("$").size() >= 0, 
                         "Users list should be returned");
        
        logger.info("GET all users test completed successfully");
    }

    @Test(groups = {"smoke", "api"}, description = "Test GET user by ID endpoint")
    public void testGetUserById() {
        logger.info("Starting GET user by ID test");
        
        // First create a user to get
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User");
        userData.put("email", "testuser@example.com");
        userData.put("age", 25);
        userData.put("role", "user");
        userData.put("status", "active");
        
        Response createResponse = performPost("/users", userData);
        validateStatusCode(createResponse, 201);
        
        String userId = extractFromResponse(createResponse, "id");
        Assert.assertNotNull(userId, "Created user should have an ID");
        
        // Now get the user by ID
        Response getResponse = performGet("/users/" + userId);
        
        // Validate response
        validateStatusCode(getResponse, 200);
        Assert.assertEquals(extractFromResponse(getResponse, "name"), "Test User");
        Assert.assertEquals(extractFromResponse(getResponse, "email"), "testuser@example.com");
        
        logger.info("GET user by ID test completed successfully");
    }

    @Test(groups = {"regression", "api"}, description = "Test create user with valid data")
    public void testCreateValidUser() {
        logger.info("Starting create valid user test");
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John Doe");
        userData.put("email", "john.doe@example.com");
        userData.put("age", 30);
        userData.put("role", "admin");
        userData.put("status", "active");
        
        Response response = performPost("/users", userData);
        
        // Validate response
        validateStatusCode(response, 201);
        
        // Store user ID for cleanup
        createdUserId = extractFromResponse(response, "id");
        Assert.assertNotNull(createdUserId, "Created user should have an ID");
        
        // Validate created user data
        Assert.assertEquals(extractFromResponse(response, "name"), "John Doe");
        Assert.assertEquals(extractFromResponse(response, "email"), "john.doe@example.com");
        Assert.assertEquals(extractFromResponse(response, "role"), "admin");
        
        logger.info("Create valid user test completed successfully");
    }

    @Test(dataProvider = "userApiData", groups = {"regression", "api"}, 
          description = "Data-driven user creation test using CSV data")
    public void testDataDrivenUserCreation(Map<String, String> testData) {
        logger.info("Starting data-driven user creation test: {}", testData.get("test_description"));
        
        // Prepare request data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", testData.get("name"));
        userData.put("email", testData.get("email"));
        
        // Handle age conversion
        String ageStr = testData.get("age");
        if (ageStr != null && !ageStr.isEmpty()) {
            try {
                userData.put("age", Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                userData.put("age", ageStr); // Keep as string for validation testing
            }
        }
        
        userData.put("role", testData.get("role"));
        userData.put("status", testData.get("status"));
        
        // Perform API call
        Response response = performPost("/users", userData);
        
        // Validate response based on expected status
        int expectedStatus = Integer.parseInt(testData.get("expected_status"));
        validateStatusCode(response, expectedStatus);
        
        if (expectedStatus == 201) {
            // Successful creation - validate user data
            Assert.assertNotNull(extractFromResponse(response, "id"), 
                               "Created user should have an ID: " + testData.get("test_description"));
            
            if (testData.get("name") != null && !testData.get("name").isEmpty()) {
                Assert.assertEquals(extractFromResponse(response, "name"), testData.get("name"));
            }
            
        } else {
            // Failed creation - validate error response
            Assert.assertNotNull(response.getBody(), 
                               "Error response should have a body: " + testData.get("test_description"));
        }
        
        logger.info("Data-driven user creation test completed: {}", testData.get("test_description"));
    }

    @Test(groups = {"api"}, description = "Test update user endpoint")
    public void testUpdateUser() {
        logger.info("Starting update user test");
        
        // First create a user
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Original User");
        userData.put("email", "original@example.com");
        userData.put("age", 25);
        userData.put("role", "user");
        userData.put("status", "active");
        
        Response createResponse = performPost("/users", userData);
        validateStatusCode(createResponse, 201);
        String userId = extractFromResponse(createResponse, "id");
        
        // Update the user
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", "Updated User");
        updateData.put("age", 30);
        updateData.put("status", "inactive");
        
        Response updateResponse = performPut("/users/" + userId, updateData);
        
        // Validate update response
        validateStatusCode(updateResponse, 200);
        Assert.assertEquals(extractFromResponse(updateResponse, "name"), "Updated User");
        Assert.assertEquals(extractFromResponse(updateResponse, "age"), 30);
        Assert.assertEquals(extractFromResponse(updateResponse, "status"), "inactive");
        
        logger.info("Update user test completed successfully");
    }

    @Test(groups = {"api"}, description = "Test delete user endpoint")
    public void testDeleteUser() {
        logger.info("Starting delete user test");
        
        // First create a user
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "User To Delete");
        userData.put("email", "delete@example.com");
        userData.put("age", 25);
        userData.put("role", "user");
        userData.put("status", "active");
        
        Response createResponse = performPost("/users", userData);
        validateStatusCode(createResponse, 201);
        String userId = extractFromResponse(createResponse, "id");
        
        // Delete the user
        Response deleteResponse = performDelete("/users/" + userId);
        validateStatusCode(deleteResponse, 204);
        
        // Verify user is deleted by trying to get it
        Response getResponse = performGet("/users/" + userId);
        validateStatusCode(getResponse, 404);
        
        logger.info("Delete user test completed successfully");
    }

    @Test(groups = {"api"}, description = "Test user authentication endpoint")
    public void testUserAuthentication() {
        logger.info("Starting user authentication test");
        
        // Create authentication request
        Map<String, Object> authData = new HashMap<>();
        authData.put("email", "admin@example.com");
        authData.put("password", "admin123");
        
        Response response = performPost("/auth/login", authData);
        
        // Validate authentication response
        validateStatusCode(response, 200);
        
        String token = extractFromResponse(response, "token");
        Assert.assertNotNull(token, "Authentication should return a token");
        Assert.assertTrue(token.length() > 0, "Token should not be empty");
        
        // Test using the token for authenticated requests
        addBearerToken(token);
        
        Response profileResponse = performGet("/auth/profile");
        validateStatusCode(profileResponse, 200);
        
        logger.info("User authentication test completed successfully");
    }

    @Test(groups = {"api"}, description = "Test API response time")
    public void testAPIResponseTime() {
        logger.info("Starting API response time test");
        
        Response response = performGet("/users");
        validateStatusCode(response, 200);
        
        long responseTime = getResponseTime(response);
        Assert.assertTrue(responseTime < 5000, 
                         "API response time should be less than 5 seconds. Actual: " + responseTime + "ms");
        
        logger.info("API response time test completed. Response time: {}ms", responseTime);
    }

    @Test(groups = {"api"}, description = "Test API error handling")
    public void testAPIErrorHandling() {
        logger.info("Starting API error handling test");
        
        // Test invalid endpoint
        Response notFoundResponse = performGet("/invalid-endpoint");
        validateStatusCode(notFoundResponse, 404);
        
        // Test invalid user ID
        Response invalidUserResponse = performGet("/users/invalid-id");
        validateStatusCode(invalidUserResponse, 400);
        
        // Test invalid request body
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("invalid_field", "invalid_value");
        
        Response invalidBodyResponse = performPost("/users", invalidData);
        Assert.assertTrue(invalidBodyResponse.getStatusCode() >= 400, 
                         "Invalid request should return error status");
        
        logger.info("API error handling test completed successfully");
    }

    /**
     * Data provider for user API test data from CSV file
     */
    @DataProvider(name = "userApiData")
    public Object[][] getUserApiData() {
        String csvFilePath = config.getCsvDataPath() + "user_api_data.csv";
        return CSVDataReader.readCSVDataForTestNG(csvFilePath);
    }
}