package com.automation.helpers;

import com.automation.driver.DriverFactory;
import com.automation.logger.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * SeleniumHelper for high-level Selenium operations
 * Provides retry mechanisms and fallback strategies for common exceptions
 */
public class SeleniumHelper {
    private static final Logger logger = LoggerManager.getLogger(SeleniumHelper.class);
    private final WebDriver driver;
    private final Actions actions;
    
    public SeleniumHelper() {
        this.driver = DriverFactory.getDriver();
        this.actions = new Actions(driver);
    }
    
    /**
     * Find element with retry mechanism
     */
    public WebElement findElement(By locator) {
        return findElement(locator, 3);
    }
    
    /**
     * Find element with retry mechanism
     */
    public WebElement findElement(By locator, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement element = driver.findElement(locator);
                logger.debug("Element found: {}", locator);
                return element;
            } catch (StaleElementReferenceException e) {
                retryCount++;
                logger.warn("Stale element on attempt {} for: {}", retryCount, locator);
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to find element after " + maxRetries + " retries: " + locator, e);
                }
            } catch (NoSuchElementException e) {
                logger.error("Element not found: {}", locator);
                throw new RuntimeException("Element not found: " + locator, e);
            }
        }
        throw new RuntimeException("Failed to find element after " + maxRetries + " retries: " + locator);
    }
    
    /**
     * Find all elements
     */
    public List<WebElement> findElements(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            logger.debug("Found {} elements for: {}", elements.size(), locator);
            return elements;
        } catch (Exception e) {
            logger.error("Error finding elements: {}", locator, e);
            throw new RuntimeException("Failed to find elements: " + locator, e);
        }
    }
    
    /**
     * Click element with retry mechanism
     */
    public void click(By locator) {
        LoggerManager.logUIOperation("CLICK", locator.toString(), null);
        click(locator, 3);
    }
    
    /**
     * Click element with retry mechanism
     */
    public void click(By locator, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement element = findElement(locator);
                element.click();
                logger.info("Successfully clicked element: {}", locator);
                return;
            } catch (StaleElementReferenceException e) {
                retryCount++;
                logger.warn("Stale element on click attempt {} for: {}", retryCount, locator);
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to click element after " + maxRetries + " retries: " + locator, e);
                }
            } catch (ElementClickInterceptedException e) {
                retryCount++;
                logger.warn("Element click intercepted on attempt {} for: {}", retryCount, locator);
                if (retryCount >= maxRetries) {
                    // Try JavaScript click as fallback
                    try {
                        WebElement element = driver.findElement(locator);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        logger.info("Successfully clicked element using JavaScript: {}", locator);
                        return;
                    } catch (Exception jsEx) {
                        throw new RuntimeException("Failed to click element after JavaScript fallback: " + locator, e);
                    }
                }
            } catch (Exception e) {
                logger.error("Error clicking element: {}", locator, e);
                throw new RuntimeException("Failed to click element: " + locator, e);
            }
        }
    }
    
    /**
     * Type text with retry mechanism
     */
    public void type(By locator, String text) {
        LoggerManager.logUIOperation("TYPE", locator.toString(), text);
        type(locator, text, 3);
    }
    
    /**
     * Type text with retry mechanism
     */
    public void type(By locator, String text, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement element = findElement(locator);
                element.clear();
                element.sendKeys(text);
                logger.info("Successfully typed text: {} into element: {}", text, locator);
                return;
            } catch (StaleElementReferenceException e) {
                retryCount++;
                logger.warn("Stale element on type attempt {} for: {}", retryCount, locator);
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to type text after " + maxRetries + " retries: " + locator, e);
                }
            } catch (Exception e) {
                logger.error("Error typing text: {} into element: {}", text, locator, e);
                throw new RuntimeException("Failed to type text: " + text + " into element: " + locator, e);
            }
        }
    }
    
    /**
     * Clear element
     */
    public void clear(By locator) {
        try {
            WebElement element = findElement(locator);
            element.clear();
            logger.info("Successfully cleared element: {}", locator);
        } catch (Exception e) {
            logger.error("Error clearing element: {}", locator, e);
            throw new RuntimeException("Failed to clear element: " + locator, e);
        }
    }
    
    /**
     * Get element text
     */
    public String getText(By locator) {
        try {
            WebElement element = findElement(locator);
            String text = element.getText();
            logger.debug("Got text from element: {} = {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("Error getting text from element: {}", locator, e);
            throw new RuntimeException("Failed to get text from element: " + locator, e);
        }
    }
    
    /**
     * Get element attribute
     */
    public String getAttribute(By locator, String attribute) {
        try {
            WebElement element = findElement(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Got attribute {} from element: {} = {}", attribute, locator, value);
            return value;
        } catch (Exception e) {
            logger.error("Error getting attribute {} from element: {}", attribute, locator, e);
            throw new RuntimeException("Failed to get attribute " + attribute + " from element: " + locator, e);
        }
    }
    
    /**
     * Check if element is present
     */
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Check if element is displayed
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    public boolean isElementEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is selected
     */
    public boolean isElementSelected(By locator) {
        try {
            return driver.findElement(locator).isSelected();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Submit form
     */
    public void submit(By locator) {
        try {
            WebElement element = findElement(locator);
            element.submit();
            logger.info("Successfully submitted form element: {}", locator);
        } catch (Exception e) {
            logger.error("Error submitting form element: {}", locator, e);
            throw new RuntimeException("Failed to submit form element: " + locator, e);
        }
    }
    
    /**
     * Get element size
     */
    public org.openqa.selenium.Dimension getElementSize(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.getSize();
        } catch (Exception e) {
            logger.error("Error getting size of element: {}", locator, e);
            throw new RuntimeException("Failed to get size of element: " + locator, e);
        }
    }
    
    /**
     * Get element location
     */
    public org.openqa.selenium.Point getElementLocation(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.getLocation();
        } catch (Exception e) {
            logger.error("Error getting location of element: {}", locator, e);
            throw new RuntimeException("Failed to get location of element: " + locator, e);
        }
    }
    
    /**
     * Get element tag name
     */
    public String getElementTagName(By locator) {
        try {
            WebElement element = findElement(locator);
            return element.getTagName();
        } catch (Exception e) {
            logger.error("Error getting tag name of element: {}", locator, e);
            throw new RuntimeException("Failed to get tag name of element: " + locator, e);
        }
    }
    
    /**
     * Get element CSS value
     */
    public String getElementCssValue(By locator, String propertyName) {
        try {
            WebElement element = findElement(locator);
            return element.getCssValue(propertyName);
        } catch (Exception e) {
            logger.error("Error getting CSS value {} of element: {}", propertyName, locator, e);
            throw new RuntimeException("Failed to get CSS value " + propertyName + " of element: " + locator, e);
        }
    }
}
