package com.framework.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HttpPut extends ApiBase {

    public Response put(String resource, Object body) {
        initApi();
        return requestSpec
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(resource)
                .then()
                .extract()
                .response();
    }
}