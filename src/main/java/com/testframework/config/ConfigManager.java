package com.testframework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager to handle all framework configuration properties
 * Provides centralized access to all configuration settings
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/config/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    // Browser Configuration
    public String getBrowserName() {
        return getProperty("browser.name", "chrome");
    }
    
    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }
    
    public int getImplicitWait() {
        return getIntProperty("browser.implicit.wait", 10);
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("browser.page.load.timeout", 30);
    }
    
    public int getScriptTimeout() {
        return getIntProperty("browser.script.timeout", 30);
    }
    
    // URL Configuration
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }
    
    public String getTestUrl() {
        return getProperty("test.url");
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
    
    // API Configuration
    public int getApiTimeout() {
        return getIntProperty("api.timeout", 30);
    }
    
    public int getApiConnectionTimeout() {
        return getIntProperty("api.connection.timeout", 10);
    }
    
    public int getApiReadTimeout() {
        return getIntProperty("api.read.timeout", 10);
    }
    
    // Test Data Configuration
    public String getDataFilePath() {
        return getProperty("data.file.path");
    }
    
    public String getExcelFileName() {
        return getProperty("excel.file.name");
    }
    
    public String getCsvFileName() {
        return getProperty("csv.file.name");
    }
    
    public String getJsonFileName() {
        return getProperty("json.file.name");
    }
    
    // Reporting Configuration
    public String getExtentReportPath() {
        return getProperty("extent.report.path");
    }
    
    public String getExtentReportTitle() {
        return getProperty("extent.report.title");
    }
    
    public String getExtentReportDocumentTitle() {
        return getProperty("extent.report.document.title");
    }
    
    // Screenshot Configuration
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }
    
    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    // Logging Configuration
    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }
    
    public String getLogFilePath() {
        return getProperty("log.file.path");
    }
    
    public String getLogFileName() {
        return getProperty("log.file.name");
    }
    
    // Test Execution Configuration
    public boolean isParallelExecution() {
        return getBooleanProperty("parallel.execution", true);
    }
    
    public int getThreadCount() {
        return getIntProperty("thread.count", 3);
    }
    
    public int getRetryFailedTests() {
        return getIntProperty("retry.failed.tests", 2);
    }
    
    // Wait Configuration
    public int getExplicitWait() {
        return getIntProperty("explicit.wait", 20);
    }
    
    public int getFluentWait() {
        return getIntProperty("fluent.wait", 30);
    }
    
    public int getPollingInterval() {
        return getIntProperty("polling.interval", 500);
    }
}