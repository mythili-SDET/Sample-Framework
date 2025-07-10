package com.automation.tests.api;

import com.automation.api.GETRequest;
import com.automation.api.POSTRequest;
import com.automation.api.PUTRequest;
import com.automation.api.DELETERequest;
import com.automation.utils.JSONDataProvider;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Test Suite
 * Demonstrates API automation capabilities
 */
public class APITestSuite {
    private static final Logger logger = LogManager.getLogger(APITestSuite.class);
    private GETRequest getRequest;
    private POSTRequest postRequest;
    private PUTRequest putRequest;
    private DELETERequest deleteRequest;

    @BeforeClass
    public void setUp() {
        logger.info("Setting up API test suite");
        getRequest = new GETRequest();
        postRequest = new POSTRequest();
        putRequest = new PUTRequest();
        deleteRequest = new DELETERequest();
    }

    /**
     * Test GET user by ID
     */
    @Test(description = "Test GET user by ID")
    public void testGetUserById() {
        logger.info("Starting GET user by ID test");
        
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", 1);
        
        Response response = getRequest.get("/users/{id}", pathParams);
        
        // Assert response
        getRequest.assertSuccess(response);
        getRequest.assertResponseBodyContains(response, "John Doe");
        getRequest.assertResponseContainsHeader(response, "Content-Type");
        
        // Log response details
        getRequest.logResponse(response);
        
        logger.info("Successfully completed GET user by ID test");
    }

    /**
     * Test GET all users
     */
    @Test(description = "Test GET all users")
    public void testGetAllUsers() {
        logger.info("Starting GET all users test");
        
        Response response = getRequest.get("/users");
        
        // Assert response
        getRequest.assertSuccess(response);
        getRequest.assertResponseTimeLessThan(response, 3000);
        
        // Verify response structure
        String responseBody = getRequest.getResponseBody(response);
        Assert.assertTrue(responseBody.contains("users"), "Response should contain users array");
        
        logger.info("Successfully completed GET all users test");
    }

    /**
     * Test GET users with query parameters
     */
    @Test(description = "Test GET users with query parameters")
    public void testGetUsersWithQueryParams() {
        logger.info("Starting GET users with query parameters test");
        
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);
        queryParams.put("limit", 10);
        queryParams.put("status", "active");
        
        Response response = getRequest.getWithQueryParams("/users", queryParams);
        
        // Assert response
        getRequest.assertSuccess(response);
        
        // Verify query parameters are reflected in response
        getRequest.assertResponseBodyContains(response, "page");
        getRequest.assertResponseBodyContains(response, "limit");
        
