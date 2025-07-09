package com.testframework.tests;

import com.testframework.api.ApiManager;
import com.testframework.core.BaseTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Test Class demonstrating API testing capabilities
 * Tests JSONPlaceholder API endpoints
 */
public class ApiTest extends BaseTest {
    
    @Test(description = "Test GET request to fetch all posts")
    public void testGetAllPosts() {
        logInfo("Starting GET all posts test");
        
        // Perform GET request
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Verify response body
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrue(responseBody != null, "Response body should not be null");
        
        logInfo("Response status: " + response.getStatusCode());
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("GET all posts test completed successfully");
    }
    
    @Test(description = "Test GET request to fetch specific post")
    public void testGetSpecificPost() {
        logInfo("Starting GET specific post test");
        
        // Perform GET request for post with ID 1
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts/1");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Verify response body
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrue(responseBody != null, "Response body should not be null");
        assertEquals(responseBody.get("id"), 1, "Post ID should be 1");
        assertTrue(responseBody.containsKey("title"), "Response should contain title");
        assertTrue(responseBody.containsKey("body"), "Response should contain body");
        
        logInfo("Post title: " + responseBody.get("title"));
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("GET specific post test completed successfully");
    }
    
    @Test(description = "Test POST request to create new post")
    public void testCreatePost() {
        logInfo("Starting POST create post test");
        
        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Test Post Title");
        requestBody.put("body", "Test Post Body");
        requestBody.put("userId", 1);
        
        // Perform POST request
        ApiManager.ApiResponse response = apiManager.simplePost("/posts", requestBody);
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        
        // Verify response body
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrue(responseBody != null, "Response body should not be null");
        assertTrue(responseBody.containsKey("id"), "Response should contain ID");
        assertEquals(responseBody.get("title"), "Test Post Title", "Title should match");
        assertEquals(responseBody.get("body"), "Test Post Body", "Body should match");
        
        logInfo("Created post ID: " + responseBody.get("id"));
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("POST create post test completed successfully");
    }
    
    @Test(description = "Test PUT request to update post")
    public void testUpdatePost() {
        logInfo("Starting PUT update post test");
        
        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("title", "Updated Post Title");
        requestBody.put("body", "Updated Post Body");
        requestBody.put("userId", 1);
        
        // Perform PUT request
        ApiManager.ApiResponse response = apiManager.simplePut("/posts/1", requestBody);
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Verify response body
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrue(responseBody != null, "Response body should not be null");
        assertEquals(responseBody.get("title"), "Updated Post Title", "Title should be updated");
        assertEquals(responseBody.get("body"), "Updated Post Body", "Body should be updated");
        
        logInfo("Updated post ID: " + responseBody.get("id"));
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("PUT update post test completed successfully");
    }
    
    @Test(description = "Test DELETE request to delete post")
    public void testDeletePost() {
        logInfo("Starting DELETE post test");
        
        // Perform DELETE request
        ApiManager.ApiResponse response = apiManager.simpleDelete("/posts/1");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("DELETE post test completed successfully");
    }
    
    @Test(description = "Test API with custom headers")
    public void testApiWithCustomHeaders() {
        logInfo("Starting API test with custom headers");
        
        // Add custom headers
        apiManager.addHeader("Content-Type", "application/json");
        apiManager.addHeader("Accept", "application/json");
        apiManager.addHeader("User-Agent", "TestFramework/1.0");
        
        // Perform GET request
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Verify response headers
        String contentType = response.getHeader("Content-Type");
        assertTrue(contentType != null && contentType.contains("application/json"), 
                  "Response should have JSON content type");
        
        logInfo("Content-Type header: " + contentType);
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("API test with custom headers completed successfully");
    }
    
    @Test(description = "Test API with query parameters")
    public void testApiWithQueryParameters() {
        logInfo("Starting API test with query parameters");
        
        // Add query parameters
        apiManager.addQueryParam("userId", "1");
        
        // Perform GET request
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("API test with query parameters completed successfully");
    }
    
    @Test(description = "Test API response time")
    public void testApiResponseTime() {
        logInfo("Starting API response time test");
        
        // Perform GET request
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts");
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        
        // Verify response time is reasonable (less than 5 seconds)
        long responseTime = response.getResponseTime();
        assertTrue(responseTime < 5000, "Response time should be less than 5 seconds");
        
        logInfo("Response time: " + responseTime + "ms");
        
        logSuccess("API response time test completed successfully");
    }
    
    @Test(description = "Test API error handling")
    public void testApiErrorHandling() {
        logInfo("Starting API error handling test");
        
        // Try to access non-existent resource
        ApiManager.ApiResponse response = apiManager.simpleGet("/posts/999999");
        
        // Verify response indicates error
        assertTrue(response.isClientError(), "Response should indicate client error");
        assertEquals(response.getStatusCode(), 404, "Status code should be 404");
        
        logInfo("Error status code: " + response.getStatusCode());
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("API error handling test completed successfully");
    }
    
    @Test(description = "Test API with JSON data from file")
    public void testApiWithJsonData() {
        logInfo("Starting API test with JSON data from file");
        
        // Get test data from JSON file
        List<Map<String, Object>> testData = jsonDataProvider.readJsonData();
        
        if (testData.isEmpty()) {
            logWarning("No test data found in JSON file");
            return;
        }
        
        // Use first row of data
        Map<String, Object> data = testData.get(0);
        String title = (String) data.get("title");
        String body = (String) data.get("body");
        
        logInfo("Using title from JSON: " + title);
        
        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("body", body);
        requestBody.put("userId", 1);
        
        // Perform POST request
        ApiManager.ApiResponse response = apiManager.simplePost("/posts", requestBody);
        
        // Verify response
        assertTrue(response.isSuccess(), "Response should be successful");
        assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        
        logInfo("Created post with title: " + title);
        logInfo("Response time: " + response.getResponseTime() + "ms");
        
        logSuccess("API test with JSON data completed successfully");
    }
}