package com.automation.stepdefinitions;

import com.automation.core.WebDriverManager;
import com.automation.ui.pages.BasePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * Step definitions for UI testing scenarios
 */
public class UI_StepDefinitions {
    private static final Logger logger = LogManager.getLogger(UI_StepDefinitions.class);
    private WebDriver driver;
    private BasePage basePage;
    private String currentUsername;
    private String currentPassword;

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        driver = WebDriverManager.getDriver();
        basePage = new BasePage(driver);
        
        // Navigate to login page (replace with actual URL)
        driver.get("https://example.com/login");
        logger.info("Navigated to login page");
    }

    @When("the user enters valid username {string}")
    public void the_user_enters_valid_username(String username) {
        currentUsername = username;
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys(username);
        logger.info("Entered username: {}", username);
    }

    @When("the user enters invalid username {string}")
    public void the_user_enters_invalid_username(String username) {
        currentUsername = username;
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys(username);
        logger.info("Entered invalid username: {}", username);
    }

    @When("the user enters valid password {string}")
    public void the_user_enters_valid_password(String password) {
        currentPassword = password;
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        logger.info("Entered password");
    }

    @When("the user enters invalid password {string}")
    public void the_user_enters_invalid_password(String password) {
        currentPassword = password;
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        logger.info("Entered invalid password");
    }

    @When("the user enters username {string}")
    public void the_user_enters_username(String username) {
        currentUsername = username;
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys(username);
        logger.info("Entered username: {}", username);
    }

    @When("the user enters password {string}")
    public void the_user_enters_password(String password) {
        currentPassword = password;
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        logger.info("Entered password");
    }

    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        logger.info("Clicked login button");
    }

    @Then("the user should be successfully logged in")
    public void the_user_should_be_successfully_logged_in() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/dashboard"), 
            "User should be redirected to dashboard after successful login");
        logger.info("User successfully logged in");
    }

    @Then("the user should see the dashboard")
    public void the_user_should_see_the_dashboard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dashboardElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))
        );
        
        Assert.assertTrue(dashboardElement.isDisplayed(), 
            "Dashboard should be visible after successful login");
        logger.info("Dashboard is visible");
    }

    @Then("the user should see an error message")
    public void the_user_should_see_an_error_message() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.className("error-message"))
        );
        
        Assert.assertTrue(errorElement.isDisplayed(), 
            "Error message should be displayed for invalid credentials");
        logger.info("Error message is displayed");
    }

    @Then("the user should remain on the login page")
    public void the_user_should_remain_on_the_login_page() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"), 
            "User should remain on login page after failed login");
        logger.info("User remained on login page");
    }

    @Then("the user should see {string}")
    public void the_user_should_see(String expectedResult) {
        if ("dashboard".equals(expectedResult)) {
            the_user_should_see_the_dashboard();
        } else if ("error message".equals(expectedResult)) {
            the_user_should_see_an_error_message();
        } else {
            Assert.fail("Unknown expected result: " + expectedResult);
        }
    }

    @When("the user logs in with the created credentials")
    public void the_user_logs_in_with_the_created_credentials() {
        // This step would use credentials created via API
        // Implementation depends on how credentials are shared between API and UI tests
        logger.info("Logging in with API-created credentials");
    }

    @When("I login to the application")
    public void i_login_to_the_application() {
        // Implementation for logging in
        logger.info("Logging in to the application");
    }

    @When("I navigate to the user management page")
    public void i_navigate_to_the_user_management_page() {
        driver.get("https://example.com/admin/users");
        logger.info("Navigated to user management page");
    }

    @Then("I should see the created user in the list")
    public void i_should_see_the_created_user_in_the_list() {
        // Implementation to verify user exists in UI
        logger.info("Verifying created user in UI list");
    }
}