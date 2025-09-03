package com.automation.helpers;

import com.automation.core.DriverFactory;
import com.automation.core.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * JavaScriptHelper for advanced browser interactions
 * Provides JavaScript-based automation methods for complex scenarios
 */
public class JavaScriptHelper {
    private static final Logger logger = LoggerManager.getInstance().getLogger(JavaScriptHelper.class);
    private final WebDriver driver;
    private final JavascriptExecutor js;

    public JavaScriptHelper() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.js = (JavascriptExecutor) driver;
    }

    // ===========================================
    // ELEMENT INTERACTION METHODS
    // ===========================================

    /**
     * Click element using JavaScript
     */
    public void clickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].click();", element);
            logger.info("JavaScript click successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript click failed for: {}", locator, e);
            throw new RuntimeException("JavaScript click failed: " + locator, e);
        }
    }

    /**
     * Click element using JavaScript
     */
    public void clickElement(WebElement element) {
        try {
            js.executeScript("arguments[0].click();", element);
            logger.info("JavaScript click successful for element");
        } catch (Exception e) {
            logger.error("JavaScript click failed for element", e);
            throw new RuntimeException("JavaScript click failed", e);
        }
    }

    /**
     * Type text using JavaScript
     */
    public void typeText(By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].value = '';", element);
            js.executeScript("arguments[0].value = arguments[1];", element, text);
            logger.info("JavaScript type successful for: {} with text: {}", locator, text);
        } catch (Exception e) {
            logger.error("JavaScript type failed for: {}", locator, e);
            throw new RuntimeException("JavaScript type failed: " + locator, e);
        }
    }

    /**
     * Type text using JavaScript
     */
    public void typeText(WebElement element, String text) {
        try {
            js.executeScript("arguments[0].value = '';", element);
            js.executeScript("arguments[0].value = arguments[1];", element, text);
            logger.info("JavaScript type successful with text: {}", text);
        } catch (Exception e) {
            logger.error("JavaScript type failed", e);
            throw new RuntimeException("JavaScript type failed", e);
        }
    }

    /**
     * Clear element using JavaScript
     */
    public void clearElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].value = '';", element);
            logger.info("JavaScript clear successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript clear failed for: {}", locator, e);
            throw new RuntimeException("JavaScript clear failed: " + locator, e);
        }
    }

    /**
     * Get element text using JavaScript
     */
    public String getElementText(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            String text = (String) js.executeScript("return arguments[0].textContent;", element);
            logger.info("JavaScript get text successful for: {} - {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("JavaScript get text failed for: {}", locator, e);
            throw new RuntimeException("JavaScript get text failed: " + locator, e);
        }
    }

    /**
     * Get element attribute using JavaScript
     */
    public String getElementAttribute(By locator, String attribute) {
        try {
            WebElement element = driver.findElement(locator);
            String value = (String) js.executeScript("return arguments[0].getAttribute(arguments[1]);", element, attribute);
            logger.info("JavaScript get attribute successful for: {} - {} = {}", locator, attribute, value);
            return value;
        } catch (Exception e) {
            logger.error("JavaScript get attribute failed for: {}", locator, e);
            throw new RuntimeException("JavaScript get attribute failed: " + locator, e);
        }
    }

    // ===========================================
    // SCROLLING METHODS
    // ===========================================

    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("JavaScript scroll to element successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript scroll to element failed for: {}", locator, e);
            throw new RuntimeException("JavaScript scroll to element failed: " + locator, e);
        }
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("JavaScript scroll to element successful");
        } catch (Exception e) {
            logger.error("JavaScript scroll to element failed", e);
            throw new RuntimeException("JavaScript scroll to element failed", e);
        }
    }

    /**
     * Scroll to top of page
     */
    public void scrollToTop() {
        try {
            js.executeScript("window.scrollTo(0, 0);");
            logger.info("JavaScript scroll to top successful");
        } catch (Exception e) {
            logger.error("JavaScript scroll to top failed", e);
            throw new RuntimeException("JavaScript scroll to top failed", e);
        }
    }

    /**
     * Scroll to bottom of page
     */
    public void scrollToBottom() {
        try {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logger.info("JavaScript scroll to bottom successful");
        } catch (Exception e) {
            logger.error("JavaScript scroll to bottom failed", e);
            throw new RuntimeException("JavaScript scroll to bottom failed", e);
        }
    }

    /**
     * Scroll by pixels
     */
    public void scrollBy(int x, int y) {
        try {
            js.executeScript("window.scrollBy(" + x + ", " + y + ");");
            logger.info("JavaScript scroll by ({}, {}) successful", x, y);
        } catch (Exception e) {
            logger.error("JavaScript scroll by ({}, {}) failed", x, y, e);
            throw new RuntimeException("JavaScript scroll by failed", e);
        }
    }

    // ===========================================
    // ELEMENT VISIBILITY AND STATE METHODS
    // ===========================================

    /**
     * Check if element is visible using JavaScript
     */
    public boolean isElementVisible(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Boolean isVisible = (Boolean) js.executeScript(
                "return arguments[0].offsetWidth > 0 && arguments[0].offsetHeight > 0 && " +
                "window.getComputedStyle(arguments[0]).visibility !== 'hidden' && " +
                "window.getComputedStyle(arguments[0]).display !== 'none';", element);
            logger.debug("JavaScript element visibility check for: {} = {}", locator, isVisible);
            return isVisible;
        } catch (Exception e) {
            logger.debug("JavaScript element visibility check failed for: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is enabled using JavaScript
     */
    public boolean isElementEnabled(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Boolean isEnabled = (Boolean) js.executeScript("return !arguments[0].disabled;", element);
            logger.debug("JavaScript element enabled check for: {} = {}", locator, isEnabled);
            return isEnabled;
        } catch (Exception e) {
            logger.debug("JavaScript element enabled check failed for: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is selected using JavaScript
     */
    public boolean isElementSelected(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Boolean isSelected = (Boolean) js.executeScript("return arguments[0].checked;", element);
            logger.debug("JavaScript element selected check for: {} = {}", locator, isSelected);
            return isSelected;
        } catch (Exception e) {
            logger.debug("JavaScript element selected check failed for: {}", locator);
            return false;
        }
    }

    // ===========================================
    // ELEMENT HIGHLIGHTING METHODS
    // ===========================================

    /**
     * Highlight element with border
     */
    public void highlightElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].style.border='3px solid red'", element);
            logger.info("JavaScript highlight successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript highlight failed for: {}", locator, e);
            throw new RuntimeException("JavaScript highlight failed: " + locator, e);
        }
    }

    /**
     * Highlight element with border
     */
    public void highlightElement(WebElement element) {
        try {
            js.executeScript("arguments[0].style.border='3px solid red'", element);
            logger.info("JavaScript highlight successful");
        } catch (Exception e) {
            logger.error("JavaScript highlight failed", e);
            throw new RuntimeException("JavaScript highlight failed", e);
        }
    }

    /**
     * Remove highlight from element
     */
    public void removeHighlight(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].style.border=''", element);
            logger.info("JavaScript remove highlight successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript remove highlight failed for: {}", locator, e);
            throw new RuntimeException("JavaScript remove highlight failed: " + locator, e);
        }
    }

    /**
     * Remove highlight from element
     */
    public void removeHighlight(WebElement element) {
        try {
            js.executeScript("arguments[0].style.border=''", element);
            logger.info("JavaScript remove highlight successful");
        } catch (Exception e) {
            logger.error("JavaScript remove highlight failed", e);
            throw new RuntimeException("JavaScript remove highlight failed", e);
        }
    }

    // ===========================================
    // FORM INTERACTION METHODS
    // ===========================================

    /**
     * Select option from dropdown using JavaScript
     */
    public void selectOptionByValue(By locator, String value) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].value = arguments[1];", element, value);
            // Trigger change event
            js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
            logger.info("JavaScript select option successful for: {} with value: {}", locator, value);
        } catch (Exception e) {
            logger.error("JavaScript select option failed for: {}", locator, e);
            throw new RuntimeException("JavaScript select option failed: " + locator, e);
        }
    }

    /**
     * Check checkbox using JavaScript
     */
    public void checkCheckbox(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].checked = true;", element);
            js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
            logger.info("JavaScript check checkbox successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript check checkbox failed for: {}", locator, e);
            throw new RuntimeException("JavaScript check checkbox failed: " + locator, e);
        }
    }

    /**
     * Uncheck checkbox using JavaScript
     */
    public void uncheckCheckbox(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].checked = false;", element);
            js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
            logger.info("JavaScript uncheck checkbox successful for: {}", locator);
        } catch (Exception e) {
            logger.error("JavaScript uncheck checkbox failed for: {}", locator, e);
            throw new RuntimeException("JavaScript uncheck checkbox failed: " + locator, e);
        }
    }

    // ===========================================
    // BROWSER INTERACTION METHODS
    // ===========================================

    /**
     * Refresh page using JavaScript
     */
    public void refreshPage() {
        try {
            js.executeScript("location.reload();");
            logger.info("JavaScript page refresh successful");
        } catch (Exception e) {
            logger.error("JavaScript page refresh failed", e);
            throw new RuntimeException("JavaScript page refresh failed", e);
        }
    }

    /**
     * Navigate back using JavaScript
     */
    public void goBack() {
        try {
            js.executeScript("history.back();");
            logger.info("JavaScript go back successful");
        } catch (Exception e) {
            logger.error("JavaScript go back failed", e);
            throw new RuntimeException("JavaScript go back failed", e);
        }
    }

    /**
     * Navigate forward using JavaScript
     */
    public void goForward() {
        try {
            js.executeScript("history.forward();");
            logger.info("JavaScript go forward successful");
        } catch (Exception e) {
            logger.error("JavaScript go forward failed", e);
            throw new RuntimeException("JavaScript go forward failed", e);
        }
    }

    /**
     * Get page title using JavaScript
     */
    public String getPageTitle() {
        try {
            String title = (String) js.executeScript("return document.title;");
            logger.info("JavaScript get page title successful: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("JavaScript get page title failed", e);
            throw new RuntimeException("JavaScript get page title failed", e);
        }
    }

    /**
     * Get current URL using JavaScript
     */
    public String getCurrentUrl() {
        try {
            String url = (String) js.executeScript("return window.location.href;");
            logger.info("JavaScript get current URL successful: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("JavaScript get current URL failed", e);
            throw new RuntimeException("JavaScript get current URL failed", e);
        }
    }

    // ===========================================
    // CUSTOM JAVASCRIPT EXECUTION
    // ===========================================

    /**
     * Execute custom JavaScript
     */
    public Object executeScript(String script, Object... args) {
        try {
            Object result = js.executeScript(script, args);
            logger.info("Custom JavaScript execution successful: {}", script);
            return result;
        } catch (Exception e) {
            logger.error("Custom JavaScript execution failed: {}", script, e);
            throw new RuntimeException("Custom JavaScript execution failed: " + script, e);
        }
    }

    /**
     * Execute custom JavaScript with WebElement
     */
    public Object executeScriptWithElement(String script, WebElement element) {
        try {
            Object result = js.executeScript(script, element);
            logger.info("Custom JavaScript execution with element successful: {}", script);
            return result;
        } catch (Exception e) {
            logger.error("Custom JavaScript execution with element failed: {}", script, e);
            throw new RuntimeException("Custom JavaScript execution with element failed: " + script, e);
        }
    }

    // ===========================================
    // UTILITY METHODS
    // ===========================================

    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        try {
            js.executeScript("return document.readyState").equals("complete");
            logger.info("JavaScript wait for page load successful");
        } catch (Exception e) {
            logger.error("JavaScript wait for page load failed", e);
            throw new RuntimeException("JavaScript wait for page load failed", e);
        }
    }

    /**
     * Get all elements by tag name using JavaScript
     */
    public List<WebElement> getElementsByTagName(String tagName) {
        try {
            @SuppressWarnings("unchecked")
            List<WebElement> elements = (List<WebElement>) js.executeScript(
                "return document.getElementsByTagName(arguments[0]);", tagName);
            logger.info("JavaScript get elements by tag name successful: {} - found {} elements", tagName, elements.size());
            return elements;
        } catch (Exception e) {
            logger.error("JavaScript get elements by tag name failed: {}", tagName, e);
            throw new RuntimeException("JavaScript get elements by tag name failed: " + tagName, e);
        }
    }

    /**
     * Get element by CSS selector using JavaScript
     */
    public WebElement getElementByCssSelector(String cssSelector) {
        try {
            WebElement element = (WebElement) js.executeScript(
                "return document.querySelector(arguments[0]);", cssSelector);
            logger.info("JavaScript get element by CSS selector successful: {}", cssSelector);
            return element;
        } catch (Exception e) {
            logger.error("JavaScript get element by CSS selector failed: {}", cssSelector, e);
            throw new RuntimeException("JavaScript get element by CSS selector failed: " + cssSelector, e);
        }
    }

    /**
     * Get all elements by CSS selector using JavaScript
     */
    public List<WebElement> getElementsByCssSelector(String cssSelector) {
        try {
            @SuppressWarnings("unchecked")
            List<WebElement> elements = (List<WebElement>) js.executeScript(
                "return document.querySelectorAll(arguments[0]);", cssSelector);
            logger.info("JavaScript get elements by CSS selector successful: {} - found {} elements", cssSelector, elements.size());
            return elements;
        } catch (Exception e) {
            logger.error("JavaScript get elements by CSS selector failed: {}", cssSelector, e);
            throw new RuntimeException("JavaScript get elements by CSS selector failed: " + cssSelector, e);
        }
    }
}
