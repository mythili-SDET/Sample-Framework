package com.automation.helpers;

import com.automation.core.DriverFactory;
import com.automation.core.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

/**
 * ActionHelper for advanced user interactions using Selenium Actions
 * Provides mouse and keyboard actions with fallback mechanisms
 */
public class ActionHelper {
    private static final Logger logger = LoggerManager.getInstance().getLogger(ActionHelper.class);
    private final WebDriver driver;
    private final Actions actions;
    
    public ActionHelper() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.actions = new Actions(driver);
    }
    
    /**
     * Click element using Actions
     */
    public void clickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.click(element).perform();
            logger.info("Successfully clicked element using Actions: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element using Actions: {}", locator, e);
            throw new RuntimeException("Actions click failed: " + locator, e);
        }
    }
    
    /**
     * Double click element using Actions
     */
    public void doubleClickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.doubleClick(element).perform();
            logger.info("Successfully double-clicked element using Actions: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to double-click element using Actions: {}", locator, e);
            throw new RuntimeException("Actions double-click failed: " + locator, e);
        }
    }
    
    /**
     * Right click element using Actions
     */
    public void rightClickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.contextClick(element).perform();
            logger.info("Successfully right-clicked element using Actions: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to right-click element using Actions: {}", locator, e);
            throw new RuntimeException("Actions right-click failed: " + locator, e);
        }
    }
    
    /**
     * Hover over element using Actions
     */
    public void hoverOverElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.moveToElement(element).perform();
            logger.info("Successfully hovered over element using Actions: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element using Actions: {}", locator, e);
            throw new RuntimeException("Actions hover failed: " + locator, e);
        }
    }
    
    /**
     * Type text using Actions
     */
    public void typeText(By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            actions.click(element).sendKeys(text).perform();
            logger.info("Successfully typed text using Actions: {} into element: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to type text using Actions: {} into element: {}", text, locator, e);
            throw new RuntimeException("Actions type failed: " + text + " into element: " + locator, e);
        }
    }
    
    /**
     * Clear text using Actions
     */
    public void clearText(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.click(element).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
            logger.info("Successfully cleared text using Actions from element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to clear text using Actions from element: {}", locator, e);
            throw new RuntimeException("Actions clear failed: " + locator, e);
        }
    }
    
    /**
     * Drag and drop element
     */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        try {
            WebElement source = driver.findElement(sourceLocator);
            WebElement target = driver.findElement(targetLocator);
            actions.dragAndDrop(source, target).perform();
            logger.info("Successfully dragged element: {} to target: {}", sourceLocator, targetLocator);
        } catch (Exception e) {
            logger.error("Failed to drag and drop element: {} to target: {}", sourceLocator, targetLocator, e);
            throw new RuntimeException("Drag and drop failed: " + sourceLocator + " to " + targetLocator, e);
        }
    }
    
    /**
     * Drag and drop by offset
     */
    public void dragAndDropBy(By locator, int xOffset, int yOffset) {
        try {
            WebElement element = driver.findElement(locator);
            actions.dragAndDropBy(element, xOffset, yOffset).perform();
            logger.info("Successfully dragged element: {} by offset ({}, {})", locator, xOffset, yOffset);
        } catch (Exception e) {
            logger.error("Failed to drag element: {} by offset ({}, {})", locator, xOffset, yOffset, e);
            throw new RuntimeException("Drag by offset failed: " + locator + " by (" + xOffset + ", " + yOffset + ")", e);
        }
    }
    
    /**
     * Move to element by offset
     */
    public void moveToElementByOffset(By locator, int xOffset, int yOffset) {
        try {
            WebElement element = driver.findElement(locator);
            actions.moveToElement(element, xOffset, yOffset).perform();
            logger.info("Successfully moved to element: {} with offset ({}, {})", locator, xOffset, yOffset);
        } catch (Exception e) {
            logger.error("Failed to move to element: {} with offset ({}, {})", locator, xOffset, yOffset, e);
            throw new RuntimeException("Move to element by offset failed: " + locator + " with (" + xOffset + ", " + yOffset + ")", e);
        }
    }
    
    /**
     * Press key
     */
    public void pressKey(Keys key) {
        try {
            actions.keyDown(key).perform();
            logger.info("Successfully pressed key: {}", key);
        } catch (Exception e) {
            logger.error("Failed to press key: {}", key, e);
            throw new RuntimeException("Key press failed: " + key, e);
        }
    }
    
    /**
     * Release key
     */
    public void releaseKey(Keys key) {
        try {
            actions.keyUp(key).perform();
            logger.info("Successfully released key: {}", key);
        } catch (Exception e) {
            logger.error("Failed to release key: {}", key, e);
            throw new RuntimeException("Key release failed: " + key, e);
        }
    }
    
    /**
     * Send keys to element
     */
    public void sendKeysToElement(By locator, CharSequence... keys) {
        try {
            WebElement element = driver.findElement(locator);
            actions.sendKeys(element, keys).perform();
            logger.info("Successfully sent keys to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", locator, e);
            throw new RuntimeException("Send keys failed: " + locator, e);
        }
    }
    
    /**
     * Click and hold element
     */
    public void clickAndHold(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            actions.clickAndHold(element).perform();
            logger.info("Successfully clicked and held element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click and hold element: {}", locator, e);
            throw new RuntimeException("Click and hold failed: " + locator, e);
        }
    }
    
    /**
     * Release click and hold
     */
    public void releaseClickAndHold() {
        try {
            actions.release().perform();
            logger.info("Successfully released click and hold");
        } catch (Exception e) {
            logger.error("Failed to release click and hold", e);
            throw new RuntimeException("Release click and hold failed", e);
        }
    }
    
    /**
     * Move by offset
     */
    public void moveByOffset(int xOffset, int yOffset) {
        try {
            actions.moveByOffset(xOffset, yOffset).perform();
            logger.info("Successfully moved by offset ({}, {})", xOffset, yOffset);
        } catch (Exception e) {
            logger.error("Failed to move by offset ({}, {})", xOffset, yOffset, e);
            throw new RuntimeException("Move by offset failed: (" + xOffset + ", " + yOffset + ")", e);
        }
    }
    
    /**
     * Build and perform actions
     */
    public void buildAndPerform() {
        try {
            actions.build().perform();
            logger.info("Successfully built and performed actions");
        } catch (Exception e) {
            logger.error("Failed to build and perform actions", e);
            throw new RuntimeException("Build and perform actions failed", e);
        }
    }
    
    /**
     * Reset actions
     */
    public void resetActions() {
        try {
            // Create new Actions instance to reset
            new Actions(driver);
            logger.info("Successfully reset actions");
        } catch (Exception e) {
            logger.error("Failed to reset actions", e);
            throw new RuntimeException("Reset actions failed", e);
        }
    }
}
