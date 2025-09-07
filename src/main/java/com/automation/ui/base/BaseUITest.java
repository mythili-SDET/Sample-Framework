package com.automation.ui.base;

import com.automation.config.ConfigManager;
import com.automation.driver.DriverManager;
import com.automation.helpers.SeleniumHelper;
import com.automation.helpers.WaitHelper;
import com.automation.listeners.RetryListener;
import com.automation.listeners.TestListener;
import com.automation.logger.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.Listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Listeners({TestListener.class, RetryListener.class})
public class BaseUITest {
    protected static final Logger logger = LoggerManager.getLogger(BaseUITest.class);

    protected ConfigManager config;
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Helpers
    protected SeleniumHelper seleniumHelper;
    protected WaitHelper waitHelper;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        config = ConfigManager.getInstance();
        LoggerManager.logFrameworkEvent("UI Test Suite Start", "Environment: " + System.getProperty("env", "qa"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));

        // Initialize helpers backed by current driver
        seleniumHelper = new SeleniumHelper();
        waitHelper = new WaitHelper();
        
        LoggerManager.logFrameworkEvent("WebDriver Initialized", "Test: " + this.getClass().getSimpleName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (!result.isSuccess()) {
                saveScreenshot(result.getMethod().getMethodName());
            }
        } finally {
            DriverManager.quitDriver();
        }
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
    }

    protected void navigateToPath(String path) {
        navigateTo(config.getUIBaseUrl() + path);
    }

    protected String saveScreenshot(String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String screenshotPath = config.getProperty("screenshot.path", "target/screenshots/") + fileName;

            Path path = Paths.get(screenshotPath);
            Files.createDirectories(path.getParent());

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), path);
            logger.info("Screenshot saved: {}", screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }
}