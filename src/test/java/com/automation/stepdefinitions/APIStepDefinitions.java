package com.automation.stepdefinitions;

import com.automation.core.APIBaseTest;
import com.automation.hooks.TestContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * API Step Definitions for Cucumber scenarios
 * Implements steps for REST API testing using RestAssured
 */
public class APIStepDefinitions extends APIBaseTest {
    private static final Logger logger = LogManager.getLogger(APIStepDefinitions.class);
    
    private final TestContext testContext;
    private Response response;
    private Map<String, Object> requestBody;
    private Map<String, String> headers;
    private Map<String, Object> queryParams;
    private Map<String, Object> pathParams;

    public APIStepDefinitions(TestContext testContext) {
        this.testContext = testContext;
        this.requestBody = new HashMap<>();
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
    }

    // Request preparation steps
    @Given("I set the API base URL")
    public void i_set_the_api_base_url() {
        String apiBaseUrl = testContext.getApiBaseUrl();
        logger.info("API base URL set to: {}", apiBaseUrl);
    }

    @Given("I set the authentication token")
    public void i_set_the_authentication_token() {
        String token = testContext.getAuthToken();
        if (token != null) {
            headers.put("Authorization", "Bearer " + token);
            setHeaders(headers);
            logger.info("Authentication token set");
        } else {
            logger.warn("No authentication token available");
        }
    }

    @Given("I set header {string} to {string}")
    public void i_set_header_to(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
        setHeaders(headers);
        testContext.setTestData("header_" + headerName, headerValue);
        logger.info("Set header: {} = {}", headerName, headerValue);
    }

    @Given("I set query parameter {string} to {string}")
    public void i_set_query_parameter_to(String paramName, String paramValue) {
        queryParams.put(paramName, paramValue);
        setQueryParams(queryParams);
        testContext.setTestData("query_" + paramName, paramValue);
        logger.info("Set query parameter: {} = {}", paramName, paramValue);
    }

    @Given("I set path parameter {string} to {string}")
    public void i_set_path_parameter_to(String paramName, String paramValue) {
        pathParams.put(paramName, paramValue);
        setPathParams(pathParams);
        testContext.setTestData("path_" + paramName, paramValue);
        logger.info("Set path parameter: {} = {}", paramName, paramValue);
    }

    @Given("I set the request body:")
    public void i_set_the_request_body(String requestBodyJson) {
        setRequestBody(requestBodyJson);
        testContext.setTestData("requestBody", requestBodyJson);
        logger.info("Set request body: {}", requestBodyJson);
    }

    @Given("I set request body field {string} to {string}")
    public void i_set_request_body_field_to(String fieldName, String fieldValue) {
        requestBody.put(fieldName, fieldValue);
        setRequestBody(requestBody);
        testContext.setTestData("body_" + fieldName, fieldValue);
        logger.info("Set request body field: {} = {}", fieldName, fieldValue);
    }

    @Given("I set request body field {string} to number {int}")
    public void i_set_request_body_field_to_number(String fieldName, int fieldValue) {
        requestBody.put(fieldName, fieldValue);
        setRequestBody(requestBody);
        testContext.setTestData("body_" + fieldName, fieldValue);
        logger.info("Set request body field: {} = {}", fieldName, fieldValue);
    }

    @Given("I set request body field {string} to boolean {}")
    public void i_set_request_body_field_to_boolean(String fieldName, boolean fieldValue) {
        requestBody.put(fieldName, fieldValue);
        setRequestBody(requestBody);
        testContext.setTestData("body_" + fieldName, fieldValue);
        logger.info("Set request body field: {} = {}", fieldName, fieldValue);
    }

