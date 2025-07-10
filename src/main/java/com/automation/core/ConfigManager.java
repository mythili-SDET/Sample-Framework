package com.automation.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configuration Manager for handling environment-specific configurations
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    private String environment;

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
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
            environment = properties.getProperty("environment", "qa");
            logger.info("Configuration loaded for environment: {}", environment);
        } catch (IOException e) {
            logger.error("Error loading configuration file", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getUIBaseUrl() {
        return properties.getProperty("ui.base.url." + environment);
    }

    public String getAPIBaseUrl() {
        return properties.getProperty("api.base.url." + environment);
    }

    public String getAPIToken() {
        return properties.getProperty("api.token." + environment);
    }

    public String getDBHost() {
        return properties.getProperty("db.host." + environment);
    }

    public String getDBName() {
        return properties.getProperty("db.name." + environment);
    }

    public String getDBUsername() {
        return properties.getProperty("db.username");
    }

    public String getDBPassword() {
        return properties.getProperty("db.password");
    }

    public int getDBPort() {
        return Integer.parseInt(properties.getProperty("db.port", "3306"));
    }

    public String getEnvironment() {
        return environment;
    }

    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("ui.implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("ui.explicit.wait", "20"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("ui.page.load.timeout", "30"));
    }

    public int getAPITimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "30"));
    }

    public boolean isParallelExecution() {
        return Boolean.parseBoolean(properties.getProperty("parallel.execution", "true"));
    }

    public int getThreadCount() {
        return Integer.parseInt(properties.getProperty("thread.count", "4"));
    }

    public int getRetryCount() {
        return Integer.parseInt(properties.getProperty("retry.count", "2"));
    }

    public int getRetryInterval() {
        return Integer.parseInt(properties.getProperty("retry.interval", "1000"));
    }
}