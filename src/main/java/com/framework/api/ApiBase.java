package com.framework.api;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class ApiBase {

    protected RequestSpecification requestSpec;

    public void initApi() {
        ConfigManager config = ConfigManager.getInstance();
        RestAssured.baseURI = config.get("apiBaseUrl");
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .addHeader("Authorization", "Bearer " + TokenManager.getToken())
                .build();
    }
}