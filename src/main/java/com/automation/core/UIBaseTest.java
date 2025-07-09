package com.automation.core;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Base class for UI testing with Selenium WebDriver
 * Provides common functionality for web automation
 */
public class UIBaseTest {
    private static final Logger logger = LogManager.getLogger(UIBaseTest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    @BeforeMethod
    public void setUp() {
        logger.info("Starting UI test setup");
        
        // Initialize WebDriver
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        
        // Initialize WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        
        // Initialize Actions
        actions = new Actions(driver);
        
        // Navigate to base URL
        String baseUrl = config.getBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty()) {
            navigateTo(baseUrl);
        }
        
        logger.info("UI test setup completed");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        logger.info("Starting UI test teardown");
        
        // Take screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getMethod().getMethodName());
        }
        
        // Quit WebDriver
        DriverManager.quitDriver();
        
        logger.info("UI test teardown completed");
    }

    /**
     * Navigate to URL
     * @param url Target URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    /**
     * Find element with explicit wait
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findElement(By locator) {
        logger.debug("Finding element: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find clickable element with explicit wait
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findClickableElement(By locator) {
        logger.debug("Finding clickable element: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Find visible element with explicit wait
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findVisibleElement(By locator) {
        logger.debug("Finding visible element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Find multiple elements
     * @param locator Element locator
     * @return List of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        logger.debug("Finding elements: {}", locator);
        return driver.findElements(locator);
    }

    /**
     * Click element
     * @param locator Element locator
     */
    protected void click(By locator) {
        WebElement element = findClickableElement(locator);
        logger.info("Clicking element: {}", locator);
        element.click();
    }

    /**
     * Click element using JavaScript
     * @param locator Element locator
     */
    protected void clickUsingJS(By locator) {
        WebElement element = findElement(locator);
        logger.info("Clicking element using JavaScript: {}", locator);
        executeScript("arguments[0].click();", element);
    }

    /**
     * Double click element
     * @param locator Element locator
     */
    protected void doubleClick(By locator) {
        WebElement element = findClickableElement(locator);
        logger.info("Double clicking element: {}", locator);
        actions.doubleClick(element).perform();
    }

    /**
     * Right click element
     * @param locator Element locator
     */
    protected void rightClick(By locator) {
        WebElement element = findClickableElement(locator);
        logger.info("Right clicking element: {}", locator);
        actions.contextClick(element).perform();
    }

    /**
     * Type text into element
     * @param locator Element locator
     * @param text Text to type
     */
    protected void type(By locator, String text) {
        WebElement element = findVisibleElement(locator);
        logger.info("Typing text '{}' into element: {}", text, locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Type text into element using JavaScript
     * @param locator Element locator
     * @param text Text to type
     */
    protected void typeUsingJS(By locator, String text) {
        WebElement element = findElement(locator);
        logger.info("Typing text '{}' using JavaScript into element: {}", text, locator);
        executeScript("arguments[0].value = arguments[1];", element, text);
    }

    /**
     * Get text from element
     * @param locator Element locator
     * @return Element text
     */
    protected String getText(By locator) {
        WebElement element = findVisibleElement(locator);
        String text = element.getText();
        logger.debug("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Get attribute value from element
     * @param locator Element locator
     * @param attributeName Attribute name
     * @return Attribute value
     */
    protected String getAttribute(By locator, String attributeName) {
        WebElement element = findElement(locator);
        String value = element.getAttribute(attributeName);
        logger.debug("Retrieved attribute '{}' value '{}' from element: {}", attributeName, value, locator);
        return value;
    }

    /**
     * Check if element is displayed
     * @param locator Element locator
     * @return true if displayed, false otherwise
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean displayed = element.isDisplayed();
            logger.debug("Element {} is displayed: {}", locator, displayed);
            return displayed;
        } catch (NoSuchElementException e) {
            logger.debug("Element {} not found", locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @param locator Element locator
     * @return true if enabled, false otherwise
     */
    protected boolean isEnabled(By locator) {
        WebElement element = findElement(locator);
        boolean enabled = element.isEnabled();
        logger.debug("Element {} is enabled: {}", locator, enabled);
        return enabled;
    }

    /**
     * Check if element is selected
     * @param locator Element locator
     * @return true if selected, false otherwise
     */
    protected boolean isSelected(By locator) {
        WebElement element = findElement(locator);
        boolean selected = element.isSelected();
        logger.debug("Element {} is selected: {}", locator, selected);
        return selected;
    }

    /**
     * Select dropdown option by visible text
     * @param locator Dropdown locator
     * @param text Option text
     */
    protected void selectByText(By locator, String text) {
        WebElement dropdown = findElement(locator);
        Select select = new Select(dropdown);
        logger.info("Selecting option '{}' from dropdown: {}", text, locator);
        select.selectByVisibleText(text);
    }

    /**
     * Select dropdown option by value
     * @param locator Dropdown locator
     * @param value Option value
     */
    protected void selectByValue(By locator, String value) {
        WebElement dropdown = findElement(locator);
        Select select = new Select(dropdown);
        logger.info("Selecting option with value '{}' from dropdown: {}", value, locator);
        select.selectByValue(value);
    }

    /**
     * Select dropdown option by index
     * @param locator Dropdown locator
     * @param index Option index
     */
    protected void selectByIndex(By locator, int index) {
        WebElement dropdown = findElement(locator);
        Select select = new Select(dropdown);
        logger.info("Selecting option at index {} from dropdown: {}", index, locator);
        select.selectByIndex(index);
    }

    /**
     * Hover over element
     * @param locator Element locator
     */
    protected void hover(By locator) {
        WebElement element = findElement(locator);
        logger.info("Hovering over element: {}", locator);
        actions.moveToElement(element).perform();
    }

    /**
     * Drag and drop
     * @param sourceLocator Source element locator
     * @param targetLocator Target element locator
     */
    protected void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = findElement(sourceLocator);
        WebElement target = findElement(targetLocator);
        logger.info("Dragging from {} to {}", sourceLocator, targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    /**
     * Scroll to element
     * @param locator Element locator
     */
    protected void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        logger.info("Scrolling to element: {}", locator);
        executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        logger.info("Scrolling to bottom of page");
        executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scroll to top of page
     */
    protected void scrollToTop() {
        logger.info("Scrolling to top of page");
        executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Execute JavaScript
     * @param script JavaScript code
     * @param args Script arguments
     * @return Script result
     */
    protected Object executeScript(String script, Object... args) {
        logger.debug("Executing JavaScript: {}", script);
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Wait for element to be visible
     * @param locator Element locator
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementVisible(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        logger.debug("Waiting for element to be visible: {}", locator);
        customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementClickable(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        logger.debug("Waiting for element to be clickable: {}", locator);
        customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for text to be present in element
     * @param locator Element locator
     * @param text Expected text
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForTextInElement(By locator, String text, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        logger.debug("Waiting for text '{}' in element: {}", text, locator);
        customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Switch to window by title
     * @param title Window title
     */
    protected void switchToWindowByTitle(String title) {
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
            if (driver.getTitle().contains(title)) {
                logger.info("Switched to window with title: {}", title);
                return;
            }
        }
        logger.error("Window with title '{}' not found", title);
        throw new RuntimeException("Window not found: " + title);
    }

    /**
     * Switch to frame
     * @param frameLocator Frame locator
     */
    protected void switchToFrame(By frameLocator) {
        WebElement frame = findElement(frameLocator);
        logger.info("Switching to frame: {}", frameLocator);
        driver.switchTo().frame(frame);
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        logger.info("Switching to default content");
        driver.switchTo().defaultContent();
    }

    /**
     * Accept alert
     */
    protected void acceptAlert() {
        logger.info("Accepting alert");
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        logger.info("Dismissing alert");
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    /**
     * Get alert text
     * @return Alert text
     */
    protected String getAlertText() {
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();
        logger.info("Alert text: {}", alertText);
        return alertText;
    }

    /**
     * Capture screenshot
     * @param testName Test name for screenshot file
     * @return Screenshot file path
     */
    protected String captureScreenshot(String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String screenshotPath = config.getScreenshotPath() + fileName;
            
            File destFile = new File(screenshotPath);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Screenshot captured: {}", screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Get current page title
     * @return Page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Current page title: {}", title);
        return title;
    }

    /**
     * Get current page URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
    }

    /**
     * Navigate back
     */
    protected void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
    }

    /**
     * Navigate forward
     */
    protected void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
    }
}