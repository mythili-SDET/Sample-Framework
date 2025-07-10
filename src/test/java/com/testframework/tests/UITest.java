package com.testframework.tests;

import com.testframework.core.BaseTest;
import com.testframework.pageobjects.GoogleHomePage;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * UI Test Class demonstrating UI testing capabilities
 * Tests Google homepage functionality
 */
public class UITest extends BaseTest {
    
    @Override
    protected boolean isUITest() {
        return true;
    }
    
    @Test(description = "Test Google homepage search functionality")
    public void testGoogleSearch() {
        logInfo("Starting Google search test");
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        logInfo("Navigated to Google homepage");
        
        // Verify page elements are displayed
        assertTrue(googlePage.isSearchBoxDisplayed(), "Search box should be displayed");
        assertTrue(googlePage.isSearchButtonDisplayed(), "Search button should be displayed");
        assertTrue(googlePage.isLuckyButtonDisplayed(), "Lucky button should be displayed");
        
        // Perform search
        String searchTerm = "Selenium WebDriver";
        googlePage.search(searchTerm);
        logInfo("Performed search for: " + searchTerm);
        
        // Verify search results
        assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        assertTrue(googlePage.getNumberOfSearchResults() > 0, "Should have search results");
        
        logSuccess("Google search test completed successfully");
    }
    
    @Test(description = "Test Google search with data from Excel")
    public void testGoogleSearchWithExcelData() {
        logInfo("Starting Google search test with Excel data");
        
        // Get test data from Excel
        List<Map<String, String>> testData = excelDataProvider.readExcelData("SearchData");
        
        if (testData.isEmpty()) {
            logWarning("No test data found in Excel file");
            return;
        }
        
        // Use first row of data
        Map<String, String> data = testData.get(0);
        String searchTerm = data.get("SearchTerm");
        String expectedResult = data.get("ExpectedResult");
        
        logInfo("Using search term: " + searchTerm);
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Perform search
        googlePage.search(searchTerm);
        
        // Verify search results
        assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        assertTrue(googlePage.pageContainsText(expectedResult), "Page should contain expected result: " + expectedResult);
        
        logSuccess("Google search test with Excel data completed successfully");
    }
    
    @Test(description = "Test Google search with data from CSV")
    public void testGoogleSearchWithCsvData() {
        logInfo("Starting Google search test with CSV data");
        
        // Get test data from CSV
        List<Map<String, String>> testData = csvDataProvider.readCsvData();
        
        if (testData.isEmpty()) {
            logWarning("No test data found in CSV file");
            return;
        }
        
        // Use first row of data
        Map<String, String> data = testData.get(0);
        String searchTerm = data.get("SearchTerm");
        
        logInfo("Using search term from CSV: " + searchTerm);
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Perform search
        googlePage.search(searchTerm);
        
        // Verify search results
        assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        assertTrue(googlePage.getNumberOfSearchResults() > 0, "Should have search results");
        
        logSuccess("Google search test with CSV data completed successfully");
    }
    
    @Test(description = "Test Google search with data from JSON")
    public void testGoogleSearchWithJsonData() {
        logInfo("Starting Google search test with JSON data");
        
        // Get test data from JSON
        List<Map<String, Object>> testData = jsonDataProvider.readJsonData();
        
        if (testData.isEmpty()) {
            logWarning("No test data found in JSON file");
            return;
        }
        
        // Use first row of data
        Map<String, Object> data = testData.get(0);
        String searchTerm = (String) data.get("SearchTerm");
        
        logInfo("Using search term from JSON: " + searchTerm);
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Perform search
        googlePage.search(searchTerm);
        
        // Verify search results
        assertTrue(googlePage.areSearchResultsDisplayed(), "Search results should be displayed");
        assertTrue(googlePage.getNumberOfSearchResults() > 0, "Should have search results");
        
        logSuccess("Google search test with JSON data completed successfully");
    }
    
    @Test(description = "Test Google search suggestions")
    public void testGoogleSearchSuggestions() {
        logInfo("Starting Google search suggestions test");
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Enter partial search term to trigger suggestions
        googlePage.enterSearchTerm("selenium");
        
        // Wait for suggestions to appear
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Get suggestions
        List<org.openqa.selenium.WebElement> suggestions = googlePage.getSearchSuggestions();
        
        // Verify suggestions are displayed
        assertTrue(!suggestions.isEmpty(), "Search suggestions should be displayed");
        logInfo("Found " + suggestions.size() + " search suggestions");
        
        logSuccess("Google search suggestions test completed successfully");
    }
    
    @Test(description = "Test Google page title and URL")
    public void testGooglePageTitleAndUrl() {
        logInfo("Starting Google page title and URL test");
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Verify page title
        String pageTitle = googlePage.getPageTitle();
        assertTrue(pageTitle.contains("Google"), "Page title should contain 'Google'");
        logInfo("Page title: " + pageTitle);
        
        // Verify page URL
        String currentUrl = googlePage.getCurrentUrl();
        assertTrue(currentUrl.contains("google.com"), "URL should contain 'google.com'");
        logInfo("Current URL: " + currentUrl);
        
        logSuccess("Google page title and URL test completed successfully");
    }
    
    @Test(description = "Test Google search box functionality")
    public void testGoogleSearchBox() {
        logInfo("Starting Google search box test");
        
        // Initialize page object
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        
        // Navigate to Google
        googlePage.navigateTo();
        
        // Test search box value
        String searchTerm = "Test Search";
        googlePage.enterSearchTerm(searchTerm);
        
        String searchBoxValue = googlePage.getSearchBoxValue();
        assertEquals(searchBoxValue, searchTerm, "Search box value should match entered text");
        
        // Test clearing search box
        googlePage.clearSearchBox();
        assertTrue(googlePage.isSearchBoxEmpty(), "Search box should be empty after clearing");
        
        logSuccess("Google search box test completed successfully");
    }
}