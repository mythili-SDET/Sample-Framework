package com.framework.api;

import com.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TokenManager {

    private static String cachedToken;

    private TokenManager() {}

    public static synchronized String getToken() {
        if (cachedToken == null) {
            ConfigManager config = ConfigManager.getInstance();
            String endpoint = config.get("tokenEndpoint");
            String username = config.get("apiUser");
            String password = config.get("apiPassword");

            Response response = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password))
                    .post(endpoint)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            cachedToken = response.jsonPath().getString("token");
        }
        return cachedToken;
    }
}