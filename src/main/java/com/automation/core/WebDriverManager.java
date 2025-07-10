package com.automation.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe WebDriver Manager with Singleton pattern
 * Manages WebDriver instances for parallel execution
 */
public class WebDriverManager {
    private static final Logger logger = LogManager.getLogger(WebDriverManager.class);
    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, WebDriverWait> waitMap = new ConcurrentHashMap<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    private WebDriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get WebDriver instance for current thread
     */
    public static WebDriver getDriver() {
        long threadId = Thread.currentThread().getId();
        return driverMap.computeIfAbsent(threadId, k -> createDriver());
    }

    /**
     * Get WebDriverWait instance for current thread
     */
    public static WebDriverWait getWait() {
        long threadId = Thread.currentThread().getId();
        return waitMap.computeIfAbsent(threadId, k -> new WebDriverWait(getDriver(), Duration.ofSeconds(config.getExplicitWait())));
    }

    /**
     * Create new WebDriver instance based on configuration
     */
    private static WebDriver createDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        
        logger.info("Creating WebDriver for browser: {} (headless: {})", browser, headless);
        
        WebDriver driver = null;
        
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                driver = new SafariDriver(safariOptions);
                break;
                
            default:
                logger.warn("Unsupported browser: {}. Defaulting to Chrome", browser);
                WebDriverManager.chromedriver().setup();
                ChromeOptions defaultOptions = new ChromeOptions();
                if (headless) {
                    defaultOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(defaultOptions);
                break;
        }
        
        // Configure driver settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        
        logger.info("WebDriver created successfully for thread: {}", Thread.currentThread().getId());
        return driver;
    }

    /**
     * Quit WebDriver for current thread
     */
    public static void quitDriver() {
        long threadId = Thread.currentThread().getId();
        WebDriver driver = driverMap.get(threadId);
        
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", threadId);
            } catch (Exception e) {
                logger.error("Error quitting WebDriver for thread: {}", threadId, e);
            } finally {
                driverMap.remove(threadId);
                waitMap.remove(threadId);
            }
        }
    }

    /**
     * Quit all WebDriver instances
     */
    public static void quitAllDrivers() {
        logger.info("Quitting all WebDriver instances");
        driverMap.forEach((threadId, driver) -> {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", threadId);
            } catch (Exception e) {
                logger.error("Error quitting WebDriver for thread: {}", threadId, e);
            }
        });
        driverMap.clear();
        waitMap.clear();
    }

    /**
     * Get current thread's WebDriver instance
     */
    public static WebDriver getCurrentDriver() {
        return getDriver();
    }

    /**
     * Check if WebDriver exists for current thread
     */
    public static boolean hasDriver() {
        return driverMap.containsKey(Thread.currentThread().getId());
    }
}