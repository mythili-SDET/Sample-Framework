package com.automation.listeners;

import com.automation.core.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG Listener for test reporting and failure handling
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {} in class: {}", 
            result.getName(), result.getTestClass().getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {} in class: {}", 
            result.getName(), result.getTestClass().getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {} in class: {}", 
            result.getName(), result.getTestClass().getName());
        
        // Take screenshot on failure
        takeScreenshot(result);
        
        // Log exception details
        if (result.getThrowable() != null) {
            logger.error("Test failure reason: {}", result.getThrowable().getMessage());
            logger.error("Stack trace:", result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {} in class: {}", 
            result.getName(), result.getTestClass().getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {} in class: {}", 
            result.getName(), result.getTestClass().getName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: {}", context.getName());
        logger.info("Test suite parameters: {}", context.getCurrentXmlTest().getAllParameters());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: {}", context.getName());
        logger.info("Test results - Passed: {}, Failed: {}, Skipped: {}", 
            context.getPassedTests().size(),
            context.getFailedTests().size(),
            context.getSkippedTests().size());
    }

    /**
     * Take screenshot on test failure
     */
    private void takeScreenshot(ITestResult result) {
        try {
            if (WebDriverManager.hasDriver()) {
                // Create screenshot directory if it doesn't exist
                Path screenshotDir = Paths.get(SCREENSHOT_DIR);
                if (!Files.exists(screenshotDir)) {
                    Files.createDirectories(screenshotDir);
                }

                // Generate screenshot filename
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String testName = result.getName().replaceAll("[^a-zA-Z0-9]", "_");
                String screenshotName = String.format("%s_%s.png", testName, timestamp);
                String screenshotPath = SCREENSHOT_DIR + screenshotName;

                // Take screenshot
                File screenshot = WebDriverManager.getDriver()
                    .getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                
                // Copy to target location
                Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
                
                logger.info("Screenshot saved: {}", screenshotPath);
                
                // Attach screenshot to test result for reporting
                result.setAttribute("screenshot", screenshotPath);
            }
        } catch (IOException e) {
            logger.error("Failed to take screenshot", e);
        } catch (Exception e) {
            logger.error("Error taking screenshot", e);
        }
    }
}