    // HTTP method steps
    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = get(endpoint);
        testContext.setTestData("lastResponse", response);
        testContext.setTestData("lastEndpoint", endpoint);
        testContext.setTestData("lastMethod", "GET");
        logger.info("Sent GET request to: {}", endpoint);
    }

    @When("I send a POST request to {string}")
    public void i_send_a_post_request_to(String endpoint) {
        if (requestBody.isEmpty()) {
            response = post(endpoint);
        } else {
            response = post(endpoint, requestBody);
        }
        testContext.setTestData("lastResponse", response);
        testContext.setTestData("lastEndpoint", endpoint);
        testContext.setTestData("lastMethod", "POST");
        logger.info("Sent POST request to: {}", endpoint);
    }

    @When("I send a PUT request to {string}")
    public void i_send_a_put_request_to(String endpoint) {
        response = put(endpoint, requestBody);
        testContext.setTestData("lastResponse", response);
        testContext.setTestData("lastEndpoint", endpoint);
        testContext.setTestData("lastMethod", "PUT");
        logger.info("Sent PUT request to: {}", endpoint);
    }

    @When("I send a PATCH request to {string}")
    public void i_send_a_patch_request_to(String endpoint) {
        response = patch(endpoint, requestBody);
        testContext.setTestData("lastResponse", response);
        testContext.setTestData("lastEndpoint", endpoint);
        testContext.setTestData("lastMethod", "PATCH");
        logger.info("Sent PATCH request to: {}", endpoint);
    }

    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        if (pathParams.isEmpty()) {
            response = delete(endpoint);
        } else {
            response = delete(endpoint, pathParams);
        }
        testContext.setTestData("lastResponse", response);
        testContext.setTestData("lastEndpoint", endpoint);
        testContext.setTestData("lastMethod", "DELETE");
        logger.info("Sent DELETE request to: {}", endpoint);
    }

    // Response validation steps
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        validateStatusCode(response, expectedStatusCode);
        testContext.setTestData("statusCode", response.getStatusCode());
        logger.info("Validated status code: {}", expectedStatusCode);
    }

    @Then("the response should contain field {string}")
    public void the_response_should_contain_field(String fieldName) {
        Object fieldValue = extractFromResponse(response, fieldName);
        Assert.assertNotNull(fieldValue, "Response does not contain field: " + fieldName);
        testContext.setTestData("extracted_" + fieldName, fieldValue);
        logger.info("Verified response contains field: {}", fieldName);
    }

    @Then("the response field {string} should be {string}")
    public void the_response_field_should_be(String fieldName, String expectedValue) {
        Object actualValue = extractFromResponse(response, fieldName);
        Assert.assertEquals(actualValue.toString(), expectedValue, 
            "Field " + fieldName + " value mismatch");
        testContext.setTestData("extracted_" + fieldName, actualValue);
        logger.info("Validated response field {} = {}", fieldName, expectedValue);
    }

    @Then("the response field {string} should be number {int}")
    public void the_response_field_should_be_number(String fieldName, int expectedValue) {
        Object actualValue = extractFromResponse(response, fieldName);
        Assert.assertEquals(((Number) actualValue).intValue(), expectedValue, 
            "Field " + fieldName + " value mismatch");
        testContext.setTestData("extracted_" + fieldName, actualValue);
        logger.info("Validated response field {} = {}", fieldName, expectedValue);
    }

    @Then("the response field {string} should be boolean {}")
    public void the_response_field_should_be_boolean(String fieldName, boolean expectedValue) {
        Object actualValue = extractFromResponse(response, fieldName);
        Assert.assertEquals((Boolean) actualValue, expectedValue, 
            "Field " + fieldName + " value mismatch");
        testContext.setTestData("extracted_" + fieldName, actualValue);
        logger.info("Validated response field {} = {}", fieldName, expectedValue);
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedText) {
        String responseBody = getResponseBody(response);
        Assert.assertTrue(responseBody.contains(expectedText), 
            "Response does not contain expected text: " + expectedText);
        logger.info("Verified response contains text: {}", expectedText);
    }

    @Then("the response header {string} should be {string}")
    public void the_response_header_should_be(String headerName, String expectedValue) {
        String actualValue = getResponseHeader(response, headerName);
        Assert.assertEquals(actualValue, expectedValue, 
            "Header " + headerName + " value mismatch");
        testContext.setTestData("response_header_" + headerName, actualValue);
        logger.info("Validated response header {} = {}", headerName, expectedValue);
    }

    @Then("the response time should be less than {int} milliseconds")
    public void the_response_time_should_be_less_than_milliseconds(int maxTime) {
        long responseTime = getResponseTime(response);
        Assert.assertTrue(responseTime < maxTime, 
            "Response time " + responseTime + "ms exceeds maximum " + maxTime + "ms");
        testContext.setTestData("responseTime", responseTime);
        logger.info("Validated response time: {}ms < {}ms", responseTime, maxTime);
    }

    @Then("the response body should not be empty")
    public void the_response_body_should_not_be_empty() {
        String responseBody = getResponseBody(response);
        Assert.assertFalse(responseBody.isEmpty(), "Response body is empty");
        logger.info("Verified response body is not empty");
    }

    @Then("the response should be valid JSON")
    public void the_response_should_be_valid_json() {
        try {
            response.jsonPath().prettify();
            logger.info("Verified response is valid JSON");
        } catch (Exception e) {
            Assert.fail("Response is not valid JSON: " + e.getMessage());
        }
    }

    // Data extraction and storage steps
    @When("I extract {string} from response and store as {string}")
    public void i_extract_from_response_and_store_as(String jsonPath, String variableName) {
        Object extractedValue = extractFromResponse(response, jsonPath);
        testContext.setTestData(variableName, extractedValue);
        testContext.setSessionData(variableName, extractedValue);
        logger.info("Extracted {} from response and stored as {}: {}", jsonPath, variableName, extractedValue);
    }

    @When("I use extracted {string} as path parameter {string}")
    public void i_use_extracted_as_path_parameter(String variableName, String paramName) {
        Object value = testContext.getTestData(variableName);
        if (value == null) {
            value = testContext.getSessionData(variableName);
        }
        Assert.assertNotNull(value, "Variable " + variableName + " not found");
        
        pathParams.put(paramName, value);
        setPathParams(pathParams);
        logger.info("Used extracted value {} as path parameter {}: {}", variableName, paramName, value);
    }

    @When("I use extracted {string} as header {string}")
    public void i_use_extracted_as_header(String variableName, String headerName) {
        Object value = testContext.getTestData(variableName);
        if (value == null) {
            value = testContext.getSessionData(variableName);
        }
        Assert.assertNotNull(value, "Variable " + variableName + " not found");
        
        headers.put(headerName, value.toString());
        setHeaders(headers);
        logger.info("Used extracted value {} as header {}: {}", variableName, headerName, value);
    }

    // Reset and cleanup steps
    @Given("I reset the request headers")
    public void i_reset_the_request_headers() {
        headers.clear();
        resetRequestSpec();
        logger.info("Reset request headers");
    }

    @Given("I reset the request body")
    public void i_reset_the_request_body() {
        requestBody.clear();
        logger.info("Reset request body");
    }

    @Given("I reset all request parameters")
    public void i_reset_all_request_parameters() {
        headers.clear();
        queryParams.clear();
        pathParams.clear();
        requestBody.clear();
        resetRequestSpec();
        logger.info("Reset all request parameters");
    }

    // Utility steps
    @Then("I print the response body")
    public void i_print_the_response_body() {
        String responseBody = getResponseBody(response);
        logger.info("Response body: {}", responseBody);
        System.out.println("Response body: " + responseBody);
    }

    @Then("I print the response headers")
    public void i_print_the_response_headers() {
        logger.info("Response headers: {}", response.getHeaders());
        System.out.println("Response headers: " + response.getHeaders());
    }

    @Then("I print the response status")
    public void i_print_the_response_status() {
        logger.info("Response status: {} {}", response.getStatusCode(), response.getStatusLine());
        System.out.println("Response status: " + response.getStatusCode() + " " + response.getStatusLine());
    }
}