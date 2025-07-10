# Hybrid Automation Framework

A comprehensive Java-based test automation framework that supports UI, API, and Database testing with multiple data formats and parallel execution capabilities.

## 🚀 Features

### Core Capabilities
- **Hybrid Testing**: Support for UI, API, and Database testing in a single framework
- **Modular Architecture**: Layered design with separate base classes for different testing types
- **Page Object Model (POM)**: Organized UI testing with reusable page objects
- **Data-Driven Testing**: Support for Excel, JSON, and CSV data sources
- **Parallel Execution**: Thread-safe WebDriver management and parallel test execution
- **Retry Logic**: Automatic retry mechanism for failed tests
- **Environment Management**: Configurable environments (QA, UAT, STAGE)
- **Comprehensive Reporting**: Extent Reports and Allure integration

### Technology Stack
- **Java 11+**
- **Selenium WebDriver 4.15.0**
- **TestNG 7.8.0**
- **Cucumber 7.14.0** (BDD)
- **REST Assured 5.3.2** (API Testing)
- **Apache POI 5.2.4** (Excel handling)
- **Jackson 2.15.2** (JSON processing)
- **OpenCSV 5.8** (CSV handling)
- **MySQL/PostgreSQL** (Database testing)
- **Log4j2 2.21.1** (Logging)
- **Extent Reports 5.1.1** (Reporting)
- **Allure 2.24.0** (Reporting)

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/automation/
│   │   ├── core/                    # Core framework classes
│   │   │   ├── ConfigManager.java   # Configuration management
│   │   │   ├── WebDriverManager.java # Thread-safe WebDriver
│   │   │   ├── BaseUITest.java      # UI testing base class
│   │   │   ├── BaseAPITest.java     # API testing base class
│   │   │   ├── BaseDBTest.java      # Database testing base class
│   │   │   └── CucumberHooks.java   # Cucumber lifecycle hooks
│   │   ├── api/                     # API testing classes
│   │   │   ├── GETRequest.java      # GET request handling
│   │   │   ├── POSTRequest.java     # POST request handling
│   │   │   ├── PUTRequest.java      # PUT request handling
│   │   │   └── DELETERequest.java   # DELETE request handling
│   │   ├── ui/                      # UI testing classes
│   │   │   └── pages/
│   │   │       └── BasePage.java    # Base page object
│   │   ├── database/                # Database utilities
│   │   │   └── DatabaseManager.java # Database connection management
│   │   ├── utils/                   # Utility classes
│   │   │   ├── ExcelDataProvider.java # Excel data handling
│   │   │   ├── JSONDataProvider.java  # JSON data handling
│   │   │   └── CSVDataProvider.java   # CSV data handling
│   │   └── listeners/               # TestNG listeners
│   │       ├── TestListener.java    # Test reporting listener
│   │       └── RetryListener.java   # Retry logic listener
│   └── resources/
│       ├── config.properties        # Environment configuration
│       └── log4j2.xml              # Logging configuration
└── test/
    ├── java/com/automation/
    │   ├── tests/                   # Test suites
    │   │   ├── ui/                  # UI test suites
    │   │   ├── api/                 # API test suites
    │   │   ├── database/            # Database test suites
    │   │   ├── mixed/               # Mixed test suites
    │   │   └── cucumber/            # Cucumber test runner
    │   └── stepdefinitions/         # Cucumber step definitions
    │       ├── UI_StepDefinitions.java
    │       ├── API_StepDefinitions.java
    │       └── Database_StepDefinitions.java
    └── resources/
        ├── features/                 # Cucumber feature files
        │   ├── UI_Login.feature
        │   ├── API_User_Management.feature
        │   └── Database_Validation.feature
        ├── testdata/                 # Test data files
        │   ├── TestData.xlsx
        │   ├── TestData.json
        │   └── TestData.csv
        └── testng.xml               # TestNG configuration
```

## ⚙️ Configuration

### Environment Setup
The framework supports multiple environments through `config.properties`:

```properties
# Environment Configuration
environment=qa
browser=chrome
headless=false

# UI Configuration
ui.base.url.qa=https://qa.example.com
ui.base.url.uat=https://uat.example.com
ui.base.url.stage=https://stage.example.com

# API Configuration
api.base.url.qa=https://api.qa.example.com
api.base.url.uat=https://api.uat.example.com
api.base.url.stage=https://api.stage.example.com

# Database Configuration
db.host.qa=qa-db.example.com
db.host.uat=uat-db.example.com
db.host.stage=stage-db.example.com
```

### Switching Environments
```bash
# Run tests for QA environment
mvn test -Denvironment=qa

