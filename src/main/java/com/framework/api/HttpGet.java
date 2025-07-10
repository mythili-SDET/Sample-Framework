package com.framework.api;

import io.restassured.response.Response;

public class HttpGet extends ApiBase {

    public Response get(String resource) {
        initApi();
        return requestSpec
                .when()
                .get(resource)
                .then()
                .extract()
                .response();
    }
}