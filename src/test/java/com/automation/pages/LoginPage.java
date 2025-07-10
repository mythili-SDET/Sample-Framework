package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login Page Object implementing Page Object Model pattern
 * Contains elements and actions for the login page
 */
public class LoginPage extends BasePage {
    
    // Page elements using @FindBy annotations
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "loginButton")
    private WebElement loginButton;
    
    @FindBy(xpath = "//div[@class='error-message']")
    private WebElement errorMessage;
    
    @FindBy(linkText = "Forgot Password?")
    private WebElement forgotPasswordLink;
    
    @FindBy(id = "rememberMe")
    private WebElement rememberMeCheckbox;
    
    // Alternative locators for elements (can be used instead of @FindBy)
    private static final By USERNAME_FIELD = By.id("username");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("loginButton");
    private static final By ERROR_MESSAGE = By.xpath("//div[@class='error-message']");
    private static final By FORGOT_PASSWORD_LINK = By.linkText("Forgot Password?");
    private static final By REMEMBER_ME_CHECKBOX = By.id("rememberMe");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enter username
     * @param username Username to enter
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        usernameField.clear();
        usernameField.sendKeys(username);
        return this;
    }
    
    /**
     * Enter password
     * @param password Password to enter
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        passwordField.clear();
        passwordField.sendKeys(password);
        return this;
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        loginButton.click();
    }
    
    /**
     * Login with credentials
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    /**
     * Login with remember me option
     * @param username Username
     * @param password Password
     * @param rememberMe Whether to check remember me
     */
    public void loginWithRememberMe(String username, String password, boolean rememberMe) {
        logger.info("Logging in with username: {} and remember me: {}", username, rememberMe);
        enterUsername(username);
        enterPassword(password);
        
        if (rememberMe && !rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        } else if (!rememberMe && rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
        
        clickLoginButton();
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            boolean displayed = errorMessage.isDisplayed();
            logger.info("Error message displayed: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.info("Error message not found");
            return false;
        }
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            String message = errorMessage.getText();
            logger.info("Error message: {}", message);
            return message;
        }
        return "";
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        logger.info("Clicking forgot password link");
        forgotPasswordLink.click();
    }
    
    /**
     * Check if login button is enabled
     * @return true if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        boolean enabled = loginButton.isEnabled();
        logger.info("Login button enabled: {}", enabled);
        return enabled;
    }
    
    /**
     * Clear username field
     */
    public LoginPage clearUsername() {
        logger.info("Clearing username field");
        usernameField.clear();
        return this;
    }
    
    /**
     * Clear password field
     */
    public LoginPage clearPassword() {
        logger.info("Clearing password field");
        passwordField.clear();
        return this;
    }
    
    /**
     * Get username field value
     * @return Username field value
     */
    public String getUsernameValue() {
        String value = usernameField.getAttribute("value");
        logger.info("Username field value: {}", value);
        return value;
    }
    
    /**
     * Check if remember me checkbox is selected
     * @return true if remember me is selected
     */
    public boolean isRememberMeSelected() {
        boolean selected = rememberMeCheckbox.isSelected();
        logger.info("Remember me selected: {}", selected);
        return selected;
    }
    
    /**
     * Check if page is loaded by verifying key elements
     * @return true if page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            boolean loaded = usernameField.isDisplayed() && 
                           passwordField.isDisplayed() && 
                           loginButton.isDisplayed();
            logger.info("Login page loaded: {}", loaded);
            return loaded;
        } catch (Exception e) {
            logger.error("Error checking if login page is loaded", e);
            return false;
        }
    }
    
    /**
     * Wait for login page to load
     */
    public void waitForPageToLoad() {
        logger.info("Waiting for login page to load");
        wait.until(driver -> isPageLoaded());
    }
}