# Run tests for UAT environment
mvn test -Denvironment=uat

# Run tests for STAGE environment
mvn test -Denvironment=stage
```

## 🧪 Test Types

### 1. UI Testing
```java
@Test
public void testUserLogin() {
    // Navigate to login page
    driver.get(config.getUIBaseUrl() + "/login");
    
    // Enter credentials
    WebElement usernameField = driver.findElement(By.id("username"));
    usernameField.sendKeys("testuser@example.com");
    
    WebElement passwordField = driver.findElement(By.id("password"));
    passwordField.sendKeys("TestPassword123");
    
    // Click login button
    WebElement loginButton = driver.findElement(By.id("login-button"));
    loginButton.click();
    
    // Verify successful login
    WebElement dashboard = driver.findElement(By.id("dashboard"));
    Assert.assertTrue(dashboard.isDisplayed());
}
```

### 2. API Testing
```java
@Test
public void testCreateUser() {
    POSTRequest postRequest = new POSTRequest();
    
    Map<String, Object> userData = new HashMap<>();
    userData.put("name", "Test User");
    userData.put("email", "testuser@example.com");
    userData.put("password", "TestPassword123");
    userData.put("role", "user");
    
    var response = postRequest.sendRequest("/users", userData);
    Assert.assertEquals(response.getStatusCode(), 201);
}
```

### 3. Database Testing
```java
@Test
public void testDatabaseValidation() {
    BaseDBTest dbTest = new BaseDBTest();
    
    String query = "SELECT * FROM users WHERE email = 'testuser@example.com'";
    ResultSet rs = dbTest.executeQuery(query);
    
    Assert.assertTrue(rs.next(), "User should exist in database");
    String userName = rs.getString("name");
    Assert.assertEquals(userName, "Test User");
}
```

### 4. Mixed Testing (UI + API + Database)
```java
@Test
public void testEndToEndUserCreation() {
    // Step 1: Create user via API
    var apiResponse = postRequest.sendRequest("/users", userData);
    Assert.assertEquals(apiResponse.getStatusCode(), 201);
    
    // Step 2: Verify in database
    verifyUserInDatabase(email, userData);
    
    // Step 3: Login via UI
    loginViaUI(email, password);
    
    // Step 4: Verify dashboard access
    verifyDashboardAccess();
}
```

## 📊 Data-Driven Testing

### Excel Data Provider
```java
@DataProvider(name = "excelUserData")
public Object[][] getExcelUserData() {
    ExcelDataProvider excelProvider = new ExcelDataProvider();
    return excelProvider.getData("src/test/resources/testdata/TestData.xlsx", "UserData");
}

@Test(dataProvider = "excelUserData")
public void testDataDrivenUserCreation(String name, String email, String role, String status) {
    // Test implementation using Excel data
}
```

### JSON Data Provider
```java
@DataProvider(name = "jsonAPIData")
public Object[][] getJSONAPIData() {
    JSONDataProvider jsonProvider = new JSONDataProvider();
    List<Map<String, Object>> apiData = jsonProvider.getData("src/test/resources/testdata/TestData.json", "apiTestData");
    // Convert to Object[][]
}
```

### CSV Data Provider
```java
@DataProvider(name = "csvDatabaseData")
public Object[][] getCSVDatabaseData() {
    CSVDataProvider csvProvider = new CSVDataProvider();
    return csvProvider.getData("src/test/resources/testdata/TestData.csv", 3);
}
```

## 🥒 Cucumber BDD Testing

### Feature File Example
```gherkin
@UI @Login
Feature: User Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  @Smoke @Positive
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters valid username "testuser@example.com"
    And the user enters valid password "TestPassword123"
    And the user clicks the login button
    Then the user should be successfully logged in
    And the user should see the dashboard
```

### Step Definitions
```java
@When("the user enters valid username {string}")
public void the_user_enters_valid_username(String username) {
    WebElement usernameField = driver.findElement(By.id("username"));
    usernameField.sendKeys(username);
}

@Then("the user should be successfully logged in")
public void the_user_should_be_successfully_logged_in() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.urlContains("/dashboard"));
}
```

## 🔄 Parallel Execution

### TestNG Configuration
```xml
<suite name="Hybrid Automation Framework Test Suite" 
       parallel="methods" 
       thread-count="4" 
       data-provider-thread-count="2">
    
    <test name="UI Tests" parallel="methods" thread-count="2">
        <classes>
            <class name="com.automation.tests.ui.UITestSuite"/>
        </classes>
    </test>
    
    <test name="API Tests" parallel="methods" thread-count="2">
        <classes>
            <class name="com.automation.tests.api.APITestSuite"/>
        </classes>
    </test>
