package com.automation.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

/**
 * Base class for API testing with RestAssured
 * Provides common functionality for REST API automation
 */
public class APIBaseTest {
    private static final Logger logger = LogManager.getLogger(APIBaseTest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();
    
    protected RequestSpecification requestSpec;
    protected String authToken;
    protected String baseURI;

    @BeforeMethod
    public void apiSetUp() {
        logger.info("Starting API test setup");
        
        // Set base URI from config
        baseURI = config.getApiBaseUrl();
        RestAssured.baseURI = baseURI;
        
        // Initialize request specification
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        
        // Setup authentication if token is available
        setupAuthentication();
        
        logger.info("API test setup completed");
    }

    /**
     * Setup authentication token
     */
    protected void setupAuthentication() {
        authToken = TokenManager.getInstance().getToken();
        if (authToken != null && !authToken.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + authToken);
            logger.info("Authentication token set");
        }
    }

    /**
     * Refresh authentication token
     */
    protected void refreshToken() {
        authToken = TokenManager.getInstance().refreshToken();
        if (authToken != null) {
            requestSpec.header("Authorization", "Bearer " + authToken);
            logger.info("Authentication token refreshed");
        }
    }

    /**
     * Set custom headers
     * @param headers Map of headers
     */
    protected void setHeaders(Map<String, String> headers) {
        logger.info("Setting custom headers: {}", headers);
        requestSpec.headers(headers);
    }

    /**
     * Set request body
     * @param body Request body object
     */
    protected void setRequestBody(Object body) {
        logger.info("Setting request body: {}", body);
        requestSpec.body(body);
    }

    /**
     * Set query parameters
     * @param params Map of query parameters
     */
    protected void setQueryParams(Map<String, Object> params) {
        logger.info("Setting query parameters: {}", params);
        requestSpec.queryParams(params);
    }

    /**
     * Set path parameters
     * @param params Map of path parameters
     */
    protected void setPathParams(Map<String, Object> params) {
        logger.info("Setting path parameters: {}", params);
        requestSpec.pathParams(params);
    }

    /**
     * Execute GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response get(String endpoint) {
        logger.info("Executing GET request to: {}", endpoint);
        Response response = requestSpec.get(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute GET request with path parameters
     * @param endpoint API endpoint with path parameters
     * @param pathParams Path parameters
     * @return Response object
     */
    protected Response get(String endpoint, Map<String, Object> pathParams) {
        setPathParams(pathParams);
        return get(endpoint);
    }

    /**
     * Execute POST request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    protected Response post(String endpoint, Object body) {
        setRequestBody(body);
        logger.info("Executing POST request to: {}", endpoint);
        Response response = requestSpec.post(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute POST request without body
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response post(String endpoint) {
        logger.info("Executing POST request to: {}", endpoint);
        Response response = requestSpec.post(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute PUT request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    protected Response put(String endpoint, Object body) {
        setRequestBody(body);
        logger.info("Executing PUT request to: {}", endpoint);
        Response response = requestSpec.put(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute PATCH request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    protected Response patch(String endpoint, Object body) {
        setRequestBody(body);
        logger.info("Executing PATCH request to: {}", endpoint);
        Response response = requestSpec.patch(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response delete(String endpoint) {
        logger.info("Executing DELETE request to: {}", endpoint);
        Response response = requestSpec.delete(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Execute DELETE request with path parameters
     * @param endpoint API endpoint with path parameters
     * @param pathParams Path parameters
     * @return Response object
     */
    protected Response delete(String endpoint, Map<String, Object> pathParams) {
        setPathParams(pathParams);
        return delete(endpoint);
    }

    /**
     * Validate response status code
     * @param response Response object
     * @param expectedStatusCode Expected status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        logger.info("Validating status code. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        
        if (actualStatusCode != expectedStatusCode) {
            logger.error("Status code validation failed. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            throw new AssertionError("Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        }
    }

    /**
     * Get response body as string
     * @param response Response object
     * @return Response body as string
     */
    protected String getResponseBody(Response response) {
        String body = response.getBody().asString();
        logger.debug("Response body: {}", body);
        return body;
    }

    /**
     * Get response header value
     * @param response Response object
     * @param headerName Header name
     * @return Header value
     */
    protected String getResponseHeader(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        logger.debug("Response header '{}': {}", headerName, headerValue);
        return headerValue;
    }

    /**
     * Extract value from JSON response using JSONPath
     * @param response Response object
     * @param jsonPath JSONPath expression
     * @return Extracted value
     */
    protected Object extractFromResponse(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        logger.debug("Extracted value from JSONPath '{}': {}", jsonPath, value);
        return value;
    }

    /**
     * Validate JSON schema
     * @param response Response object
     * @param schemaPath Path to JSON schema file
     */
    protected void validateJsonSchema(Response response, String schemaPath) {
        logger.info("Validating JSON schema: {}", schemaPath);
        // Implementation would require additional dependencies like json-schema-validator
        // response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

    /**
     * Get response time
     * @param response Response object
     * @return Response time in milliseconds
     */
    protected long getResponseTime(Response response) {
        long responseTime = response.getTime();
        logger.info("Response time: {} ms", responseTime);
        return responseTime;
    }

    /**
     * Log response details
     * @param response Response object
     */
    private void logResponse(Response response) {
        logger.info("Response status: {} {}", response.getStatusCode(), response.getStatusLine());
        logger.debug("Response time: {} ms", response.getTime());
        logger.debug("Response headers: {}", response.getHeaders());
        
        String contentType = response.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            logger.debug("Response body: {}", response.getBody().asString());
        }
    }

    /**
     * Reset request specification to default
     */
    protected void resetRequestSpec() {
        logger.debug("Resetting request specification");
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        setupAuthentication();
    }
}