package com.framework.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HttpPost extends ApiBase {

    public Response post(String resource, Object body) {
        initApi();
        return requestSpec
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(resource)
                .then()
                .extract()
                .response();
    }
}