</suite>
```

### Thread-Safe WebDriver Management
```java
// Get WebDriver for current thread
WebDriver driver = WebDriverManager.getDriver();

// Get WebDriverWait for current thread
WebDriverWait wait = WebDriverManager.getWait();

// Quit WebDriver for current thread
WebDriverManager.quitDriver();
```

## 🔁 Retry Logic

### Retry Configuration
```properties
retry.count=2
retry.interval=1000
```

### Retry Implementation
```java
@Test(retryAnalyzer = RetryListener.class)
public void testWithRetry() {
    // Test implementation with automatic retry on failure
}
```

## 📈 Reporting

### Extent Reports
- HTML reports with detailed test information
- Screenshots on failure
- Test execution timeline
- Environment information

### Allure Reports
- Rich HTML reports
- Test execution graphs
- Attachments support
- Integration with CI/CD

## 🚀 Running Tests

### Maven Commands
```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml

# Run with specific environment
mvn test -Denvironment=qa

# Run with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true

# Run with parallel execution
mvn test -Dparallel=true -DthreadCount=4
```

### TestNG XML Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Hybrid Automation Framework Test Suite" 
       parallel="methods" 
       thread-count="4">
    
    <test name="UI Tests">
        <classes>
            <class name="com.automation.tests.ui.UITestSuite"/>
        </classes>
    </test>
    
    <test name="API Tests">
        <classes>
            <class name="com.automation.tests.api.APITestSuite"/>
        </classes>
    </test>
    
    <test name="Database Tests">
        <classes>
            <class name="com.automation.tests.database.DatabaseTestSuite"/>
        </classes>
    </test>
    
    <test name="Mixed Tests">
        <classes>
            <class name="com.automation.tests.mixed.MixedTestSuite"/>
        </classes>
    </test>
    
    <test name="Cucumber Tests">
        <classes>
            <class name="com.automation.tests.cucumber.CucumberTestRunner"/>
        </classes>
    </test>
</suite>
```

## 🛠️ Framework Components

### 1. ConfigManager
Singleton class for managing environment-specific configurations.

### 2. WebDriverManager
Thread-safe WebDriver management for parallel execution.

### 3. Base Classes
- `BaseUITest`: Common UI testing utilities
- `BaseAPITest`: Common API testing utilities
- `BaseDBTest`: Common database testing utilities

### 4. API Classes
- `GETRequest`: GET request handling
- `POSTRequest`: POST request handling
- `PUTRequest`: PUT request handling
- `DELETERequest`: DELETE request handling

### 5. Data Providers
- `ExcelDataProvider`: Excel file data handling
- `JSONDataProvider`: JSON file data handling
- `CSVDataProvider`: CSV file data handling

### 6. Listeners
- `TestListener`: Test reporting and screenshot capture
- `RetryListener`: Retry logic for failed tests

### 7. Cucumber Components
- `CucumberHooks`: Lifecycle management
- `CucumberTestRunner`: Test execution
- Step Definitions: BDD implementation

## 📋 Best Practices

### 1. Page Object Model
- Create page objects for each page
- Keep locators in page objects
- Implement reusable methods

### 2. Data Management
- Use external data files
- Implement data providers
- Keep test data separate from test logic

### 3. Error Handling
- Implement proper exception handling
- Use retry mechanisms
- Capture screenshots on failure

### 4. Logging
- Use structured logging
- Include relevant context
- Log at appropriate levels

### 5. Test Organization
- Group related tests
- Use descriptive test names
- Implement proper setup and teardown

## 🔧 Customization

### Adding New Environments
1. Update `config.properties` with new environment settings
2. Add environment-specific URLs and credentials
3. Update test data for new environment

### Adding New Data Sources
1. Create new data provider class
2. Implement data reading logic
3. Add to test classes as data providers

### Adding New Test Types
1. Create new base class if needed
2. Implement test utilities
3. Add to test suite configuration

## 📞 Support

For questions and support:
- Check the framework documentation
- Review example test implementations
- Consult the logging output for debugging

## 🤝 Contributing

1. Follow the existing code structure
2. Add proper documentation
3. Include unit tests for new features
4. Update this README as needed

---

**Note**: This framework is designed to be extensible and maintainable. Follow the established patterns when adding new features or test types.