        logger.info("Successfully completed GET users with query parameters test");
    }

    /**
     * Test POST create user
     */
    @Test(description = "Test POST create user")
    public void testCreateUser() {
        logger.info("Starting POST create user test");
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Jane Smith");
        userData.put("email", "jane.smith@example.com");
        userData.put("role", "user");
        
        Response response = postRequest.post("/users", userData);
        
        // Assert response
        postRequest.assertCreated(response);
        postRequest.assertResponseBodyContains(response, "Jane Smith");
        postRequest.assertResponseContainsHeader(response, "Location");
        
        // Get created user ID from Location header
        String location = postRequest.getResponseHeader(response, "Location");
        Assert.assertNotNull(location, "Location header should be present");
        
        logger.info("Successfully completed POST create user test");
    }

    /**
     * Test POST create user with validation
     */
    @Test(description = "Test POST create user with validation")
    public void testCreateUserWithValidation() {
        logger.info("Starting POST create user with validation test");
        
        Map<String, Object> invalidUserData = new HashMap<>();
        invalidUserData.put("name", "");
        invalidUserData.put("email", "invalid-email");
        
        Response response = postRequest.post("/users", invalidUserData);
        
        // Assert validation errors
        postRequest.assertBadRequest(response);
        postRequest.assertResponseBodyContains(response, "validation");
        postRequest.assertResponseBodyContains(response, "email");
        
        logger.info("Successfully completed POST create user with validation test");
    }

    /**
     * Test PUT update user
     */
    @Test(description = "Test PUT update user")
    public void testUpdateUser() {
        logger.info("Starting PUT update user test");
        
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", 1);
        
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", "John Updated");
        updateData.put("email", "john.updated@example.com");
        
        Response response = putRequest.put("/users/{id}", updateData, pathParams);
        
        // Assert response
        putRequest.assertSuccess(response);
        putRequest.assertResponseBodyContains(response, "John Updated");
        
        logger.info("Successfully completed PUT update user test");
    }

    /**
     * Test DELETE user
     */
    @Test(description = "Test DELETE user")
    public void testDeleteUser() {
        logger.info("Starting DELETE user test");
        
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", 999); // Use a test user ID
        
        Response response = deleteRequest.delete("/users/{id}", pathParams);
        
        // Assert response
        deleteRequest.assertNoContent(response);
        
        // Verify user is deleted by trying to get it
        Response getResponse = getRequest.get("/users/{id}", pathParams);
        getRequest.assertNotFound(getResponse);
        
        logger.info("Successfully completed DELETE user test");
    }

    /**
     * Test authentication
     */
    @Test(description = "Test API authentication")
    public void testAuthentication() {
        logger.info("Starting authentication test");
        
        // Test with valid token
        Response validResponse = getRequest.getWithAuth("/protected/users", "valid-token-here");
        getRequest.assertSuccess(validResponse);
        
        // Test with invalid token
        Response invalidResponse = getRequest.getWithAuth("/protected/users", "invalid-token");
        getRequest.assertUnauthorized(invalidResponse);
        
        // Test without token
        Response noTokenResponse = getRequest.get("/protected/users");
        getRequest.assertUnauthorized(noTokenResponse);
        
        logger.info("Successfully completed authentication test");
    }

    /**
     * Test rate limiting
     */
    @Test(description = "Test API rate limiting")
    public void testRateLimiting() {
        logger.info("Starting rate limiting test");
        
        // Make multiple requests quickly
        for (int i = 0; i < 5; i++) {
            Response response = getRequest.get("/users");
            
            if (i < 3) {
                // First 3 requests should succeed
                getRequest.assertSuccess(response);
            } else {
                // Subsequent requests might be rate limited
                int statusCode = response.getStatusCode();
                Assert.assertTrue(statusCode == 200 || statusCode == 429, 
                               "Response should be either success or rate limited");
            }
        }
        
        logger.info("Successfully completed rate limiting test");
    }

    /**
     * Test response headers
     */
    @Test(description = "Test API response headers")
    public void testResponseHeaders() {
        logger.info("Starting response headers test");
        
        Response response = getRequest.get("/users");
        
        // Verify common headers
        getRequest.assertResponseContainsHeader(response, "Content-Type");
        getRequest.assertResponseContainsHeader(response, "Cache-Control");
        getRequest.assertResponseHeaderValue(response, "Content-Type", "application/json");
        
        logger.info("Successfully completed response headers test");
    }

    /**
     * Test error handling
     */
    @Test(description = "Test API error handling")
    public void testErrorHandling() {
        logger.info("Starting error handling test");
        
        // Test 404 - Resource not found
        Response notFoundResponse = getRequest.get("/nonexistent");
        getRequest.assertNotFound(notFoundResponse);
        
        // Test 400 - Bad request
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("invalid", "data");
        Response badRequestResponse = postRequest.post("/users", invalidData);
        postRequest.assertBadRequest(badRequestResponse);
        
        // Test 500 - Server error (if available)
        Response serverErrorResponse = getRequest.get("/error");
        if (serverErrorResponse.getStatusCode() == 500) {
            getRequest.assertServerError(serverErrorResponse);
        }
        
        logger.info("Successfully completed error handling test");
    }

    /**
     * Test data-driven API tests with JSON data
     */
    @Test(dataProvider = "userData", description = "Test data-driven API tests")
    public void testDataDrivenAPI(String name, String email, String expectedStatus) {
        logger.info("Testing API with name: {}, email: {}, expected status: {}", name, email, expectedStatus);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        
        Response response = postRequest.post("/users", userData);
        
        if ("success".equals(expectedStatus)) {
            postRequest.assertCreated(response);
        } else {
            postRequest.assertBadRequest(response);
        }
    }

    /**
     * Data provider for API tests
     */
    @DataProvider(name = "userData")
    public Object[][] getUserData() {
        List<Map<String, Object>> testData = JSONDataProvider.readTestData();
        
        Object[][] data = new Object[testData.size()][3];
        for (int i = 0; i < testData.size(); i++) {
            Map<String, Object> row = testData.get(i);
            data[i][0] = row.get("name");
            data[i][1] = row.get("email");
            data[i][2] = row.get("expectedStatus");
        }
        
        return data;
    }

    /**
     * Test API performance
     */
    @Test(description = "Test API performance and response times")
    public void testAPIPerformance() {
        logger.info("Starting API performance test");
        
        long totalTime = 0;
        int numberOfRequests = 10;
        
        for (int i = 0; i < numberOfRequests; i++) {
            long startTime = System.currentTimeMillis();
            Response response = getRequest.get("/users");
            long responseTime = System.currentTimeMillis() - startTime;
            
            totalTime += responseTime;
            getRequest.assertSuccess(response);
            
            logger.info("Request {} took {} ms", i + 1, responseTime);
        }
        
        double averageTime = (double) totalTime / numberOfRequests;
        logger.info("Average response time: {} ms", averageTime);
        
        // Assert average response time is acceptable (2 seconds)
        Assert.assertTrue(averageTime < 2000, "Average response time should be less than 2 seconds");
        
        logger.info("Successfully completed API performance test");
    }

    /**
     * Test API versioning
     */
    @Test(description = "Test API versioning")
    public void testAPIVersioning() {
        logger.info("Starting API versioning test");
        
        // Test v1 API
        Response v1Response = getRequest.get("/v1/users");
        getRequest.assertSuccess(v1Response);
        
        // Test v2 API
        Response v2Response = getRequest.get("/v2/users");
        getRequest.assertSuccess(v2Response);
        
        // Verify different response structures
        String v1Body = getRequest.getResponseBody(v1Response);
        String v2Body = getRequest.getResponseBody(v2Response);
        
        Assert.assertNotEquals(v1Body, v2Body, "V1 and V2 APIs should have different responses");
        
        logger.info("Successfully completed API versioning test");
    }

    /**
     * Test API pagination
     */
    @Test(description = "Test API pagination")
    public void testAPIPagination() {
        logger.info("Starting API pagination test");
        
        Map<String, Object> page1Params = new HashMap<>();
        page1Params.put("page", 1);
        page1Params.put("limit", 5);
        
        Response page1Response = getRequest.getWithQueryParams("/users", page1Params);
        getRequest.assertSuccess(page1Response);
        
        Map<String, Object> page2Params = new HashMap<>();
        page2Params.put("page", 2);
        page2Params.put("limit", 5);
        
        Response page2Response = getRequest.getWithQueryParams("/users", page2Params);
        getRequest.assertSuccess(page2Response);
        
        // Verify different pages have different data
        String page1Body = getRequest.getResponseBody(page1Response);
        String page2Body = getRequest.getResponseBody(page2Response);
        
        Assert.assertNotEquals(page1Body, page2Body, "Page 1 and Page 2 should have different data");
        
        logger.info("Successfully completed API pagination test");
    }
}