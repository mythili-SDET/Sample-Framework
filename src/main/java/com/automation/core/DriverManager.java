package com.automation.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

/**
 * WebDriver Manager for handling browser initialization and cleanup
 * Uses ThreadLocal for thread-safe parallel execution
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Initialize WebDriver based on configuration
     */
    public static void initializeDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        
        logger.info("Initializing {} driver with headless: {}", browser, headless);
        
        WebDriver driver = null;
        
        switch (browser) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            case "edge":
                driver = createEdgeDriver(headless);
                break;
            case "safari":
                driver = createSafariDriver();
                break;
            default:
                logger.error("Unsupported browser: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        
        // Maximize window if not headless
        if (!headless) {
            driver.manage().window().maximize();
        }
        
        driverThreadLocal.set(driver);
        logger.info("Driver initialized successfully: {}", driver.getClass().getSimpleName());
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("WebDriver not initialized. Call initializeDriver() first.");
            throw new IllegalStateException("WebDriver not initialized");
        }
        return driver;
    }

    /**
     * Quit current WebDriver instance
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        } else {
            logger.warn("No WebDriver instance found to quit");
        }
    }

    /**
     * Create Chrome WebDriver
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Chrome performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--remote-allow-origins=*");
        
        // Set user agent
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Firefox optimizations
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        
        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Edge optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        
        return new EdgeDriver(options);
    }

    /**
     * Create Safari WebDriver
     */
    private static WebDriver createSafariDriver() {
        // Safari doesn't support headless mode
        logger.info("Safari browser does not support headless mode");
        return new SafariDriver();
    }

    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Get current browser name
     */
    public static String getCurrentBrowser() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            return driver.getClass().getSimpleName().replace("Driver", "");
        }
        return "No Driver";
    }

    /**
     * Quit all driver instances (useful for cleanup)
     */
    public static void quitAllDrivers() {
        // This method is called from hooks for global cleanup
        // Since we're using ThreadLocal, each thread will clean up its own driver
        quitDriver();
    }
}