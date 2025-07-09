package com.automation.ui;

import com.automation.core.UIBaseTest;
import com.automation.utils.CSVDataReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Sample UI test class demonstrating login functionality
 * Shows data-driven testing using CSV data
 */
public class LoginTest extends UIBaseTest {

    // Page element locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-btn");
    private final By errorMessage = By.className("error-message");
    private final By successMessage = By.className("success-message");
    private final By dashboardHeader = By.id("dashboard-header");
    private final By logoutButton = By.id("logout-btn");

    @Test(groups = {"smoke", "ui"}, description = "Test login with valid credentials")
    public void testValidLogin() {
        logger.info("Starting valid login test");
        
        // Navigate to login page
        navigateTo(config.getBaseUrl() + "/login");
        
        // Enter valid credentials
        type(usernameField, "admin");
        type(passwordField, "admin123");
        
        // Click login button
        click(loginButton);
        
        // Verify successful login
        waitForElementVisible(dashboardHeader, 10);
        Assert.assertTrue(isDisplayed(dashboardHeader), "Dashboard header should be visible after login");
        
        String currentUrl = getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard"), "Should be redirected to dashboard");
        
        logger.info("Valid login test completed successfully");
    }

    @Test(groups = {"regression", "ui"}, description = "Test login with invalid credentials")
    public void testInvalidLogin() {
        logger.info("Starting invalid login test");
        
        // Navigate to login page
        navigateTo(config.getBaseUrl() + "/login");
        
        // Enter invalid credentials
        type(usernameField, "invaliduser");
        type(passwordField, "wrongpassword");
        
        // Click login button
        click(loginButton);
        
        // Verify error message is displayed
        waitForElementVisible(errorMessage, 5);
        Assert.assertTrue(isDisplayed(errorMessage), "Error message should be displayed for invalid login");
        
        String errorText = getText(errorMessage);
        Assert.assertTrue(errorText.contains("Invalid"), "Error message should indicate invalid credentials");
        
        logger.info("Invalid login test completed successfully");
    }

    @Test(dataProvider = "loginData", groups = {"regression", "ui"}, 
          description = "Data-driven login test using CSV data")
    public void testDataDrivenLogin(Map<String, String> testData) {
        logger.info("Starting data-driven login test: {}", testData.get("test_description"));
        
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedResult = testData.get("expected_result");
        
        // Navigate to login page
        navigateTo(config.getBaseUrl() + "/login");
        
        // Enter credentials from test data
        type(usernameField, username != null ? username : "");
        type(passwordField, password != null ? password : "");
        
        // Click login button
        click(loginButton);
        
        // Verify result based on expected outcome
        if ("success".equals(expectedResult)) {
            waitForElementVisible(dashboardHeader, 10);
            Assert.assertTrue(isDisplayed(dashboardHeader), 
                "Dashboard should be visible for successful login: " + testData.get("test_description"));
            
            // Logout for next test
            click(logoutButton);
            waitForElementVisible(loginButton, 5);
            
        } else if ("failure".equals(expectedResult)) {
            // Should not redirect to dashboard
            String currentUrl = getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("dashboard"), 
                "Should not be redirected to dashboard for failed login: " + testData.get("test_description"));
            
            // Error message might be displayed (depending on implementation)
            // This is optional based on application behavior
        }
        
        logger.info("Data-driven login test completed: {}", testData.get("test_description"));
    }

    @Test(groups = {"ui"}, description = "Test login form validation")
    public void testLoginFormValidation() {
        logger.info("Starting login form validation test");
        
        // Navigate to login page
        navigateTo(config.getBaseUrl() + "/login");
        
        // Test empty form submission
        click(loginButton);
        
        // Verify form validation (this depends on application implementation)
        // Check if browser validation prevents submission or if custom validation is shown
        String currentUrl = getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Should remain on login page for empty form");
        
        // Test username field validation
        type(usernameField, "test");
        String usernameValue = getAttribute(usernameField, "value");
        Assert.assertEquals(usernameValue, "test", "Username field should accept input");
        
        // Test password field type
        String passwordType = getAttribute(passwordField, "type");
        Assert.assertEquals(passwordType, "password", "Password field should be of type password");
        
        logger.info("Login form validation test completed");
    }

    @Test(groups = {"ui"}, description = "Test remember me functionality")
    public void testRememberMeOption() {
        logger.info("Starting remember me test");
        
        // Navigate to login page
        navigateTo(config.getBaseUrl() + "/login");
        
        // Check if remember me checkbox exists
        By rememberMeCheckbox = By.id("remember-me");
        if (isDisplayed(rememberMeCheckbox)) {
            // Test remember me functionality
            click(rememberMeCheckbox);
            Assert.assertTrue(isSelected(rememberMeCheckbox), "Remember me checkbox should be selected");
            
            // Login with remember me checked
            type(usernameField, "admin");
            type(passwordField, "admin123");
            click(loginButton);
            
            // Verify login success
            waitForElementVisible(dashboardHeader, 10);
            Assert.assertTrue(isDisplayed(dashboardHeader), "Should login successfully with remember me");
            
        } else {
            logger.info("Remember me checkbox not found, skipping test");
        }
        
        logger.info("Remember me test completed");
    }

    /**
     * Data provider for login test data from CSV file
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        String csvFilePath = config.getCsvDataPath() + "login_data.csv";
        return CSVDataReader.readCSVDataForTestNG(csvFilePath);
    }
}