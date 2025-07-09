package com.testframework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testframework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

/**
 * API Manager to handle REST API testing operations
 * Provides comprehensive API testing capabilities using REST Assured
 */
public class ApiManager {
    
    private ConfigManager configManager;
    private ObjectMapper objectMapper;
    private RequestSpecification requestSpec;
    
    public ApiManager() {
        configManager = ConfigManager.getInstance();
        objectMapper = new ObjectMapper();
        initializeRequestSpec();
    }
    
    /**
     * Initialize request specification with default settings
     */
    private void initializeRequestSpec() {
        requestSpec = RestAssured.given()
            .baseUri(configManager.getApiBaseUrl())
            .contentType(ContentType.JSON)
            .timeout(configManager.getApiTimeout() * 1000)
            .connectionTimeout(configManager.getApiConnectionTimeout() * 1000)
            .readTimeout(configManager.getApiReadTimeout() * 1000);
    }
    
    /**
     * Set base URI
     */
    public ApiManager setBaseUri(String baseUri) {
        requestSpec = requestSpec.baseUri(baseUri);
        return this;
    }
    
    /**
     * Add header
     */
    public ApiManager addHeader(String name, String value) {
        requestSpec = requestSpec.header(name, value);
        return this;
    }
    
    /**
     * Add multiple headers
     */
    public ApiManager addHeaders(Map<String, String> headers) {
        requestSpec = requestSpec.headers(new Headers(
            headers.entrySet().stream()
                .map(entry -> new Header(entry.getKey(), entry.getValue()))
                .toArray(Header[]::new)
        ));
        return this;
    }
    
    /**
     * Set content type
     */
    public ApiManager setContentType(ContentType contentType) {
        requestSpec = requestSpec.contentType(contentType);
        return this;
    }
    
    /**
     * Add query parameter
     */
    public ApiManager addQueryParam(String name, String value) {
        requestSpec = requestSpec.queryParam(name, value);
        return this;
    }
    
    /**
     * Add multiple query parameters
     */
    public ApiManager addQueryParams(Map<String, String> params) {
        requestSpec = requestSpec.queryParams(params);
        return this;
    }
    
    /**
     * Add path parameter
     */
    public ApiManager addPathParam(String name, String value) {
        requestSpec = requestSpec.pathParam(name, value);
        return this;
    }
    
    /**
     * Add multiple path parameters
     */
    public ApiManager addPathParams(Map<String, String> params) {
        requestSpec = requestSpec.pathParams(params);
        return this;
    }
    
    /**
     * Set request body
     */
    public ApiManager setBody(Object body) {
        requestSpec = requestSpec.body(body);
        return this;
    }
    
    /**
     * Set request body as string
     */
    public ApiManager setBody(String body) {
        requestSpec = requestSpec.body(body);
        return this;
    }
    
