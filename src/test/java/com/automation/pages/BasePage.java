package com.automation.pages;

import com.automation.core.UIBaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page class implementing Page Object Model pattern
 * Provides common functionality for all page objects
 */
public abstract class BasePage extends UIBaseTest {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        PageFactory.initElements(driver, this);
        logger.info("Initialized page: {}", this.getClass().getSimpleName());
    }

    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
        return title;
    }

    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }

    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        logger.info("Waiting for page to load completely");
        wait.until(driver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Navigate to specific URL
     * @param url Target URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
        waitForPageLoad();
    }

    /**
     * Refresh current page
     */
    public void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        waitForPageLoad();
    }

    /**
     * Navigate back
     */
    public void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
        waitForPageLoad();
    }

    /**
     * Navigate forward
     */
    public void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
        waitForPageLoad();
    }

    /**
     * Check if element is present on page
     * @param locator Element locator
     * @return true if element is present, false otherwise
     */
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Wait for element to disappear
     * @param locator Element locator
     */
    public void waitForElementToDisappear(By locator) {
        logger.info("Waiting for element to disappear: {}", locator);
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Get element text with wait
     * @param locator Element locator
     * @return Element text
     */
    public String getElementText(By locator) {
        WebElement element = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
        String text = element.getText();
        logger.info("Element text: {}", text);
        return text;
    }

    /**
     * Check if page contains text
     * @param text Text to search for
     * @return true if text is found, false otherwise
     */
    public boolean pageContainsText(String text) {
        String pageSource = driver.getPageSource();
        boolean contains = pageSource.contains(text);
        logger.info("Page contains text '{}': {}", text, contains);
        return contains;
    }

    /**
     * Abstract method to verify page is loaded
     * Must be implemented by each page object
     */
    public abstract boolean isPageLoaded();
}