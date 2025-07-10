package com.automation.ui.pages;

import com.automation.core.BaseUITest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base Page Object class
 * Provides common functionality for all page objects
 */
public abstract class BasePage extends BaseUITest {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        super();
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for page to load
     */
    protected void waitForPageLoad() {
        logger.info("Waiting for page to load");
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for page title to contain text
     */
    protected void waitForPageTitle(String title) {
        logger.info("Waiting for page title to contain: {}", title);
        wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * Wait for page title to be exact
     */
    protected void waitForPageTitleExact(String title) {
        logger.info("Waiting for page title to be: {}", title);
        wait.until(ExpectedConditions.titleIs(title));
    }

    /**
     * Wait for URL to contain text
     */
    protected void waitForUrlContains(String urlPart) {
        logger.info("Waiting for URL to contain: {}", urlPart);
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Wait for URL to be exact
     */
    protected void waitForUrlExact(String url) {
        logger.info("Waiting for URL to be: {}", url);
        wait.until(ExpectedConditions.urlToBe(url));
    }

    /**
     * Check if element is present
     */
    protected boolean isElementPresent(By locator) {
        try {
            return findElement(locator) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is not present
     */
    protected boolean isElementNotPresent(By locator) {
        return !isElementPresent(locator);
    }

    /**
     * Wait for element to be present
     */
    protected void waitForElementPresent(By locator) {
        logger.info("Waiting for element to be present: {}", locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to not be present
     */
    protected void waitForElementNotPresent(By locator) {
        logger.info("Waiting for element to not be present: {}", locator);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    protected void waitForElementClickable(By locator) {
        logger.info("Waiting for element to be clickable: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be visible
     */
    protected void waitForElementVisible(By locator) {
        logger.info("Waiting for element to be visible: {}", locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to not be visible
     */
    protected void waitForElementInvisible(By locator) {
        logger.info("Waiting for element to be invisible: {}", locator);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for text to be present in element
     */
    protected void waitForTextPresent(By locator, String text) {
        logger.info("Waiting for text '{}' to be present in element: {}", text, locator);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for text to not be present in element
     */
    protected void waitForTextNotPresent(By locator, String text) {
        logger.info("Waiting for text '{}' to not be present in element: {}", text, locator);
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(locator, text)));
    }

    /**
     * Wait for attribute to contain value
     */
    protected void waitForAttributeContains(By locator, String attribute, String value) {
        logger.info("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
        wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    /**
     * Wait for attribute to be exact value
     */
    protected void waitForAttributeToBe(By locator, String attribute, String value) {
        logger.info("Waiting for attribute '{}' to be '{}' in element: {}", attribute, value, locator);
        wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /**
     * Wait for number of elements to be
     */
    protected void waitForNumberOfElementsToBe(By locator, int number) {
        logger.info("Waiting for {} elements to be present: {}", number, locator);
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, number));
    }

    /**
     * Wait for number of elements to be less than
     */
    protected void waitForNumberOfElementsToBeLessThan(By locator, int number) {
        logger.info("Waiting for less than {} elements to be present: {}", number, locator);
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(locator, number));
    }

    /**
     * Wait for number of elements to be more than
     */
    protected void waitForNumberOfElementsToBeMoreThan(By locator, int number) {
        logger.info("Waiting for more than {} elements to be present: {}", number, locator);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
    }

    /**
     * Wait for frame to be available and switch to it
     */
    protected void waitForFrameAndSwitch(By locator) {
        logger.info("Waiting for frame and switching to it: {}", locator);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    /**
     * Wait for alert to be present
     */
    protected void waitForAlertPresent() {
        logger.info("Waiting for alert to be present");
        wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page source
     */
    protected String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        waitForPageLoad();
    }

    /**
     * Navigate back
     */
    protected void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
        waitForPageLoad();
    }

    /**
     * Navigate forward
     */
    protected void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
        waitForPageLoad();
    }

    /**
     * Get window handle
     */
    protected String getWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Get all window handles
     */
    protected java.util.Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Switch to window by handle
     */
    protected void switchToWindow(String handle) {
        logger.info("Switching to window: {}", handle);
        driver.switchTo().window(handle);
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        logger.info("Switching to default content");
        driver.switchTo().defaultContent();
    }

    /**
     * Switch to parent frame
     */
    protected void switchToParentFrame() {
        logger.info("Switching to parent frame");
        driver.switchTo().parentFrame();
    }

    /**
     * Accept alert
     */
    protected void acceptAlert() {
        logger.info("Accepting alert");
        waitForAlertPresent();
        driver.switchTo().alert().accept();
    }

    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        logger.info("Dismissing alert");
        waitForAlertPresent();
        driver.switchTo().alert().dismiss();
    }

    /**
     * Get alert text
     */
    protected String getAlertText() {
        waitForAlertPresent();
        return driver.switchTo().alert().getText();
    }

    /**
     * Send keys to alert
     */
    protected void sendKeysToAlert(String keys) {
        logger.info("Sending keys to alert: {}", keys);
        waitForAlertPresent();
        driver.switchTo().alert().sendKeys(keys);
    }

    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(WebElement element) {
        logger.info("Scrolling to element");
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to element by locator
     */
    protected void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        scrollToElement(element);
    }

    /**
     * Scroll to top of page
     */
    protected void scrollToTop() {
        logger.info("Scrolling to top of page");
        executeJavaScript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        logger.info("Scrolling to bottom of page");
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Highlight element
     */
    protected void highlightElement(WebElement element) {
        executeJavaScript("arguments[0].style.border='3px solid red'", element);
    }

    /**
     * Remove highlight from element
     */
    protected void removeHighlight(WebElement element) {
        executeJavaScript("arguments[0].style.border=''", element);
    }

    /**
     * Take screenshot of element
     */
    protected String takeElementScreenshot(WebElement element, String testName) {
        try {
            highlightElement(element);
            String screenshotPath = takeScreenshot(testName + "_element");
            removeHighlight(element);
            return screenshotPath;
        } catch (Exception e) {
            logger.error("Failed to take element screenshot", e);
            return null;
        }
    }
}