    /**
     * Set request body from Map
     */
    public ApiManager setBody(Map<String, Object> body) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            requestSpec = requestSpec.body(jsonBody);
        } catch (Exception e) {
            throw new RuntimeException("Error converting body to JSON", e);
        }
        return this;
    }
    
    /**
     * Perform GET request
     */
    public Response get(String endpoint) {
        return requestSpec.when().get(endpoint);
    }
    
    /**
     * Perform POST request
     */
    public Response post(String endpoint) {
        return requestSpec.when().post(endpoint);
    }
    
    /**
     * Perform PUT request
     */
    public Response put(String endpoint) {
        return requestSpec.when().put(endpoint);
    }
    
    /**
     * Perform DELETE request
     */
    public Response delete(String endpoint) {
        return requestSpec.when().delete(endpoint);
    }
    
    /**
     * Perform PATCH request
     */
    public Response patch(String endpoint) {
        return requestSpec.when().patch(endpoint);
    }
    
    /**
     * Perform HEAD request
     */
    public Response head(String endpoint) {
        return requestSpec.when().head(endpoint);
    }
    
    /**
     * Perform OPTIONS request
     */
    public Response options(String endpoint) {
        return requestSpec.when().options(endpoint);
    }
    
    /**
     * Get response status code
     */
    public int getStatusCode(Response response) {
        return response.getStatusCode();
    }
    
    /**
     * Get response body as string
     */
    public String getResponseBody(Response response) {
        return response.getBody().asString();
    }
    
    /**
     * Get response body as Map
     */
    public Map<String, Object> getResponseBodyAsMap(Response response) {
        return response.getBody().as(Map.class);
    }
    
    /**
     * Get response header value
     */
    public String getResponseHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }
    
    /**
     * Get all response headers
     */
    public Map<String, String> getResponseHeaders(Response response) {
        return response.getHeaders().asMap();
    }
    
    /**
     * Get response time in milliseconds
     */
    public long getResponseTime(Response response) {
        return response.getTime();
    }
    
    /**
     * Check if response status code matches expected
     */
    public boolean isStatusCode(Response response, int expectedStatusCode) {
        return response.getStatusCode() == expectedStatusCode;
    }
    
    /**
     * Check if response contains specific text
     */
    public boolean responseContains(Response response, String text) {
        return response.getBody().asString().contains(text);
    }
    
    /**
     * Check if response header contains specific value
     */
    public boolean responseHeaderContains(Response response, String headerName, String value) {
        String headerValue = response.getHeader(headerName);
        return headerValue != null && headerValue.contains(value);
    }
    
    /**
     * Extract value from JSON response using JSONPath
     */
    public Object extractValue(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }
    
    /**
     * Extract string value from JSON response using JSONPath
     */
    public String extractStringValue(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }
    
    /**
     * Extract integer value from JSON response using JSONPath
     */
    public Integer extractIntValue(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }
    
    /**
     * Extract list from JSON response using JSONPath
     */
    public <T> T extractList(Response response, String jsonPath) {
        return response.jsonPath().getList(jsonPath);
    }
    
    /**
     * Validate response schema against JSON schema
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Create a simple GET request with basic validation
     */
    public ApiResponse simpleGet(String endpoint) {
        Response response = get(endpoint);
        return new ApiResponse(response);
    }
    
    /**
     * Create a simple POST request with basic validation
     */
    public ApiResponse simplePost(String endpoint, Object body) {
        Response response = setBody(body).post(endpoint);
        return new ApiResponse(response);
    }
    
    /**
     * Create a simple PUT request with basic validation
     */
    public ApiResponse simplePut(String endpoint, Object body) {
        Response response = setBody(body).put(endpoint);
        return new ApiResponse(response);
    }
    
    /**
     * Create a simple DELETE request with basic validation
     */
    public ApiResponse simpleDelete(String endpoint) {
        Response response = delete(endpoint);
        return new ApiResponse(response);
    }
    
    /**
     * Reset request specification to default
     */
    public ApiManager reset() {
        initializeRequestSpec();
        return this;
    }
    
    /**
     * Helper method for JSON schema validation
     */
    private static io.restassured.module.jsv.JsonSchemaValidator matchesJsonSchemaInClasspath(String schemaPath) {
        return io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath);
    }
    
    /**
     * Inner class to wrap API response with common validation methods
     */
    public static class ApiResponse {
        private Response response;
        
        public ApiResponse(Response response) {
            this.response = response;
        }
        
        public Response getResponse() {
            return response;
        }
        
        public int getStatusCode() {
            return response.getStatusCode();
        }
        
        public String getBody() {
            return response.getBody().asString();
        }
        
        public Map<String, Object> getBodyAsMap() {
            return response.getBody().as(Map.class);
        }
        
        public boolean isSuccess() {
            return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
        }
        
        public boolean isClientError() {
            return response.getStatusCode() >= 400 && response.getStatusCode() < 500;
        }
        
        public boolean isServerError() {
            return response.getStatusCode() >= 500;
        }
        
        public String getHeader(String headerName) {
            return response.getHeader(headerName);
        }
        
        public long getResponseTime() {
            return response.getTime();
        }
    }
}