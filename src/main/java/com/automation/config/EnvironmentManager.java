package com.automation.config;


public class EnvironmentManager {

    private static String environment;

    public static String getEnvironment() {
        if (environment == null) {
            environment = System.getProperty("env", "qa");
        }
        return environment;
    }

    public static void setEnvironment(String env) {
        environment = env;
        System.setProperty("env", env);
    }
}
