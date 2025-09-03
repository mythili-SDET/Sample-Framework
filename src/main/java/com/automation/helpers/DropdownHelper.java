package com.automation.helpers;

import com.automation.core.DriverFactory;
import com.automation.core.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * DropdownHelper for dropdown operations
 * Provides methods for selecting options from dropdown elements
 */
public class DropdownHelper {
    private static final Logger logger = LoggerManager.getInstance().getLogger(DropdownHelper.class);
    private final WebDriver driver;
    
    public DropdownHelper() {
        this.driver = DriverFactory.getInstance().getDriver();
    }
    
    /**
     * Select by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.info("Successfully selected option by visible text: {} from dropdown: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to select option by visible text: {} from dropdown: {}", text, locator, e);
            throw new RuntimeException("Select by visible text failed: " + text + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Select by value
     */
    public void selectByValue(By locator, String value) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Successfully selected option by value: {} from dropdown: {}", value, locator);
        } catch (Exception e) {
            logger.error("Failed to select option by value: {} from dropdown: {}", value, locator, e);
            throw new RuntimeException("Select by value failed: " + value + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Select by index
     */
    public void selectByIndex(By locator, int index) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.info("Successfully selected option by index: {} from dropdown: {}", index, locator);
        } catch (Exception e) {
            logger.error("Failed to select option by index: {} from dropdown: {}", index, locator, e);
            throw new RuntimeException("Select by index failed: " + index + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Get selected option text
     */
    public String getSelectedOptionText(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            WebElement selectedOption = select.getFirstSelectedOption();
            String text = selectedOption.getText();
            logger.debug("Got selected option text: {} from dropdown: {}", text, locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get selected option text from dropdown: {}", locator, e);
            throw new RuntimeException("Get selected option text failed: " + locator, e);
        }
    }
    
    /**
     * Get selected option value
     */
    public String getSelectedOptionValue(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            WebElement selectedOption = select.getFirstSelectedOption();
            String value = selectedOption.getAttribute("value");
            logger.debug("Got selected option value: {} from dropdown: {}", value, locator);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get selected option value from dropdown: {}", locator, e);
            throw new RuntimeException("Get selected option value failed: " + locator, e);
        }
    }
    
    /**
     * Get all options
     */
    public List<WebElement> getAllOptions(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            List<WebElement> options = select.getOptions();
            logger.debug("Got {} options from dropdown: {}", options.size(), locator);
            return options;
        } catch (Exception e) {
            logger.error("Failed to get all options from dropdown: {}", locator, e);
            throw new RuntimeException("Get all options failed: " + locator, e);
        }
    }
    
    /**
     * Get all selected options
     */
    public List<WebElement> getAllSelectedOptions(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            List<WebElement> selectedOptions = select.getAllSelectedOptions();
            logger.debug("Got {} selected options from dropdown: {}", selectedOptions.size(), locator);
            return selectedOptions;
        } catch (Exception e) {
            logger.error("Failed to get all selected options from dropdown: {}", locator, e);
            throw new RuntimeException("Get all selected options failed: " + locator, e);
        }
    }
    
    /**
     * Check if dropdown is multiple select
     */
    public boolean isMultiple(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            boolean isMultiple = select.isMultiple();
            logger.debug("Dropdown is multiple select: {} for: {}", isMultiple, locator);
            return isMultiple;
        } catch (Exception e) {
            logger.error("Failed to check if dropdown is multiple select: {}", locator, e);
            throw new RuntimeException("Check multiple select failed: " + locator, e);
        }
    }
    
    /**
     * Deselect by visible text
     */
    public void deselectByVisibleText(By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.deselectByVisibleText(text);
            logger.info("Successfully deselected option by visible text: {} from dropdown: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to deselect option by visible text: {} from dropdown: {}", text, locator, e);
            throw new RuntimeException("Deselect by visible text failed: " + text + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Deselect by value
     */
    public void deselectByValue(By locator, String value) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.deselectByValue(value);
            logger.info("Successfully deselected option by value: {} from dropdown: {}", value, locator);
        } catch (Exception e) {
            logger.error("Failed to deselect option by value: {} from dropdown: {}", value, locator, e);
            throw new RuntimeException("Deselect by value failed: " + value + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Deselect by index
     */
    public void deselectByIndex(By locator, int index) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.deselectByIndex(index);
            logger.info("Successfully deselected option by index: {} from dropdown: {}", index, locator);
        } catch (Exception e) {
            logger.error("Failed to deselect option by index: {} from dropdown: {}", index, locator, e);
            throw new RuntimeException("Deselect by index failed: " + index + " from dropdown: " + locator, e);
        }
    }
    
    /**
     * Deselect all options
     */
    public void deselectAll(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            select.deselectAll();
            logger.info("Successfully deselected all options from dropdown: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to deselect all options from dropdown: {}", locator, e);
            throw new RuntimeException("Deselect all failed: " + locator, e);
        }
    }
    
    /**
     * Get option count
     */
    public int getOptionCount(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Select select = new Select(element);
            int count = select.getOptions().size();
            logger.debug("Got option count: {} from dropdown: {}", count, locator);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get option count from dropdown: {}", locator, e);
            throw new RuntimeException("Get option count failed: " + locator, e);
        }
    }
}
