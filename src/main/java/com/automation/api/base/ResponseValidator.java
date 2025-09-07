package com.automation.api.core.base;

import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;

import java.io.File;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");
    }

    public static void validateHeader(Response response, String headerName, String expectedValue) {
        Assert.assertEquals(response.getHeader(headerName), expectedValue, "Header mismatch for " + headerName);
    }

    public static void validateResponseTime(Response response, long maxTimeInMillis) {
        Assert.assertTrue(response.getTime() <= maxTimeInMillis, "Response time exceeded " + maxTimeInMillis);
    }

    public static String getValueByJsonPath(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    public static void validateContentType(Response response, String expectedContentType) {
        Assert.assertTrue(response.getContentType().toLowerCase().contains(expectedContentType.toLowerCase()),
                "Content type mismatch");
    }

    // JSON Schema validation method
    public static void validateJsonSchema(Response response, String schemaFilePath) {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File(schemaFilePath)));
    }
}
