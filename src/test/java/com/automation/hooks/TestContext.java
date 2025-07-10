package com.automation.hooks;

import org.openqa.selenium.WebDriver;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Context for sharing data between Cucumber steps
 * Maintains state for UI, API, and Database components
 */
public class TestContext {
    private WebDriver webDriver;
    private String apiBaseUrl;
    private String authToken;
    private Connection databaseConnection;
    private String dataSource;
    private final Map<String, Object> testData = new HashMap<>();
    private final Map<String, Object> sessionData = new HashMap<>();

    // WebDriver methods
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public boolean hasWebDriver() {
        return webDriver != null;
    }

    public void clearWebDriver() {
        this.webDriver = null;
    }

    // API methods
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean hasAuthToken() {
        return authToken != null && !authToken.isEmpty();
    }

    public void clearAuthToken() {
        this.authToken = null;
    }

    // Database methods
    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public void setDatabaseConnection(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean hasDatabaseConnection() {
        return databaseConnection != null;
    }

    public void clearDatabaseConnection() {
        this.databaseConnection = null;
    }

    // Data source methods
    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    // Test data methods
    public void setTestData(String key, Object value) {
        testData.put(key, value);
    }

    public Object getTestData(String key) {
        return testData.get(key);
    }

    public <T> T getTestData(String key, Class<T> type) {
        Object value = testData.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public void clearTestData() {
        testData.clear();
    }

    public boolean hasTestData(String key) {
        return testData.containsKey(key);
    }

    // Session data methods (for data that persists across scenarios)
    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }

    public Object getSessionData(String key) {
        return sessionData.get(key);
    }

    public <T> T getSessionData(String key, Class<T> type) {
        Object value = sessionData.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public void clearSessionData() {
        sessionData.clear();
    }

    public boolean hasSessionData(String key) {
        return sessionData.containsKey(key);
    }

    // Utility methods
    public void clearAll() {
        clearWebDriver();
        clearAuthToken();
        clearDatabaseConnection();
        clearTestData();
        clearSessionData();
    }
}