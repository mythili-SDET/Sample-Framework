package com.testframework.steps;

import com.testframework.api.ApiManager;
import com.testframework.core.CucumberBaseTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Step Definitions for API Testing Scenarios
 * Implements BDD steps for JSONPlaceholder API testing
 */
public class API_StepDefinitions extends CucumberBaseTest {
    
    private ApiManager.ApiResponse response;
    private String endpoint;
    private Map<String, Object> requestBody;
    private int expectedStatusCode;
    
    @Given("I have access to the JSONPlaceholder API")
    public void i_have_access_to_the_jsonplaceholder_api() {
        logStep("Initializing API manager for JSONPlaceholder");
        apiManager = new ApiManager();
        logVerification("API manager initialized successfully");
    }
    
    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        this.endpoint = endpoint;
        logStep("Sending GET request to: " + endpoint);
        response = apiManager.simpleGet(endpoint);
        logData("Endpoint", endpoint);
        logData("Response Status", String.valueOf(response.getStatusCode()));
    }
    
    @When("I send a POST request to {string} with the following data:")
    public void i_send_a_post_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
        this.endpoint = endpoint;
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = data.get(0);
        
        requestBody = new HashMap<>();
        requestBody.put("title", row.get("title"));
        requestBody.put("body", row.get("body"));
        requestBody.put("userId", Integer.parseInt(row.get("userId")));
        
        logStep("Sending POST request to: " + endpoint);
        logData("Request Body", requestBody.toString());
        
        response = apiManager.simplePost(endpoint, requestBody);
        logData("Response Status", String.valueOf(response.getStatusCode()));
    }
    
    @When("I send a PUT request to {string} with the following data:")
    public void i_send_a_put_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
        this.endpoint = endpoint;
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = data.get(0);
        
        requestBody = new HashMap<>();
        requestBody.put("id", Integer.parseInt(endpoint.split("/")[endpoint.split("/").length - 1]));
        requestBody.put("title", row.get("title"));
        requestBody.put("body", row.get("body"));
        requestBody.put("userId", Integer.parseInt(row.get("userId")));
        
        logStep("Sending PUT request to: " + endpoint);
        logData("Request Body", requestBody.toString());
        
        response = apiManager.simplePut(endpoint, requestBody);
        logData("Response Status", String.valueOf(response.getStatusCode()));
    }
    
    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        this.endpoint = endpoint;
        logStep("Sending DELETE request to: " + endpoint);
        response = apiManager.simpleDelete(endpoint);
        logData("Response Status", String.valueOf(response.getStatusCode()));
    }
    
    @When("I send a GET request to {string} with custom headers")
    public void i_send_a_get_request_to_with_custom_headers(String endpoint) {
        this.endpoint = endpoint;
        logStep("Sending GET request with custom headers to: " + endpoint);
        
        apiManager.addHeader("Content-Type", "application/json");
        apiManager.addHeader("Accept", "application/json");
        apiManager.addHeader("User-Agent", "TestFramework/1.0");
        
        response = apiManager.simpleGet(endpoint);
        logData("Response Status", String.valueOf(response.getStatusCode()));
    }
    
    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
        logStep("Verifying response status code: " + expectedStatusCode);
        assertEqualsWithLog(response.getStatusCode(), expectedStatusCode, 
            "Response status should be: " + expectedStatusCode);
    }
    
    @Then("the response should contain posts data")
    public void the_response_should_contain_posts_data() {
        logStep("Verifying response contains posts data");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody != null, 
            "Response body should not be null");
        assertTrueWithLog(responseBody instanceof List, 
            "Response should be a list of posts");
    }
    
    @Then("the response should contain post with ID {int}")
    public void the_response_should_contain_post_with_id(int expectedId) {
        logStep("Verifying response contains post with ID: " + expectedId);
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody != null, 
            "Response body should not be null");
        assertEqualsWithLog(responseBody.get("id"), expectedId, 
            "Post ID should be: " + expectedId);
    }
    
    @Then("the post should have a title")
    public void the_post_should_have_a_title() {
        logStep("Verifying post has a title");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody.containsKey("title"), 
            "Post should have a title");
        assertTrueWithLog(responseBody.get("title") != null, 
            "Post title should not be null");
    }
    
    @Then("the post should have a body")
    public void the_post_should_have_a_body() {
        logStep("Verifying post has a body");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody.containsKey("body"), 
            "Post should have a body");
        assertTrueWithLog(responseBody.get("body") != null, 
            "Post body should not be null");
    }
    
    @Then("the response should contain the created post data")
    public void the_response_should_contain_the_created_post_data() {
        logStep("Verifying response contains created post data");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody != null, 
            "Response body should not be null");
        assertTrueWithLog(responseBody.containsKey("id"), 
            "Response should contain post ID");
        assertTrueWithLog(responseBody.containsKey("title"), 
            "Response should contain post title");
        assertTrueWithLog(responseBody.containsKey("body"), 
            "Response should contain post body");
    }
    
    @Then("the response should contain the updated post data")
    public void the_response_should_contain_the_updated_post_data() {
        logStep("Verifying response contains updated post data");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody != null, 
            "Response body should not be null");
        assertTrueWithLog(responseBody.containsKey("id"), 
            "Response should contain post ID");
        assertTrueWithLog(responseBody.containsKey("title"), 
            "Response should contain updated title");
        assertTrueWithLog(responseBody.containsKey("body"), 
            "Response should contain updated body");
    }
    
    @Then("the response time should be less than {int} seconds")
    public void the_response_time_should_be_less_than_seconds(int maxSeconds) {
        logStep("Verifying response time is less than " + maxSeconds + " seconds");
        long responseTimeMs = response.getResponseTime();
        long maxTimeMs = maxSeconds * 1000L;
        assertTrueWithLog(responseTimeMs < maxTimeMs, 
            "Response time should be less than " + maxSeconds + " seconds");
        logData("Response Time (ms)", String.valueOf(responseTimeMs));
    }
    
    @Then("the response should have JSON content type")
    public void the_response_should_have_json_content_type() {
        logStep("Verifying response has JSON content type");
        String contentType = response.getHeader("Content-Type");
        assertTrueWithLog(contentType != null && contentType.contains("application/json"), 
            "Response should have JSON content type");
        logData("Content-Type", contentType);
    }
    
    @Then("the response should contain post data")
    public void the_response_should_contain_post_data() {
        logStep("Verifying response contains post data");
        Map<String, Object> responseBody = response.getBodyAsMap();
        assertTrueWithLog(responseBody != null, 
            "Response body should not be null");
        assertTrueWithLog(responseBody.containsKey("id") || responseBody instanceof List, 
            "Response should contain post data");
    }
    
    @Then("the response should be successful")
    public void the_response_should_be_successful() {
        logStep("Verifying response is successful");
        assertTrueWithLog(response.isSuccess(), 
            "Response should be successful");
    }
    
    @Then("the response should indicate client error")
    public void the_response_should_indicate_client_error() {
        logStep("Verifying response indicates client error");
        assertTrueWithLog(response.isClientError(), 
            "Response should indicate client error");
    }
    
    @Then("the response should indicate server error")
    public void the_response_should_indicate_server_error() {
        logStep("Verifying response indicates server error");
        assertTrueWithLog(response.isServerError(), 
            "Response should indicate server error");
    }
    
    @Then("the response body should not be empty")
    public void the_response_body_should_not_be_empty() {
        logStep("Verifying response body is not empty");
        String responseBody = response.getBody();
        assertTrueWithLog(responseBody != null && !responseBody.isEmpty(), 
            "Response body should not be empty");
    }
    
    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedText) {
        logStep("Verifying response contains: " + expectedText);
        String responseBody = response.getBody();
        assertTrueWithLog(responseBody.contains(expectedText), 
            "Response should contain: " + expectedText);
    }
}