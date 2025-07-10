package com.automation.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager to handle all framework configurations
 * Singleton pattern implementation for global access
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        loadProperties();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find config.properties");
                throw new RuntimeException("config.properties file not found");
            }
            properties.load(input);
            logger.info("Configuration properties loaded successfully");
        } catch (IOException ex) {
            logger.error("Error loading configuration properties", ex);
            throw new RuntimeException("Failed to load configuration properties", ex);
        }
    }

    public String getProperty(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }

    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for property '{}': {}", key, value);
            throw new RuntimeException("Invalid integer configuration for: " + key);
        }
    }

    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    // Browser Configuration
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    public int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }

    public int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }

    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }

    // URL Configuration
    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    public String getStagingUrl() {
        return getProperty("staging.url");
    }

    // Database Configuration
    public String getDbUrl() {
        return getProperty("db.url");
    }

    public String getDbUsername() {
        return getProperty("db.username");
    }

    public String getDbPassword() {
        return getProperty("db.password");
    }

    public String getDbDriver() {
        return getProperty("db.driver");
    }

    // PostgreSQL Configuration
    public String getPostgresUrl() {
        return getProperty("postgres.url");
    }

    public String getPostgresUsername() {
        return getProperty("postgres.username");
    }

    public String getPostgresPassword() {
        return getProperty("postgres.password");
    }

    public String getPostgresDriver() {
        return getProperty("postgres.driver");
    }

    // API Configuration
    public int getApiTimeout() {
        return getIntProperty("api.timeout");
    }

    public String getApiContentType() {
        return getProperty("api.content.type");
    }

    // Test Data Paths
    public String getExcelDataPath() {
        return getProperty("excel.data.path");
    }

    public String getCsvDataPath() {
        return getProperty("csv.data.path");
    }

    public String getJsonDataPath() {
        return getProperty("json.data.path");
    }

    // Reporting Paths
    public String getReportPath() {
        return getProperty("report.path");
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }

    public String getAllureResultsPath() {
        return getProperty("allure.results.path");
    }

    // Environment Configuration
    public String getEnvironment() {
        return getProperty("environment", "dev");
    }

    public boolean isTestParallel() {
        return getBooleanProperty("test.parallel");
    }

    public int getThreadCount() {
        return getIntProperty("thread.count");
    }
}