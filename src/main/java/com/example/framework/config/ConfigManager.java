package com.example.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from classpath `config.properties` and allows overriding via JVM system properties.
 */
public final class ConfigManager {

    private static final String CONFIG_FILE = "/config.properties";
    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
            // Override with system properties (e.g. -Dbrowser=firefox)
            System.getProperties().forEach((key, value) -> properties.setProperty(key.toString(), value.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
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

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }
}