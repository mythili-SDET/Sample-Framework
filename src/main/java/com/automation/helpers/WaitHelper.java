package com.automation.helpers;

import com.automation.core.DriverFactory;
import com.automation.core.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;
import java.util.List;

/**
 * WaitHelper for explicit wait strategies and stale element handling
 * Provides robust waiting mechanisms with retry logic
 */
public class WaitHelper {
    private static final Logger logger = LoggerManager.getInstance().getLogger(WaitHelper.class);
    private final WebDriver driver;
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_POLLING = 1;
    
    public WaitHelper() {
        this.driver = DriverFactory.getInstance().getDriver();
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForVisible(By locator) {
        return waitForVisible(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForVisible(By locator, int timeoutSeconds) {
        return waitForElement(locator, timeoutSeconds, ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForClickable(By locator) {
        return waitForClickable(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        return waitForElement(locator, timeoutSeconds, ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be invisible
     */
    public boolean waitForInvisibility(By locator) {
        return waitForInvisibility(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be invisible
     */
    public boolean waitForInvisibility(By locator, int timeoutSeconds) {
        return waitForCondition(ExpectedConditions.invisibilityOfElementLocated(locator), timeoutSeconds);
    }
    
    /**
     * Wait for element to be present
     */
    public WebElement waitForPresence(By locator) {
        return waitForPresence(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be present
     */
    public WebElement waitForPresence(By locator, int timeoutSeconds) {
        return waitForElement(locator, timeoutSeconds, ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element with custom condition
     */
    public WebElement waitForElement(By locator, int timeoutSeconds, Function<WebDriver, WebElement> condition) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING));
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(NoSuchElementException.class);
        
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element: {} after {} seconds", locator, timeoutSeconds);
            throw new RuntimeException("Element not found within timeout: " + locator, e);
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element encountered for: {}, retrying...", locator);
            return retryForStaleElement(locator, timeoutSeconds, condition);
        } catch (NoSuchElementException e) {
            logger.error("No such element found: {}", locator);
            throw new RuntimeException("Element not found: " + locator, e);
        }
    }
    
    /**
     * Wait for condition with timeout
     */
    public <T> T waitForCondition(Function<WebDriver, T> condition, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING));
        
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for condition after {} seconds", timeoutSeconds);
            throw new RuntimeException("Condition not met within timeout", e);
        }
    }
    
    /**
     * Retry for stale element
     */
    private WebElement retryForStaleElement(By locator, int timeoutSeconds, Function<WebDriver, WebElement> condition) {
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                // Wait a bit before retrying
                Thread.sleep(500);
                
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds / 2));
                wait.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING));
                
                return wait.until(condition);
                
            } catch (StaleElementReferenceException e) {
                retryCount++;
                logger.warn("Stale element retry {} for: {}", retryCount, locator);
                
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to find element after " + maxRetries + " stale element retries: " + locator, e);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
        
        throw new RuntimeException("Failed to find element after stale element retries: " + locator);
    }
    
    /**
     * Wait for page load
     */
    public void waitForPageLoad() {
        waitForCondition(driver -> {
            String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            return "complete".equals(readyState);
        }, DEFAULT_TIMEOUT);
        logger.info("Page load completed");
    }
    
    /**
     * Wait for AJAX to complete
     */
    public void waitForAjaxComplete() {
        waitForCondition(driver -> {
            Long activeRequests = (Long) ((JavascriptExecutor) driver)
                .executeScript("return jQuery.active");
            return activeRequests != null && activeRequests == 0;
        }, DEFAULT_TIMEOUT);
        logger.info("AJAX requests completed");
    }
    
    /**
     * Wait for element count to be specific value
     */
    public List<WebElement> waitForElementCount(By locator, int expectedCount) {
        return waitForCondition(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            if (elements.size() == expectedCount) {
                return elements;
            }
            return null;
        }, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for text to be present in element
     */
    public boolean waitForTextPresent(By locator, String text) {
        return waitForCondition(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return element.getText().contains(text);
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public boolean waitForUrlContains(String partialUrl) {
        return waitForCondition(driver -> driver.getCurrentUrl().contains(partialUrl), DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for title to contain specific text
     */
    public boolean waitForTitleContains(String partialTitle) {
        return waitForCondition(driver -> driver.getTitle().contains(partialTitle), DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for alert to be present
     */
    public Alert waitForAlert() {
        return waitForCondition(ExpectedConditions.alertIsPresent(), DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for frame to be available and switch to it
     */
    public void waitForFrameAndSwitch(int frameIndex) {
        waitForCondition(driver -> {
            try {
                driver.switchTo().frame(frameIndex);
                return true;
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
        logger.info("Switched to frame at index: {}", frameIndex);
    }
    
    /**
     * Wait for frame to be available and switch to it
     */
    public void waitForFrameAndSwitch(String frameNameOrId) {
        waitForCondition(driver -> {
            try {
                driver.switchTo().frame(frameNameOrId);
                return true;
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
        logger.info("Switched to frame: {}", frameNameOrId);
    }
    
    /**
     * Wait for element to be selected
     */
    public boolean waitForElementSelected(By locator) {
        return waitForCondition(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return element.isSelected();
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be enabled
     */
    public boolean waitForElementEnabled(By locator) {
        return waitForCondition(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return element.isEnabled();
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be disabled
     */
    public boolean waitForElementDisabled(By locator) {
        return waitForCondition(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return !element.isEnabled();
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);
    }
}
