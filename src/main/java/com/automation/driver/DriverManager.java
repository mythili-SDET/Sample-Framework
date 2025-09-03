package com.automation.driver;

import org.openqa.selenium.WebDriver;
import utils.ConfigManager;

import java.time.Duration;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverManager() {} // private constructor for singleton

    public static void initializeDriver() {
        if (driverThread.get() == null) {
            ConfigManager config = ConfigManager.getInstance();

            // Get execution mode / browser / os / headless from command line or properties
            ExecutionMode mode = ExecutionMode.fromString(System.getProperty("execution.mode",
                    config.get("executionMode")));
            BrowserType browser = BrowserType.fromString(System.getProperty("browser",
                    config.get("browser")));
            OSType os = OSType.fromString(System.getProperty("os", config.get("os")));
            boolean headless = Boolean.parseBoolean(System.getProperty("headless",
                    config.get("headless")));

            // Create driver via factory
            WebDriver driver = DriverFactory.createInstance(browser, mode, os, headless);

            // Default configuration
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                    Long.parseLong(config.get("implicitWait"))));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                    Long.parseLong(config.get("pageLoadTimeout"))));

            if (!headless) driver.manage().window().maximize();

            driverThread.set(driver);
        }
    }

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initializeDriver();
        }
        return driverThread.get();
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
        }
    }
}

