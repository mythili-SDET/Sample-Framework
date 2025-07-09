package com.example.framework.driver;

import com.example.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Factory for creating and managing {@link WebDriver} instances per thread.
 */
public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    /**
     * Get the WebDriver for current thread. Creates one if not present.
     */
    public static WebDriver getDriver() {
        if (TL_DRIVER.get() == null) {
            TL_DRIVER.set(createDriver());
        }
        return TL_DRIVER.get();
    }

    /**
     * Quit WebDriver for current thread and remove reference.
     */
    public static void quitDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver != null) {
            driver.quit();
            TL_DRIVER.remove();
        }
    }

    private static WebDriver createDriver() {
        String browser = ConfigManager.getInstance().getString("browser");
        LOG.info("Launching browser: {}", browser);
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox" -> driver = new FirefoxDriver();
            case "edge" -> driver = new EdgeDriver();
            case "remote" -> {
                // Provide grid URL via config: remote.url
                String gridUrl = ConfigManager.getInstance().getString("remote.url");
                try {
                    driver = new RemoteWebDriver(new java.net.URL(gridUrl), new org.openqa.selenium.chrome.ChromeOptions());
                } catch (java.net.MalformedURLException e) {
                    throw new RuntimeException("Invalid remote URL", e);
                }
            }
            default -> driver = new ChromeDriver();
        }
        int implicitWait = ConfigManager.getInstance().getInt("implicit.wait", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();
        return driver;
    }
}