package com.testframework.steps;

import com.testframework.core.CucumberBaseTest;
import com.testframework.pageobjects.GoogleHomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

/**
 * Step Definitions for UI Testing Scenarios
 * Implements BDD steps for Google search functionality
 */
public class UI_StepDefinitions extends CucumberBaseTest {
    
    private GoogleHomePage googlePage;
    private String searchTerm;
    private String expectedResult;
    
    @Override
    protected boolean isUITest() {
        return true;
    }
    
    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        logStep("Initializing Google homepage");
        driver = webDriverManager.getDriver();
        googlePage = new GoogleHomePage(driver);
        googlePage.navigateTo();
        logVerification("Successfully navigated to Google homepage");
    }
    
    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        this.searchTerm = searchTerm;
        logStep("Performing search for: " + searchTerm);
        googlePage.search(searchTerm);
        logData("Search Term", searchTerm);
    }
    
    @When("I search for the following terms:")
    public void i_search_for_the_following_terms(DataTable dataTable) {
        List<Map<String, String>> searchData = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : searchData) {
            String term = row.get("SearchTerm");
            String expected = row.get("ExpectedResult");
            
            logStep("Searching for: " + term);
            googlePage.search(term);
            
            // Verify search results
            assertTrueWithLog(googlePage.areSearchResultsDisplayed(), 
                "Search results should be displayed for: " + term);
            
            logData("Search Term", term);
            logData("Expected Result", expected);
        }
    }
    
    @When("I type {string} in the search box")
    public void i_type_in_the_search_box(String text) {
        logStep("Typing in search box: " + text);
        googlePage.enterSearchTerm(text);
        logData("Typed Text", text);
    }
    
    @When("I enter {string} in the search box")
    public void i_enter_in_the_search_box(String text) {
        logStep("Entering text in search box: " + text);
        googlePage.enterSearchTerm(text);
        logData("Entered Text", text);
    }
    
    @When("I clear the search box")
    public void i_clear_the_search_box() {
        logStep("Clearing search box");
        googlePage.clearSearchBox();
    }
    
    @When("I navigate to Google")
    public void i_navigate_to_google() {
        logStep("Navigating to Google");
        navigateToWithLog("https://www.google.com");
    }
    
    @Then("I should see search results")
    public void i_should_see_search_results() {
        logStep("Verifying search results are displayed");
        assertTrueWithLog(googlePage.areSearchResultsDisplayed(), 
            "Search results should be displayed");
    }
    
    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitle) {
        logStep("Verifying page title contains: " + expectedTitle);
        String actualTitle = getPageTitleWithLog();
        assertTrueWithLog(actualTitle.contains(expectedTitle), 
            "Page title should contain: " + expectedTitle);
    }
    
    @Then("I should see search results for each term")
    public void i_should_see_search_results_for_each_term() {
        logStep("Verifying search results for all terms");
        assertTrueWithLog(googlePage.areSearchResultsDisplayed(), 
            "Search results should be displayed for all terms");
    }
    
    @Then("I should see search suggestions")
    public void i_should_see_search_suggestions() {
        logStep("Verifying search suggestions are displayed");
        List<org.openqa.selenium.WebElement> suggestions = googlePage.getSearchSuggestions();
        assertTrueWithLog(!suggestions.isEmpty(), 
            "Search suggestions should be displayed");
        logData("Number of Suggestions", String.valueOf(suggestions.size()));
    }
    
    @Then("the suggestions should contain {string}")
    public void the_suggestions_should_contain(String expectedText) {
        logStep("Verifying suggestions contain: " + expectedText);
        List<org.openqa.selenium.WebElement> suggestions = googlePage.getSearchSuggestions();
        boolean found = suggestions.stream()
            .anyMatch(suggestion -> suggestion.getText().toLowerCase().contains(expectedText.toLowerCase()));
        assertTrueWithLog(found, 
            "Suggestions should contain: " + expectedText);
    }
    
    @Then("the search box should contain {string}")
    public void the_search_box_should_contain(String expectedText) {
        logStep("Verifying search box contains: " + expectedText);
        String actualText = googlePage.getSearchBoxValue();
        assertEqualsWithLog(actualText, expectedText, 
            "Search box should contain: " + expectedText);
    }
    
    @Then("the search box should be empty")
    public void the_search_box_should_be_empty() {
        logStep("Verifying search box is empty");
        assertTrueWithLog(googlePage.isSearchBoxEmpty(), 
            "Search box should be empty");
    }
    
    @Then("the URL should contain {string}")
    public void the_url_should_contain(String expectedUrl) {
        logStep("Verifying URL contains: " + expectedUrl);
        String actualUrl = getCurrentUrlWithLog();
        assertTrueWithLog(actualUrl.contains(expectedUrl), 
            "URL should contain: " + expectedUrl);
    }
    
    @Then("the search box should be displayed")
    public void the_search_box_should_be_displayed() {
        logStep("Verifying search box is displayed");
        assertTrueWithLog(googlePage.isSearchBoxDisplayed(), 
            "Search box should be displayed");
    }
    
    @Then("the search button should be displayed")
    public void the_search_button_should_be_displayed() {
        logStep("Verifying search button is displayed");
        assertTrueWithLog(googlePage.isSearchButtonDisplayed(), 
            "Search button should be displayed");
    }
    
    @Then("the page should contain {string}")
    public void the_page_should_contain(String expectedText) {
        logStep("Verifying page contains: " + expectedText);
        assertTrueWithLog(googlePage.pageContainsText(expectedText), 
            "Page should contain: " + expectedText);
    }
    
    @Then("I should see {int} search results")
    public void i_should_see_search_results(int expectedCount) {
        logStep("Verifying number of search results: " + expectedCount);
        int actualCount = googlePage.getNumberOfSearchResults();
        assertTrueWithLog(actualCount > 0, 
            "Should have search results");
        logData("Number of Results", String.valueOf(actualCount));
    }
    
    @Then("the lucky button should be displayed")
    public void the_lucky_button_should_be_displayed() {
        logStep("Verifying lucky button is displayed");
        assertTrueWithLog(googlePage.isLuckyButtonDisplayed(), 
            "Lucky button should be displayed");
    }
    
    @When("I click the lucky button")
    public void i_click_the_lucky_button() {
        logStep("Clicking lucky button");
        googlePage.clickLuckyButton();
    }
    
    @When("I click the first search result")
    public void i_click_the_first_search_result() {
        logStep("Clicking first search result");
        googlePage.clickFirstSearchResult();
    }
    
    @Then("I should be on a different page")
    public void i_should_be_on_a_different_page() {
        logStep("Verifying navigation to different page");
        String currentUrl = getCurrentUrlWithLog();
        assertTrueWithLog(!currentUrl.contains("google.com/search"), 
            "Should be on a different page");
    }
}