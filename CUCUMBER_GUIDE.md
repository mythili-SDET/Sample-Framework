# Cucumber BDD Framework Guide

This guide explains how to use the Cucumber BDD (Behavior Driven Development) framework that has been integrated into the test automation framework.

## 🎯 Overview

The Cucumber framework enables Behavior Driven Development by allowing you to write tests in natural language using Gherkin syntax. This makes tests readable by both technical and non-technical stakeholders.

## 📁 Framework Structure

```
src/test/
├── java/com/testframework/
│   ├── steps/
│   │   ├── UI_StepDefinitions.java      # UI test step definitions
│   │   ├── API_StepDefinitions.java     # API test step definitions
│   │   └── Database_StepDefinitions.java # Database test step definitions
│   └── cucumber/
│       ├── CucumberTestRunner.java      # Main test runner
│       └── CucumberHooks.java          # Global hooks
└── resources/
    └── features/
        ├── ui_features.feature          # UI test scenarios
        ├── api_features.feature         # API test scenarios
        └── database_features.feature    # Database test scenarios
```

## 🚀 Getting Started

### 1. Understanding Gherkin Syntax

Gherkin is the language used to write Cucumber scenarios. It uses keywords like:
- `Feature`: Describes the feature being tested
- `Scenario`: Describes a specific test scenario
- `Given`: Sets up the initial context
- `When`: Describes the action being performed
- `Then`: Describes the expected outcome
- `And`: Used to add additional steps

### 2. Writing Feature Files

Create feature files in `src/test/resources/features/`:

```gherkin
Feature: User Login Functionality
  As a user
  I want to log into the application
  So that I can access my account

  Background:
    Given I am on the login page

  @smoke @ui
  Scenario: Successful Login
    When I enter username "testuser"
    And I enter password "password123"
    And I click the login button
    Then I should be logged in successfully
    And I should see the dashboard

  @regression @ui
  Scenario: Failed Login with Invalid Credentials
    When I enter username "invaliduser"
    And I enter password "wrongpassword"
    And I click the login button
    Then I should see an error message
    And I should remain on the login page
```

### 3. Creating Step Definitions

Step definitions implement the Gherkin steps:

```java
package com.testframework.steps;

import com.testframework.core.CucumberBaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginStepDefinitions extends CucumberBaseTest {
    
    @Override
    protected boolean isUITest() {
        return true;
    }
    
    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        logStep("Navigating to login page");
        driver = webDriverManager.getDriver();
        driver.get("https://example.com/login");
        logVerification("Successfully navigated to login page");
    }
    
    @When("I enter username {string}")
    public void i_enter_username(String username) {
        logStep("Entering username: " + username);
        // Implementation here
        logData("Username", username);
    }
    
    @When("I enter password {string}")
    public void i_enter_password(String password) {
        logStep("Entering password");
        // Implementation here
        logData("Password", "***"); // Don't log actual password
    }
    
    @When("I click the login button")
    public void i_click_the_login_button() {
        logStep("Clicking login button");
        // Implementation here
    }
    
    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        logStep("Verifying successful login");
        // Implementation here
        assertTrueWithLog(true, "User should be logged in successfully");
    }
    
    @Then("I should see the dashboard")
    public void i_should_see_the_dashboard() {
        logStep("Verifying dashboard is displayed");
        // Implementation here
        assertTrueWithLog(true, "Dashboard should be displayed");
    }
    
    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        logStep("Verifying error message is displayed");
        // Implementation here
        assertTrueWithLog(true, "Error message should be displayed");
    }
    
    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        logStep("Verifying user remains on login page");
        // Implementation here
        assertTrueWithLog(true, "User should remain on login page");
    }
}
```

## 🏷️ Using Tags

Tags allow you to organize and run specific scenarios:

```gherkin
@smoke @ui
Scenario: Basic functionality test

@regression @ui
Scenario: Comprehensive functionality test

@api @integration
Scenario: API integration test

@database @unit
Scenario: Database unit test
```

### Running Tests by Tags

```bash
# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run UI tests only
mvn test -Dcucumber.filter.tags="@ui"

# Run tests with multiple tags
mvn test -Dcucumber.filter.tags="@smoke and @ui"

# Run tests with either tag
mvn test -Dcucumber.filter.tags="@smoke or @regression"

# Exclude certain tags
mvn test -Dcucumber.filter.tags="not @slow"
```

## 📊 Data Tables

Use data tables for data-driven testing:

```gherkin
Scenario: Login with different users
  When I login with the following credentials:
    | username | password    | expected_result |
    | user1    | password1   | success         |
    | user2    | password2   | success         |
    | invalid  | wrongpass   | failure         |
  Then I should see the appropriate result
```

```java
@When("I login with the following credentials:")
public void i_login_with_the_following_credentials(DataTable dataTable) {
    List<Map<String, String>> credentials = dataTable.asMaps(String.class, String.class);
    
    for (Map<String, String> row : credentials) {
        String username = row.get("username");
        String password = row.get("password");
        String expectedResult = row.get("expected_result");
        
        logStep("Testing login with username: " + username);
        // Implementation here
    }
}
```

## 🔄 Scenario Outlines

