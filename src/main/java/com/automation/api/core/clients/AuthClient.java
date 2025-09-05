package com.automation.api.core.clients;

import com.framework.api.base.APIBaseClass;
import com.framework.api.endpoints.ApiEndpoints;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient extends APIBaseClass {

    private static final String AUTH_URL = "/auth/login"; // Replace with your actual auth endpoint

    /**
     * Generate authentication token by posting username and password.
     *
     * @param username API username
     * @param password API password
     * @return Authorization token string (e.g., JWT)
     */
    public String getAuthToken(String username, String password) {
        String payload = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        Response response = given()
                .spec(requestSpec)
                .body(payload)
                .post(ApiEndpoints.AUTH_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Adjust "token" to the actual JSON key your API returns for the token
        return response.jsonPath().getString("token");
    }
}
