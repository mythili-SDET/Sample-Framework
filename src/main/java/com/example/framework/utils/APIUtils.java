package com.example.framework.utils;

import com.example.framework.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Wrapper around REST Assured to provide simple GET/POST etc. operations.
 */
public final class APIUtils {

    private static final String BASE_URL = ConfigManager.getInstance().getString("api.base.url");

    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();
    }

    private APIUtils() {}

    public static Response get(String path) {
        return givenDefault().get(path);
    }

    public static Response post(String path, Object body) {
        return givenDefault().body(body).post(path);
    }

    public static Response put(String path, Object body) {
        return givenDefault().body(body).put(path);
    }

    public static Response delete(String path) {
        return givenDefault().delete(path);
    }

    private static RequestSpecification givenDefault() {
        return RestAssured.given().header("Content-Type", "application/json");
    }
}