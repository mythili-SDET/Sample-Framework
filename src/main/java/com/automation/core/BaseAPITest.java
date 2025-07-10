package com.automation.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Base class for API automation tests
 * Provides common REST Assured operations and utilities
 */
public class BaseAPITest {
    protected static final Logger logger = LogManager.getLogger(BaseAPITest.class);
    protected ConfigManager config;
    protected RequestSpecification requestSpec;

    public BaseAPITest() {
        this.config = ConfigManager.getInstance();
        setupRestAssured();
    }

    /**
     * Setup REST Assured configuration
     */
    private void setupRestAssured() {
        RestAssured.baseURI = config.getAPIBaseUrl();
        RestAssured.timeout = java.time.Duration.ofSeconds(config.getAPITimeout());
        
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        
        // Add authentication token if available
        String token = config.getAPIToken();
        if (token != null && !token.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + token);
        }
        
        logger.info("REST Assured configured with base URI: {}", config.getAPIBaseUrl());
    }

    /**
     * GET request
     */
    protected Response get(String endpoint) {
        logger.info("Making GET request to: {}", endpoint);
        return requestSpec.when().get(endpoint);
    }

    /**
     * GET request with path parameters
     */
    protected Response get(String endpoint, Map<String, Object> pathParams) {
        logger.info("Making GET request to: {} with path params: {}", endpoint, pathParams);
        return requestSpec.pathParams(pathParams).when().get(endpoint);
    }

    /**
     * GET request with query parameters
     */
    protected Response get(String endpoint, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        logger.info("Making GET request to: {} with path params: {} and query params: {}", 
                   endpoint, pathParams, queryParams);
        return requestSpec.pathParams(pathParams)
                        .queryParams(queryParams)
                        .when()
                        .get(endpoint);
    }

    /**
     * POST request with body
     */
    protected Response post(String endpoint, Object body) {
        logger.info("Making POST request to: {} with body: {}", endpoint, body);
        return requestSpec.body(body).when().post(endpoint);
    }

    /**
     * POST request with body and path parameters
     */
    protected Response post(String endpoint, Object body, Map<String, Object> pathParams) {
        logger.info("Making POST request to: {} with body: {} and path params: {}", 
                   endpoint, body, pathParams);
        return requestSpec.body(body)
                        .pathParams(pathParams)
                        .when()
                        .post(endpoint);
    }

    /**
     * PUT request with body
     */
    protected Response put(String endpoint, Object body) {
        logger.info("Making PUT request to: {} with body: {}", endpoint, body);
        return requestSpec.body(body).when().put(endpoint);
    }

    /**
     * PUT request with body and path parameters
     */
    protected Response put(String endpoint, Object body, Map<String, Object> pathParams) {
        logger.info("Making PUT request to: {} with body: {} and path params: {}", 
                   endpoint, body, pathParams);
        return requestSpec.body(body)
                        .pathParams(pathParams)
                        .when()
                        .put(endpoint);
    }

    /**
     * DELETE request
     */
    protected Response delete(String endpoint) {
        logger.info("Making DELETE request to: {}", endpoint);
        return requestSpec.when().delete(endpoint);
    }

    /**
     * DELETE request with path parameters
     */
    protected Response delete(String endpoint, Map<String, Object> pathParams) {
        logger.info("Making DELETE request to: {} with path params: {}", endpoint, pathParams);
        return requestSpec.pathParams(pathParams).when().delete(endpoint);
    }

    /**
     * PATCH request with body
     */
    protected Response patch(String endpoint, Object body) {
        logger.info("Making PATCH request to: {} with body: {}", endpoint, body);
        return requestSpec.body(body).when().patch(endpoint);
    }

    /**
     * Add header to request
     */
    protected void addHeader(String name, String value) {
        requestSpec.header(name, value);
        logger.info("Added header: {} = {}", name, value);
    }

    /**
     * Add multiple headers to request
     */
    protected void addHeaders(Map<String, String> headers) {
        requestSpec.headers(headers);
        logger.info("Added headers: {}", headers);
    }

    /**
     * Add query parameter
     */
    protected void addQueryParam(String name, Object value) {
        requestSpec.queryParam(name, value);
        logger.info("Added query param: {} = {}", name, value);
    }

    /**
     * Add path parameter
     */
    protected void addPathParam(String name, Object value) {
        requestSpec.pathParam(name, value);
        logger.info("Added path param: {} = {}", name, value);
    }

    /**
     * Set request body
     */
    protected void setBody(Object body) {
        requestSpec.body(body);
        logger.info("Set request body: {}", body);
    }

    /**
     * Set content type
     */
    protected void setContentType(ContentType contentType) {
        requestSpec.contentType(contentType);
        logger.info("Set content type: {}", contentType);
    }

    /**
     * Set accept header
     */
    protected void setAccept(ContentType contentType) {
        requestSpec.accept(contentType);
        logger.info("Set accept: {}", contentType);
    }

    /**
     * Assert response status code
     */
    protected void assertStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        logger.info("Response status code: {} (expected: {})", actualStatusCode, expectedStatusCode);
        Assert.assertEquals(actualStatusCode, expectedStatusCode, 
                          "Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
    }

    /**
     * Assert response status code is 200
     */
    protected void assertSuccess(Response response) {
        assertStatusCode(response, 200);
    }

    /**
     * Assert response status code is 201
     */
    protected void assertCreated(Response response) {
        assertStatusCode(response, 201);
    }

    /**
     * Assert response status code is 204
     */
    protected void assertNoContent(Response response) {
        assertStatusCode(response, 204);
    }

    /**
     * Assert response status code is 400
     */
    protected void assertBadRequest(Response response) {
        assertStatusCode(response, 400);
    }

    /**
     * Assert response status code is 401
     */
    protected void assertUnauthorized(Response response) {
        assertStatusCode(response, 401);
    }

    /**
     * Assert response status code is 403
     */
    protected void assertForbidden(Response response) {
        assertStatusCode(response, 403);
    }

    /**
     * Assert response status code is 404
     */
    protected void assertNotFound(Response response) {
        assertStatusCode(response, 404);
    }

    /**
     * Assert response status code is 500
     */
    protected void assertServerError(Response response) {
        assertStatusCode(response, 500);
    }

    /**
     * Get response body as string
     */
    protected String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    /**
     * Get response body as object
     */
    protected <T> T getResponseBody(Response response, Class<T> clazz) {
        return response.getBody().as(clazz);
    }

    /**
     * Get response header value
     */
    protected String getResponseHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }

    /**
     * Get all response headers
     */
    protected Headers getResponseHeaders(Response response) {
        return response.getHeaders();
    }

    /**
     * Assert response contains header
     */
    protected void assertResponseContainsHeader(Response response, String headerName) {
        String headerValue = getResponseHeader(response, headerName);
        Assert.assertNotNull(headerValue, "Response should contain header: " + headerName);
    }

    /**
     * Assert response header value
     */
    protected void assertResponseHeaderValue(Response response, String headerName, String expectedValue) {
        String actualValue = getResponseHeader(response, headerName);
        Assert.assertEquals(actualValue, expectedValue, 
                          "Header " + headerName + " should have value " + expectedValue);
    }

    /**
     * Assert response body contains text
     */
    protected void assertResponseBodyContains(Response response, String expectedText) {
        String responseBody = getResponseBody(response);
        Assert.assertTrue(responseBody.contains(expectedText), 
                        "Response body should contain: " + expectedText);
    }

    /**
     * Assert response body equals
     */
    protected void assertResponseBodyEquals(Response response, String expectedBody) {
        String actualBody = getResponseBody(response);
        Assert.assertEquals(actualBody, expectedBody, "Response body should match expected");
    }

    /**
     * Assert response time is less than
     */
    protected void assertResponseTimeLessThan(Response response, long maxTimeInMillis) {
        long responseTime = response.getTime();
        Assert.assertTrue(responseTime < maxTimeInMillis, 
                        "Response time should be less than " + maxTimeInMillis + "ms, but was " + responseTime + "ms");
    }

    /**
     * Log response details
     */
    protected void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Headers: {}", response.getHeaders());
        logger.info("Response Body: {}", getResponseBody(response));
        logger.info("Response Time: {}ms", response.getTime());
    }

    /**
     * Reset request specification
     */
    protected void resetRequestSpec() {
        setupRestAssured();
    }
}