package com.testframework.ui;

import com.testframework.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;

/**
 * WebDriver Manager to handle browser initialization and management
 * Supports multiple browsers with automatic driver management
 */
public class WebDriverManager {
    
    private static WebDriverManager instance;
    private WebDriver driver;
    private ConfigManager configManager;
    
    private WebDriverManager() {
        configManager = ConfigManager.getInstance();
    }
    
    public static WebDriverManager getInstance() {
        if (instance == null) {
            instance = new WebDriverManager();
        }
        return instance;
    }
    
    public WebDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
            configureDriver();
        }
        return driver;
    }
    
    private WebDriver createDriver() {
        String browserName = configManager.getBrowserName().toLowerCase();
        
        switch (browserName) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            case "safari":
                return createSafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
    
    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (configManager.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        
        return new ChromeDriver(options);
    }
    
    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (configManager.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    private WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (configManager.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }
    
    private WebDriver createSafariDriver() {
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }
    
    private void configureDriver() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configManager.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(configManager.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(configManager.getScriptTimeout()));
    }
    
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    
    public void closeDriver() {
        if (driver != null) {
            driver.close();
        }
    }
    
    public void navigateTo(String url) {
        driver.get(url);
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public String getTitle() {
        return driver.getTitle();
    }
    
    public void refresh() {
        driver.navigate().refresh();
    }
    
    public void back() {
        driver.navigate().back();
    }
    
    public void forward() {
        driver.navigate().forward();
    }
}