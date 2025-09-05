package com.automation.api.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseAPITest {
    protected static final Logger logger = LogManager.getLogger(BaseAPITest.class);
    protected ConfigManager config;
    protected RequestSpecification requestSpec;

    public BaseAPITest() {
        this.config = ConfigManager.getInstance();
        setupRestAssured();
    }
    private void setupRestAssured() {
        RestAssured.baseURI = config.getAPIBaseUrl();
        requestSpec = given().contentType(ContentType.JSON).accept(ContentType.JSON);
        String token = config.getAPIToken();
        if (token != null && !token.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + token);
        }
        logger.info("REST Assured configured with base URI: {}", config.getAPIBaseUrl());
    }
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
     * Perform GET request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response performGet(String endpoint) {
        logger.info("Performing GET request to: {}", endpoint);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams Query parameters
     * @return Response object
     */
    protected Response performGet(String endpoint, Map<String, Object> queryParams) {
        logger.info("Performing GET request to: {} with query params: {}", endpoint, queryParams);

        Response response = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform POST request with JSON body
     * @param endpoint API endpoint
     * @param requestBody Request body as JSON string
     * @return Response object
     */
    protected Response performPost(String endpoint, String requestBody) {
        logger.info("Performing POST request to: {} with body: {}", endpoint, requestBody);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform POST request with object body
     * @param endpoint API endpoint
     * @param requestBody Request body as object
     * @return Response object
     */
    protected Response performPost(String endpoint, Object requestBody) {
        logger.info("Performing POST request to: {} with object body", endpoint);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform PUT request
     * @param endpoint API endpoint
     * @param requestBody Request body
     * @return Response object
     */
    protected Response performPut(String endpoint, Object requestBody) {
        logger.info("Performing PUT request to: {}", endpoint);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform PATCH request
     * @param endpoint API endpoint
     * @param requestBody Request body
     * @return Response object
     */
    protected Response performPatch(String endpoint, Object requestBody) {
        logger.info("Performing PATCH request to: {}", endpoint);

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Perform DELETE request
     * @param endpoint API endpoint
     * @return Response object
     */
    protected Response performDelete(String endpoint) {
        logger.info("Performing DELETE request to: {}", endpoint);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();

        logResponse(response);
        return response;
    }

    /**
     * Add authentication header
     * @param token Bearer token
     */
    protected void addBearerToken(String token) {
        requestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        logger.debug("Bearer token added to request specification");
    }

    /**
     * Add API key header
     * @param apiKey API key
     * @param headerName Header name (default: X-API-Key)
     */
    protected void addApiKey(String apiKey, String headerName) {
        if (headerName == null) {
            headerName = "X-API-Key";
        }

        requestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader(headerName, apiKey)
                .build();

        logger.debug("API key added to request specification with header: {}", headerName);
    }

    /**
     * Add custom header
     * @param headerName Header name
     * @param headerValue Header value
     */
    protected void addHeader(String headerName, String headerValue) {
        requestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader(headerName, headerValue)
                .build();

        logger.debug("Custom header added: {} = {}", headerName, headerValue);
    }

    /**
     * Set content type
     * @param contentType Content type
     */
    protected void setContentType(ContentType contentType) {
        requestSpec = new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .setContentType(contentType)
                .build();

        logger.debug("Content type set to: {}", contentType);
    }

    /**
     * Validate response status code
     * @param response Response object
     * @param expectedStatusCode Expected status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            logger.error("Status code mismatch. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            throw new AssertionError("Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        }
        logger.info("Status code validation passed: {}", actualStatusCode);
    }

    /**
     * Validate response contains specific text
     * @param response Response object
     * @param expectedText Expected text in response
     */
    protected void validateResponseContains(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        if (!responseBody.contains(expectedText)) {
            logger.error("Response does not contain expected text: {}", expectedText);
            throw new AssertionError("Response does not contain expected text: " + expectedText);
        }
        logger.info("Response contains expected text: {}", expectedText);
    }

    /**
     * Get response time in milliseconds
     * @param response Response object
     * @return Response time
     */
    protected long getResponseTime(Response response) {
        long responseTime = response.getTime();
        logger.info("Response time: {} ms", responseTime);
        return responseTime;
    }

    /**
     * Extract value from JSON response using JsonPath
     * @param response Response object
     * @param jsonPath JsonPath expression
     * @return Extracted value
     */
    protected <T> T extractFromResponse(Response response, String jsonPath) {
        T value = response.jsonPath().get(jsonPath);
        logger.debug("Extracted value from response using JsonPath '{}': {}", jsonPath, value);
        return value;
    }

    /**
     * Enable or disable request/response logging
     * @param enable true to enable, false to disable
     */
    protected void enableLogging(boolean enable) {
        if (enable) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }

    /**
     * Log response details
     * @param response Response object
     */
    private void logResponse(Response response) {
        logger.info("Response Status: {} {}", response.getStatusCode(), response.getStatusLine());
        logger.info("Response Time: {} ms", response.getTime());
        logger.debug("Response Headers: {}", response.getHeaders());
        logger.debug("Response Body: {}", response.getBody().asString());
    }

    /**
     * Reset request specification to default
     */
    protected void resetRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "Automation-Framework/1.0")
                .build();

        logger.debug("Request specification reset to default");
    }

}