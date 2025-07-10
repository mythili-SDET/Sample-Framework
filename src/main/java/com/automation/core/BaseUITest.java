package com.automation.core;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class for UI automation tests
 * Provides common WebDriver operations and utilities
 */
public class BaseUITest {
    protected static final Logger logger = LogManager.getLogger(BaseUITest.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected ConfigManager config;

    public BaseUITest() {
        this.config = ConfigManager.getInstance();
        this.driver = WebDriverManager.getDriver();
        this.wait = WebDriverManager.getWait();
        this.actions = new Actions(driver);
    }

    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
    }

    /**
     * Navigate to base URL with path
     */
    protected void navigateToPath(String path) {
        String fullUrl = config.getUIBaseUrl() + path;
        navigateTo(fullUrl);
    }

    /**
     * Find element with explicit wait
     */
    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Find element with custom timeout
     */
    protected WebElement findElement(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Click element with explicit wait
     */
    protected void click(By locator) {
        logger.info("Clicking element: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Click element with JavaScript
     */
    protected void clickWithJS(By locator) {
        logger.info("Clicking element with JavaScript: {}", locator);
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Send keys to element
     */
    protected void sendKeys(By locator, String text) {
        logger.info("Sending keys to element {}: {}", locator, text);
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element
     */
    protected String getText(By locator) {
        return findElement(locator).getText();
    }

    /**
     * Get attribute value
     */
    protected String getAttribute(By locator, String attribute) {
        return findElement(locator).getAttribute(attribute);
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return findElement(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            return findElement(locator).isEnabled();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Wait for element to be visible
     */
    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    protected void waitForElementClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to disappear
     */
    protected void waitForElementInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Select option by visible text
     */
    protected void selectByVisibleText(By locator, String text) {
        Select select = new Select(findElement(locator));
        select.selectByVisibleText(text);
    }

    /**
     * Select option by value
     */
    protected void selectByValue(By locator, String value) {
        Select select = new Select(findElement(locator));
        select.selectByValue(value);
    }

    /**
     * Select option by index
     */
    protected void selectByIndex(By locator, int index) {
        Select select = new Select(findElement(locator));
        select.selectByIndex(index);
    }

    /**
     * Hover over element
     */
    protected void hoverOver(By locator) {
        actions.moveToElement(findElement(locator)).perform();
    }

    /**
     * Drag and drop
     */
    protected void dragAndDrop(By source, By target) {
        actions.dragAndDrop(findElement(source), findElement(target)).perform();
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Switch to frame
     */
    protected void switchToFrame(By locator) {
        driver.switchTo().frame(findElement(locator));
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /**
     * Switch to window by title
     */
    protected void switchToWindowByTitle(String title) {
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains(title)) {
                break;
            }
        }
    }

    /**
     * Accept alert
     */
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    /**
     * Get alert text
     */
    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    /**
     * Take screenshot
     */
    protected String takeScreenshot(String testName) {
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

    /**
     * Assert element is displayed
     */
    protected void assertElementDisplayed(By locator, String message) {
        Assert.assertTrue(isElementDisplayed(locator), message);
    }

    /**
     * Assert element is not displayed
     */
    protected void assertElementNotDisplayed(By locator, String message) {
        Assert.assertFalse(isElementDisplayed(locator), message);
    }

    /**
     * Assert text equals
     */
    protected void assertTextEquals(By locator, String expectedText, String message) {
        String actualText = getText(locator);
        Assert.assertEquals(actualText, expectedText, message);
    }

    /**
     * Assert text contains
     */
    protected void assertTextContains(By locator, String expectedText, String message) {
        String actualText = getText(locator);
        Assert.assertTrue(actualText.contains(expectedText), message);
    }

    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Go back
     */
    protected void goBack() {
        driver.navigate().back();
    }

    /**
     * Go forward
     */
    protected void goForward() {
        driver.navigate().forward();
    }
}