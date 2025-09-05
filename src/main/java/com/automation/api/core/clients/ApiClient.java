package com.automation.api.core.clients;

import com.framework.api.base.APIBaseClass;
import com.framework.utils.JsonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient extends APIBaseClass {

    // Generic GET request with optional path params, query params, headers
    public Response get(String url, Map<String, ?> pathParams, Map<String, ?> queryParams,
                        Map<String, String> headers) {
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (pathParams != null) req.pathParams(pathParams);
        if (queryParams != null) req.queryParams(queryParams);
        return req.get(url).then().extract().response();
    }

    // Generic POST request with optional body and headers
    public Response post(String url, Object body, Map<String, String> headers) {
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (body != null) {
            if (body instanceof String) req.body((String) body);
            else if (body instanceof Map) req.body(JsonUtils.toJson(body));
            else req.body(body); // Serialize POJOs or JSON strings
        }
        return req.post(url).then().extract().response();
    }

    // Generic PUT request with path params, body, and headers
    public Response put(String url, Map<String, ?> pathParams, Object body, Map<String, String> headers) {
        RequestSpecification req = given().spec(requestSpec);
        if (headers != null) req.headers(headers);
        if (pathParams != null) req.pathParams(pathParams);
        if (body != null) {
            if (body instanceof String) req.body((String) body);
            else if (body instanceof Map) req.body(JsonUtils.toJson(body));
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
