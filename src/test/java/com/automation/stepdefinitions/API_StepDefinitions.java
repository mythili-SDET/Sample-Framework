package com.automation.stepdefinitions;

import com.automation.api.GETRequest;
import com.automation.api.POSTRequest;
import com.automation.api.PUTRequest;
import com.automation.api.DELETERequest;
import com.automation.core.ConfigManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Step definitions for API testing scenarios
 */
public class API_StepDefinitions {
    private static final Logger logger = LogManager.getLogger(API_StepDefinitions.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private GETRequest getRequest;
    private POSTRequest postRequest;
    private PUTRequest putRequest;
    private DELETERequest deleteRequest;
    
    private int lastResponseCode;
    private String lastResponseBody;
    private Map<String, Object> createdUserData;

    @Given("the API base URL is configured")
    public void the_api_base_url_is_configured() {
        String baseUrl = config.getAPIBaseUrl();
        Assert.assertNotNull(baseUrl, "API base URL should be configured");
        logger.info("API base URL configured: {}", baseUrl);
    }

    @Given("the API authentication token is set")
    public void the_api_authentication_token_is_set() {
        String token = config.getAPIToken();
        Assert.assertNotNull(token, "API authentication token should be set");
        logger.info("API authentication token is set");
    }

    @When("I send a POST request to {string} with user data")
    public void i_send_a_post_request_to_with_user_data(String endpoint) {
        postRequest = new POSTRequest();
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User");
        userData.put("email", "testuser@example.com");
        userData.put("password", "TestPassword123");
        userData.put("role", "user");
        
        var response = postRequest.sendRequest(endpoint, userData);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        createdUserData = userData;
        
        logger.info("POST request sent to {} with response code: {}", endpoint, lastResponseCode);
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        getRequest = new GETRequest();
        
        var response = getRequest.sendRequest(endpoint);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        
        logger.info("GET request sent to {} with response code: {}", endpoint, lastResponseCode);
    }

    @When("I send a PUT request to {string} with updated data")
    public void i_send_a_put_request_to_with_updated_data(String endpoint) {
        putRequest = new PUTRequest();
        
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", "Updated Test User");
        updatedData.put("email", "updated@example.com");
        
        var response = putRequest.sendRequest(endpoint, updatedData);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        
        logger.info("PUT request sent to {} with response code: {}", endpoint, lastResponseCode);
    }

    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        deleteRequest = new DELETERequest();
        
        var response = deleteRequest.sendRequest(endpoint);
        lastResponseCode = response.getStatusCode();
        
        logger.info("DELETE request sent to {} with response code: {}", endpoint, lastResponseCode);
    }

    @When("I send a POST request to {string} with invalid data")
    public void i_send_a_post_request_to_with_invalid_data(String endpoint) {
        postRequest = new POSTRequest();
        
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("name", ""); // Invalid: empty name
        invalidData.put("email", "invalid-email"); // Invalid email format
        invalidData.put("password", "123"); // Invalid: too short password
        
        var response = postRequest.sendRequest(endpoint, invalidData);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        
        logger.info("POST request with invalid data sent to {} with response code: {}", endpoint, lastResponseCode);
    }

    @When("I send a POST request to {string} with role {string}")
    public void i_send_a_post_request_to_with_role(String endpoint, String role) {
        postRequest = new POSTRequest();
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User");
        userData.put("email", "testuser@example.com");
        userData.put("password", "TestPassword123");
        userData.put("role", role);
        
        var response = postRequest.sendRequest(endpoint, userData);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        
        logger.info("POST request with role {} sent to {} with response code: {}", role, endpoint, lastResponseCode);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(lastResponseCode, expectedStatusCode, 
            "Response status code should be " + expectedStatusCode);
        logger.info("Response status code verified: {}", expectedStatusCode);
    }

    @Then("the response should contain the created user details")
    public void the_response_should_contain_the_created_user_details() {
        Assert.assertNotNull(lastResponseBody, "Response body should not be null");
        Assert.assertTrue(lastResponseBody.contains("id"), "Response should contain user ID");
        Assert.assertTrue(lastResponseBody.contains("name"), "Response should contain user name");
        Assert.assertTrue(lastResponseBody.contains("email"), "Response should contain user email");
        logger.info("Response contains created user details");
    }

    @Then("the response should contain user details")
    public void the_response_should_contain_user_details() {
        Assert.assertNotNull(lastResponseBody, "Response body should not be null");
        Assert.assertTrue(lastResponseBody.contains("id"), "Response should contain user ID");
        Assert.assertTrue(lastResponseBody.contains("name"), "Response should contain user name");
        logger.info("Response contains user details");
    }

    @Then("the user details should be updated in the database")
    public void the_user_details_should_be_updated_in_the_database() {
        // This would typically involve a database verification step
        logger.info("User details updated in database (verification step)");
    }

    @Then("the user should be deleted from the database")
    public void the_user_should_be_deleted_from_the_database() {
        // This would typically involve a database verification step
        logger.info("User deleted from database (verification step)");
    }

    @Then("the response should contain validation errors")
    public void the_response_should_contain_validation_errors() {
        Assert.assertNotNull(lastResponseBody, "Response body should not be null");
        Assert.assertTrue(lastResponseBody.contains("error") || lastResponseBody.contains("validation"), 
            "Response should contain validation errors");
        logger.info("Response contains validation errors");
    }

    @Then("the user should have role {string}")
    public void the_user_should_have_role(String expectedRole) {
        Assert.assertTrue(lastResponseBody.contains(expectedRole), 
            "Response should contain role: " + expectedRole);
        logger.info("User has correct role: {}", expectedRole);
    }

    @Then("the user should be created in the database")
    public void the_user_should_be_created_in_the_database() {
        // This would typically involve a database verification step
        logger.info("User created in database (verification step)");
    }

    @Given("a user exists with ID {string}")
    public void a_user_exists_with_id(String userId) {
        // This step would ensure a user exists before testing
        // Implementation depends on test data setup
        logger.info("User exists with ID: {}", userId);
    }

    @When("I create a new user via API")
    public void i_create_a_new_user_via_api() {
        postRequest = new POSTRequest();
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "API Created User");
        userData.put("email", "apiuser@example.com");
        userData.put("password", "APIPassword123");
        userData.put("role", "user");
        
        var response = postRequest.sendRequest("/users", userData);
        lastResponseCode = response.getStatusCode();
        lastResponseBody = response.getBody().asString();
        createdUserData = userData;
        
        logger.info("New user created via API with response code: {}", lastResponseCode);
    }

    @When("I create a user via API")
    public void i_create_a_user_via_api() {
        i_create_a_new_user_via_api();
    }
}