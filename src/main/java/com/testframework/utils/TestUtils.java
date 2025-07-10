package com.testframework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

/**
 * Utility class providing common test helper methods
 * Contains reusable functions for test automation
 */
public class TestUtils {
    
    private static final Random random = new Random();
    
    /**
     * Generate random string of specified length
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate random email address
     */
    public static String generateRandomEmail() {
        return "test" + generateRandomString(8) + "@example.com";
    }
    
    /**
     * Generate random phone number
     */
    public static String generateRandomPhone() {
        return "+1" + (random.nextInt(900) + 100) + "-" + (random.nextInt(900) + 100) + "-" + (random.nextInt(9000) + 1000);
    }
    
    /**
     * Wait for element to be clickable
     */
    public static void waitForElementClickable(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be visible
     */
    public static void waitForElementVisible(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Scroll to element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }
    
    /**
     * Click element using JavaScript
     */
    public static void clickElementJS(WebDriver driver, WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
    
    /**
     * Get element text using JavaScript
     */
    public static String getElementTextJS(WebDriver driver, WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", element);
        }
        return element.getText();
    }
    
    /**
     * Set element value using JavaScript
     */
    public static void setElementValueJS(WebDriver driver, WebElement element, String value) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, value);
        }
    }
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Wait for jQuery to load (if present)
     */
    public static void waitForJQueryLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(webDriver -> {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            return (Boolean) js.executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
        });
    }
    
    /**
     * Take screenshot of specific element
     */
    public static byte[] takeElementScreenshot(WebDriver driver, WebElement element) {
        if (driver instanceof org.openqa.selenium.TakesScreenshot) {
            return element.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
        }
        return new byte[0];
    }
    
    /**
     * Check if element is displayed and enabled
     */
    public static boolean isElementInteractable(WebElement element) {
        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get current timestamp
     */
    public static String getCurrentTimestamp() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
    
    /**
     * Format duration in milliseconds to readable string
     */
    public static String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        milliseconds = milliseconds % 1000;
        
        if (minutes > 0) {
            return String.format("%dm %ds %dms", minutes, seconds, milliseconds);
        } else if (seconds > 0) {
            return String.format("%ds %dms", seconds, milliseconds);
        } else {
            return String.format("%dms", milliseconds);
        }
    }
    
    /**
     * Sleep for specified milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Retry operation with specified attempts
     */
    public static <T> T retryOperation(java.util.function.Supplier<T> operation, int maxAttempts, long delayMs) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    sleep(delayMs);
                }
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxAttempts + " attempts", lastException);
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        String phoneRegex = "^\\+?[1-9]\\d{1,14}$";
        return phone != null && phone.replaceAll("[\\s\\-\\(\\)]", "").matches(phoneRegex);
    }
    
    /**
     * Generate random number between min and max
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Generate random date within specified range
     */
    public static java.time.LocalDate generateRandomDate(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = startEpochDay + random.nextInt((int) (endEpochDay - startEpochDay + 1));
        return java.time.LocalDate.ofEpochDay(randomEpochDay);
    }
}