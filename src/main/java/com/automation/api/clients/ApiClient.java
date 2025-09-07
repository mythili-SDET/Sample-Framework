package com.automation.api.clients;

import com.automation.api.base.APIBaseClass;
import com.automation.utils.JsonUtil;
import com.automation.logger.LoggerManager;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.Logger;

import java.util.*;

// removed static import; using base class given()

public class ApiClient extends APIBaseClass {
    
    private static final Logger logger = LoggerManager.getAPILogger();

    // Generic GET request with optional path params, query params, headers
    public Response get(String url, Map<String, ?> pathParams, Map<String, ?> queryParams,
                        Map<String, String> headers) {
        LoggerManager.logAPIRequest("GET", url, null);
        long startTime = System.currentTimeMillis();
        
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (pathParams != null) req.pathParams(pathParams);
        if (queryParams != null) req.queryParams(queryParams);
        
        Response response = req.get(url).then().extract().response();
        
        long responseTime = System.currentTimeMillis() - startTime;
        LoggerManager.logAPIResponse(response.getStatusCode(), response.asString(), responseTime);
        
        return response;
    }

    // Generic POST request with optional body and headers
    public Response post(String url, Object body, Map<String, String> headers) {
        String requestBody = null;
        if (body != null) {
            if (body instanceof String) requestBody = (String) body;
            else if (body instanceof Map) requestBody = JsonUtil.objectToJson(body);
            else requestBody = JsonUtil.objectToJson(body);
        }
        
        LoggerManager.logAPIRequest("POST", url, requestBody);
        long startTime = System.currentTimeMillis();
        
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (body != null) {
            if (body instanceof String) req.body((String) body);
            else if (body instanceof Map) req.body(JsonUtil.objectToJson(body));
            else req.body(body); // Serialize POJOs or JSON strings
        }
        
        Response response = req.post(url).then().extract().response();
        
        long responseTime = System.currentTimeMillis() - startTime;
        LoggerManager.logAPIResponse(response.getStatusCode(), response.asString(), responseTime);
        
        return response;
    }

    // Generic PUT request with path params, body, and headers
    public Response put(String url, Map<String, ?> pathParams, Object body, Map<String, String> headers) {
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (pathParams != null) req.pathParams(pathParams);
        if (body != null) {
            if (body instanceof String) req.body((String) body);
            else if (body instanceof Map) req.body(JsonUtil.toJson(body));
            else req.body(body);
        }
        return req.put(url).then().extract().response();
    }

    // Generic DELETE request with path params and headers
    public Response delete(String url, Map<String, ?> pathParams, Map<String, String> headers) {
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (pathParams != null) req.pathParams(pathParams);
        return req.delete(url).then().extract().response();
    }
    
}
