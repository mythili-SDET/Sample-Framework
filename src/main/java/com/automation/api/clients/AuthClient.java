package com.automation.api.core.clients;

import com.automation.api.core.base.APIBaseClass;
import com.automation.api.core.endpoints.ApiEndpoints;
import io.restassured.response.Response;

// using base class given()

public class AuthClient extends APIBaseClass {

    // Endpoint is defined in ApiEndpoints

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
