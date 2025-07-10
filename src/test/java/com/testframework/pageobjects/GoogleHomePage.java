package com.testframework.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Google Home Page Object
 * Demonstrates Page Object Model pattern implementation
 */
public class GoogleHomePage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Page Elements using @FindBy annotation
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(name = "btnK")
    private WebElement searchButton;
    
    @FindBy(id = "result-stats")
    private WebElement searchResults;
    
    @FindBy(css = "input[name='btnI']")
    private WebElement luckyButton;
    
    // Constructor
    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Navigate to Google homepage
     */
    public void navigateTo() {
        driver.get("https://www.google.com");
        waitForPageToLoad();
    }
    
    /**
     * Wait for page to load
     */
    private void waitForPageToLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
    }
    
    /**
     * Enter search term
     */
    public void enterSearchTerm(String searchTerm) {
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }
    
    /**
     * Click search button
     */
    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
    }
    
    /**
     * Perform search
     */
    public void search(String searchTerm) {
        enterSearchTerm(searchTerm);
        clickSearchButton();
    }
    
    /**
     * Click "I'm Feeling Lucky" button
     */
    public void clickLuckyButton() {
        wait.until(ExpectedConditions.elementToBeClickable(luckyButton));
        luckyButton.click();
    }
    
    /**
     * Get search box value
     */
    public String getSearchBoxValue() {
        return searchBox.getAttribute("value");
    }
    
    /**
     * Check if search results are displayed
     */
    public boolean areSearchResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result-stats")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Check if search box is displayed
     */
    public boolean isSearchBoxDisplayed() {
        return searchBox.isDisplayed();
    }
    
    /**
     * Check if search button is displayed
     */
    public boolean isSearchButtonDisplayed() {
        return searchButton.isDisplayed();
    }
    
    /**
     * Check if lucky button is displayed
     */
    public boolean isLuckyButtonDisplayed() {
        return luckyButton.isDisplayed();
    }
    
    /**
     * Get search results text
     */
    public String getSearchResultsText() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result-stats")));
            return searchResults.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Wait for search results to load
     */
    public void waitForSearchResults() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result-stats")));
    }
    
    /**
     * Get number of search results
     */
    public int getNumberOfSearchResults() {
        try {
            String resultsText = getSearchResultsText();
            // Extract number from text like "About 1,000,000,000 results"
            String[] parts = resultsText.split(" ");
            for (String part : parts) {
                if (part.matches("\\d+")) {
                    return Integer.parseInt(part);
                }
            }
        } catch (Exception e) {
            // Return 0 if parsing fails
        }
        return 0;
    }
    
    /**
     * Check if page contains specific text
     */
    public boolean pageContainsText(String text) {
        return driver.getPageSource().contains(text);
    }
    
    /**
     * Get all search result links
     */
    public java.util.List<WebElement> getSearchResultLinks() {
        return driver.findElements(By.cssSelector("div.g a"));
    }
    
    /**
     * Click on first search result
     */
    public void clickFirstSearchResult() {
        java.util.List<WebElement> links = getSearchResultLinks();
        if (!links.isEmpty()) {
            links.get(0).click();
        }
    }
    
    /**
     * Get search suggestions
     */
    public java.util.List<WebElement> getSearchSuggestions() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul[role='listbox'] li")));
            return driver.findElements(By.cssSelector("ul[role='listbox'] li"));
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Click on search suggestion by index
     */
    public void clickSearchSuggestion(int index) {
        java.util.List<WebElement> suggestions = getSearchSuggestions();
        if (index >= 0 && index < suggestions.size()) {
            suggestions.get(index).click();
        }
    }
    
    /**
     * Clear search box
     */
    public void clearSearchBox() {
        searchBox.clear();
    }
    
    /**
     * Check if search box is empty
     */
    public boolean isSearchBoxEmpty() {
        return searchBox.getAttribute("value").isEmpty();
    }
}