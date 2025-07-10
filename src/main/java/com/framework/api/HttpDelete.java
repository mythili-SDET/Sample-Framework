package com.framework.api;

import io.restassured.response.Response;

public class HttpDelete extends ApiBase {

    public Response delete(String resource) {
        initApi();
        return requestSpec
                .when()
                .delete(resource)
                .then()
                .extract()
                .response();
    }
}