Use scenario outlines for parameterized testing:

```gherkin
Scenario Outline: Search functionality
  Given I am on the search page
  When I search for "<search_term>"
  Then I should see "<expected_results>" results

  Examples:
    | search_term | expected_results |
    | Java        | 1000            |
    | Python      | 800             |
    | JavaScript  | 1200            |
```

## 🎛️ Test Runners

Different test runners for different purposes:

### Main Test Runner
```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.testframework.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/cucumber.json"
    },
    monochrome = true,
    dryRun = false
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
```

### Specific Test Runners
```java
// UI Tests Only
@CucumberOptions(
    features = "src/test/resources/features/ui_features.feature",
    glue = {"com.testframework.steps"},
    tags = "@ui",
    plugin = {"pretty", "html:target/cucumber-reports/ui-tests.html"}
)
public class UITestRunner extends AbstractTestNGCucumberTests {
}

// API Tests Only
@CucumberOptions(
    features = "src/test/resources/features/api_features.feature",
    glue = {"com.testframework.steps"},
    tags = "@api",
    plugin = {"pretty", "html:target/cucumber-reports/api-tests.html"}
)
public class APITestRunner extends AbstractTestNGCucumberTests {
}
```

## 🪝 Hooks

Global hooks for setup and teardown:

```java
public class CucumberHooks {
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
        // Setup code here
    }
    
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            // Take screenshot
            takeScreenshot(scenario);
        }
        // Cleanup code here
    }
}
```

## 🚀 Running Tests

### Using Maven
```bash
# Run all Cucumber tests
mvn test -Dtest=CucumberTestRunner

# Run specific test types
mvn test -Dtest=UITestRunner
mvn test -Dtest=APITestRunner
mvn test -Dtest=DatabaseTestRunner

# Run with tags
mvn test -Dcucumber.filter.tags="@smoke"
```

### Using the Script
```bash
# Make script executable
chmod +x run-cucumber-tests.sh

# Run all tests
./run-cucumber-tests.sh all

# Run specific test types
./run-cucumber-tests.sh ui
./run-cucumber-tests.sh api
./run-cucumber-tests.sh smoke

# Run in parallel
./run-cucumber-tests.sh parallel

# Run by tags
./run-cucumber-tests.sh tags "@smoke"
```

## 📈 Reports

After test execution, reports are generated in:
- `target/cucumber-reports/` - Cucumber HTML and JSON reports
- `target/surefire-reports/` - TestNG reports
- `reports/` - Extent reports
- `logs/` - Framework logs

## 🎯 Best Practices

### 1. Feature Organization
- Group related scenarios in the same feature file
- Use descriptive feature names
- Keep scenarios focused and atomic

### 2. Step Definitions
- Use descriptive step names
- Implement reusable steps
- Keep step definitions simple and focused

### 3. Data Management
- Use data tables for multiple test cases
- Use scenario outlines for parameterized tests
- Keep test data separate from step definitions

### 4. Tagging Strategy
- Use consistent tag naming conventions
- Tag scenarios by test type (@ui, @api, @database)
- Tag scenarios by priority (@smoke, @regression)
- Tag scenarios by functionality (@login, @search, @payment)

### 5. Reporting
- Use appropriate plugins for reporting
- Include screenshots for UI test failures
- Log important information during test execution

## 🔧 Configuration

### Cucumber Options
```java
@CucumberOptions(
    features = "src/test/resources/features",  // Feature files location
    glue = {"com.testframework.steps"},       // Step definitions package
    tags = "@smoke",                          // Tags to run
    plugin = {                                // Reporting plugins
        "pretty",
        "html:target/cucumber-reports/report.html",
        "json:target/cucumber-reports/report.json"
    },
    monochrome = true,                        // Console output formatting
    dryRun = false,                          // Validate steps without running
    snippets = SnippetType.CAMELCASE         // Step definition naming
)
```

### Parallel Execution
```java
@Override
@DataProvider(parallel = true)
public Object[][] scenarios() {
    return super.scenarios();
}
```

## 🐛 Troubleshooting

### Common Issues

1. **Step Definition Not Found**
   - Check that step definition method names match the Gherkin steps
   - Verify the glue package is correctly specified
   - Ensure step definitions are in the correct package

2. **Feature File Not Found**
   - Verify feature file path in CucumberOptions
   - Check file extension (.feature)
   - Ensure proper Gherkin syntax

3. **Parallel Execution Issues**
   - Reduce thread count if tests are failing
   - Ensure thread-safe step definitions
   - Check for shared state between scenarios

4. **Report Generation Issues**
   - Verify plugin configurations
   - Check output directory permissions
   - Ensure proper plugin dependencies

## 📚 Additional Resources

- [Cucumber Documentation](https://cucumber.io/docs)
- [Gherkin Reference](https://cucumber.io/docs/gherkin/reference/)
- [Cucumber Java](https://cucumber.io/docs/cucumber/api/)
- [TestNG Integration](https://cucumber.io/docs/cucumber/api/#testng)

## 🤝 Support

For issues and questions:
1. Check the logs in `logs/` directory
2. Review Cucumber reports in `target/cucumber-reports/`
3. Verify feature file syntax
4. Check step definition implementations