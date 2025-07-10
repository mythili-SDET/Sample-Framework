package com.automation.stepdefinitions;

import com.automation.hooks.TestContext;
import com.automation.pages.LoginPage;
import com.automation.utils.DataProvider;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

/**
 * UI Step Definitions for Cucumber scenarios
 * Implements steps for web interface testing using Page Object Model
 */
public class UIStepDefinitions {
    private static final Logger logger = LogManager.getLogger(UIStepDefinitions.class);
    
    private final TestContext testContext;
    private WebDriver driver;
    private LoginPage loginPage;

    public UIStepDefinitions(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        driver = testContext.getWebDriver();
        loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page is not loaded");
        logger.info("User is on the login page");
    }

    @Given("I navigate to the application URL")
    public void i_navigate_to_the_application_url() {
        driver = testContext.getWebDriver();
        String currentUrl = driver.getCurrentUrl();
        logger.info("Navigated to application URL: {}", currentUrl);
    }

    @When("I enter username {string}")
    public void i_enter_username(String username) {
        loginPage.enterUsername(username);
        testContext.setTestData("username", username);
        logger.info("Entered username: {}", username);
    }

    @When("I enter password {string}")
    public void i_enter_password(String password) {
        loginPage.enterPassword(password);
        testContext.setTestData("password", password);
        logger.info("Entered password");
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        loginPage.clickLoginButton();
        logger.info("Clicked login button");
    }

    @When("I login with credentials {string} and {string}")
    public void i_login_with_credentials(String username, String password) {
        loginPage.login(username, password);
        testContext.setTestData("username", username);
        testContext.setTestData("password", password);
        logger.info("Logged in with username: {}", username);
    }

    @When("I login with remember me option")
    public void i_login_with_remember_me_option() {
        String username = testContext.getTestData("username", String.class);
        String password = testContext.getTestData("password", String.class);
        loginPage.loginWithRememberMe(username, password, true);
        logger.info("Logged in with remember me option");
    }

    @When("I click forgot password link")
    public void i_click_forgot_password_link() {
        loginPage.clickForgotPasswordLink();
        logger.info("Clicked forgot password link");
    }

    @When("I clear the username field")
    public void i_clear_the_username_field() {
        loginPage.clearUsername();
        logger.info("Cleared username field");
    }

    @When("I clear the password field")
    public void i_clear_the_password_field() {
        loginPage.clearPassword();
        logger.info("Cleared password field");
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expectedMessage) {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message is not displayed");
        String actualMessage = loginPage.getErrorMessageText();
        Assert.assertEquals(actualMessage, expectedMessage, "Error message mismatch");
        logger.info("Verified error message: {}", expectedMessage);
    }

    @Then("I should not see any error message")
    public void i_should_not_see_any_error_message() {
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error message is displayed when it shouldn't be");
        logger.info("Verified no error message is displayed");
    }

    @Then("the login button should be enabled")
    public void the_login_button_should_be_enabled() {
        Assert.assertTrue(loginPage.isLoginButtonEnabled(), "Login button is not enabled");
        logger.info("Verified login button is enabled");
    }

    @Then("the login button should be disabled")
    public void the_login_button_should_be_disabled() {
        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Login button is enabled when it should be disabled");
        logger.info("Verified login button is disabled");
    }

    @Then("the remember me checkbox should be selected")
    public void the_remember_me_checkbox_should_be_selected() {
        Assert.assertTrue(loginPage.isRememberMeSelected(), "Remember me checkbox is not selected");
        logger.info("Verified remember me checkbox is selected");
    }

    @Then("the username field should contain {string}")
    public void the_username_field_should_contain(String expectedValue) {
        String actualValue = loginPage.getUsernameValue();
        Assert.assertEquals(actualValue, expectedValue, "Username field value mismatch");
        logger.info("Verified username field contains: {}", expectedValue);
    }

    @Then("the page title should be {string}")
    public void the_page_title_should_be(String expectedTitle) {
        String actualTitle = loginPage.getPageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch");
        logger.info("Verified page title: {}", expectedTitle);
    }

    @Then("the current URL should contain {string}")
    public void the_current_url_should_contain(String expectedUrlPart) {
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrlPart), 
            "Current URL does not contain expected part: " + expectedUrlPart);
        logger.info("Verified URL contains: {}", expectedUrlPart);
    }

    @Then("the page should contain text {string}")
    public void the_page_should_contain_text(String expectedText) {
        Assert.assertTrue(loginPage.pageContainsText(expectedText), 
            "Page does not contain expected text: " + expectedText);
        logger.info("Verified page contains text: {}", expectedText);
    }

    // Data-driven test steps
    @When("I login with data from Excel")
    public void i_login_with_data_from_excel() {
        String dataSource = testContext.getDataSource();
        if ("excel".equals(dataSource)) {
            // Get test data from context (set by data hooks)
            String username = testContext.getTestData("username", String.class);
            String password = testContext.getTestData("password", String.class);
            
            if (username != null && password != null) {
                loginPage.login(username, password);
                logger.info("Logged in with Excel data - Username: {}", username);
            } else {
                throw new RuntimeException("Excel test data not found in context");
            }
        } else {
            throw new RuntimeException("Expected Excel data source but found: " + dataSource);
        }
    }

    @When("I login with data from JSON")
    public void i_login_with_data_from_json() {
        String dataSource = testContext.getDataSource();
        if ("json".equals(dataSource)) {
            String username = testContext.getTestData("username", String.class);
            String password = testContext.getTestData("password", String.class);
            
            if (username != null && password != null) {
                loginPage.login(username, password);
                logger.info("Logged in with JSON data - Username: {}", username);
            } else {
                throw new RuntimeException("JSON test data not found in context");
            }
        } else {
            throw new RuntimeException("Expected JSON data source but found: " + dataSource);
        }
    }

    @When("I login with data from CSV")
    public void i_login_with_data_from_csv() {
        String dataSource = testContext.getDataSource();
        if ("csv".equals(dataSource)) {
            String username = testContext.getTestData("username", String.class);
            String password = testContext.getTestData("password", String.class);
            
            if (username != null && password != null) {
                loginPage.login(username, password);
                logger.info("Logged in with CSV data - Username: {}", username);
            } else {
                throw new RuntimeException("CSV test data not found in context");
            }
        } else {
            throw new RuntimeException("Expected CSV data source but found: " + dataSource);
        }
    }

    @When("I login with test data:")
    public void i_login_with_test_data(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        loginPage.login(username, password);
        testContext.setTestData("username", username);
        testContext.setTestData("password", password);
        logger.info("Logged in with data table - Username: {}", username);
    }

    // Navigation steps
    @When("I refresh the page")
    public void i_refresh_the_page() {
        loginPage.refreshPage();
        logger.info("Page refreshed");
    }

    @When("I navigate back")
    public void i_navigate_back() {
        loginPage.navigateBack();
        logger.info("Navigated back");
    }

    @When("I navigate forward")
    public void i_navigate_forward() {
        loginPage.navigateForward();
        logger.info("Navigated forward");
    }

    @Given("I wait for the page to load completely")
    public void i_wait_for_the_page_to_load_completely() {
        loginPage.waitForPageLoad();
        logger.info("Waited for page to load completely");
    }
}