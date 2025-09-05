package com.automation.api.core.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class APIBaseClass {
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static AllureRestAssured allureFilter = new AllureRestAssured();

    static {
        setupRequestSpec();
        setupResponseSpec();
    }

    private static void setupRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(allureFilter)  // Allure listener to capture API calls
                .log(LogDetail.ALL)
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    private static void setupResponseSpec() {
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = responseSpec;
    }

    protected RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }
}
