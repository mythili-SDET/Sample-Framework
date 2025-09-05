package com.automation.config;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        String env = System.getProperty("env", "application"); // default
        String filePath = "src/main/resources/config/" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        String value = System.getProperty(key); // command line override
        return value != null ? value : properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getUIBaseUrl() {
        return properties.getProperty("ui.base.url" );
    }

    public String getAPIBaseUrl() {
        return properties.getProperty("api.base.url." );
    }

    public String getAPIToken() {
        return properties.getProperty("api.token." );
    }
    public String getDBHost() {
        return properties.getProperty("db.host." );
    }

    public String getDBName() {
        return properties.getProperty("db.name." );
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
