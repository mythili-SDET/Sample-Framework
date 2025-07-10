package com.automation.tests.ui;

import com.automation.core.BaseUITest;
import com.automation.ui.pages.LoginPage;
import com.automation.ui.pages.DashboardPage;
import com.automation.utils.ExcelDataProvider;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * UI Test Suite
 * Demonstrates UI automation capabilities
 */
public class UITestSuite extends BaseUITest {
    private static final Logger logger = LogManager.getLogger(UITestSuite.class);
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeClass
    public void setUp() {
        logger.info("Setting up UI test suite");
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
    }

    /**
     * Test successful login
     */
    @Test(description = "Test successful login with valid credentials")
    public void testSuccessfulLogin() {
        logger.info("Starting successful login test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Perform login
        loginPage.login("testuser@example.com", "password123");
        
        // Verify successful login
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after login");
        Assert.assertEquals(dashboardPage.getWelcomeMessage(), "Welcome, Test User!", "Welcome message should match");
        
        logger.info("Successfully completed login test");
    }

    /**
     * Test failed login
     */
    @Test(description = "Test failed login with invalid credentials")
    public void testFailedLogin() {
        logger.info("Starting failed login test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Attempt login with invalid credentials
        loginPage.login("invalid@example.com", "wrongpassword");
        
        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid email or password", "Error message should match");
        
        logger.info("Successfully completed failed login test");
    }

    /**
     * Test login with empty credentials
     */
    @Test(description = "Test login validation with empty credentials")
    public void testLoginWithEmptyCredentials() {
        logger.info("Starting empty credentials test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Attempt login with empty credentials
        loginPage.login("", "");
        
        // Verify validation messages
        Assert.assertTrue(loginPage.isEmailValidationMessageDisplayed(), "Email validation message should be displayed");
        Assert.assertTrue(loginPage.isPasswordValidationMessageDisplayed(), "Password validation message should be displayed");
        
        logger.info("Successfully completed empty credentials test");
    }

    /**
     * Test dashboard navigation
     */
    @Test(description = "Test dashboard navigation and menu items")
    public void testDashboardNavigation() {
        logger.info("Starting dashboard navigation test");
        
        // Login first
        loginPage.navigateToLoginPage();
        loginPage.login("testuser@example.com", "password123");
        
        // Verify dashboard elements
        Assert.assertTrue(dashboardPage.isMenuDisplayed(), "Menu should be displayed");
        Assert.assertTrue(dashboardPage.isProfileSectionDisplayed(), "Profile section should be displayed");
        Assert.assertTrue(dashboardPage.isNotificationIconDisplayed(), "Notification icon should be displayed");
        
        // Test menu navigation
        dashboardPage.clickOnProfileMenu();
        Assert.assertTrue(dashboardPage.isProfileDropdownDisplayed(), "Profile dropdown should be displayed");
        
        dashboardPage.clickOnSettings();
        Assert.assertTrue(dashboardPage.isSettingsPageDisplayed(), "Settings page should be displayed");
        
        logger.info("Successfully completed dashboard navigation test");
    }

    /**
     * Test responsive design
     */
    @Test(description = "Test responsive design on different screen sizes")
    public void testResponsiveDesign() {
        logger.info("Starting responsive design test");
        
        // Test on desktop size
        driver.manage().window().maximize();
        loginPage.navigateToLoginPage();
        Assert.assertTrue(loginPage.isDesktopLayoutCorrect(), "Desktop layout should be correct");
        
        // Test on tablet size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024));
        loginPage.refreshPage();
        Assert.assertTrue(loginPage.isTabletLayoutCorrect(), "Tablet layout should be correct");
        
        // Test on mobile size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));
        loginPage.refreshPage();
        Assert.assertTrue(loginPage.isMobileLayoutCorrect(), "Mobile layout should be correct");
        
        logger.info("Successfully completed responsive design test");
    }

    /**
     * Test accessibility features
     */
    @Test(description = "Test accessibility features and ARIA labels")
    public void testAccessibility() {
        logger.info("Starting accessibility test");
        
        loginPage.navigateToLoginPage();
        
        // Test ARIA labels
        Assert.assertTrue(loginPage.hasProperAriaLabels(), "All form elements should have proper ARIA labels");
        
        // Test keyboard navigation
        loginPage.tabThroughElements();
        Assert.assertTrue(loginPage.isKeyboardNavigationWorking(), "Keyboard navigation should work properly");
        
        // Test screen reader compatibility
        Assert.assertTrue(loginPage.isScreenReaderCompatible(), "Page should be screen reader compatible");
        
        logger.info("Successfully completed accessibility test");
    }

    /**
     * Test browser compatibility
     */
    @Test(description = "Test browser compatibility features")
    public void testBrowserCompatibility() {
        logger.info("Starting browser compatibility test");
        
        loginPage.navigateToLoginPage();
        
        // Test JavaScript functionality
        Assert.assertTrue(loginPage.isJavaScriptWorking(), "JavaScript should be working properly");
        
        // Test CSS features
        Assert.assertTrue(loginPage.isCSSFeaturesWorking(), "CSS features should be working properly");
        
        // Test HTML5 features
        Assert.assertTrue(loginPage.isHTML5FeaturesWorking(), "HTML5 features should be working properly");
        
        logger.info("Successfully completed browser compatibility test");
    }

    /**
     * Test performance
     */
    @Test(description = "Test page load performance")
    public void testPageLoadPerformance() {
        logger.info("Starting performance test");
        
        long startTime = System.currentTimeMillis();
        loginPage.navigateToLoginPage();
        long loadTime = System.currentTimeMillis() - startTime;
        
        // Verify page loads within acceptable time (5 seconds)
        Assert.assertTrue(loadTime < 5000, "Page should load within 5 seconds. Actual time: " + loadTime + "ms");
        
        logger.info("Page loaded in {} ms", loadTime);
        logger.info("Successfully completed performance test");
    }

    /**
     * Test data-driven login with Excel data
     */
    @Test(dataProvider = "loginData", description = "Test login with data from Excel")
    public void testDataDrivenLogin(String email, String password, String expectedResult) {
        logger.info("Testing login with email: {}, expected result: {}", email, expectedResult);
        
        loginPage.navigateToLoginPage();
        loginPage.login(email, password);
        
        if ("success".equals(expectedResult)) {
            Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Login should be successful");
        } else {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should fail with error message");
        }
    }

    /**
     * Data provider for login tests
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        List<Map<String, String>> testData = ExcelDataProvider.readTestData("LoginData");
        
        Object[][] data = new Object[testData.size()][3];
        for (int i = 0; i < testData.size(); i++) {
            Map<String, String> row = testData.get(i);
            data[i][0] = row.get("Email");
            data[i][1] = row.get("Password");
            data[i][2] = row.get("ExpectedResult");
        }
        
        return data;
    }

    /**
     * Test parallel execution
     */
    @Test(description = "Test parallel execution capability")
    public void testParallelExecution() {
        logger.info("Starting parallel execution test on thread: {}", Thread.currentThread().getId());
        
        // Simple test to verify parallel execution
        loginPage.navigateToLoginPage();
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        
        // Simulate some work
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Completed parallel execution test on thread: {}", Thread.currentThread().getId());
    }

    /**
     * Test retry mechanism
     */
    @Test(description = "Test retry mechanism for flaky tests", retryAnalyzer = com.automation.listeners.RetryAnalyzer.class)
    public void testRetryMechanism() {
        logger.info("Starting retry mechanism test");
        
        // This test might fail occasionally to demonstrate retry mechanism
        loginPage.navigateToLoginPage();
        
        // Simulate a flaky condition
        if (Math.random() < 0.3) {
            Assert.fail("Simulated flaky test failure");
        }
        
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        logger.info("Successfully completed retry mechanism test");
    }
}