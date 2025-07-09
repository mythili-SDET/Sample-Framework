# Robust Test Automation Framework

A comprehensive test automation framework built with Java, Selenium, TestNG, and Maven. This framework supports UI, API, and Database testing with data-driven capabilities using Excel and CSV files.

## 🚀 Features

- **Multi-Layer Testing**: UI, API, and Database testing capabilities
- **Data-Driven Testing**: Support for Excel (.xlsx/.xls) and CSV data formats
- **Cross-Browser Support**: Chrome, Firefox, Edge, and Safari browsers
- **Parallel Execution**: TestNG parallel execution support
- **Comprehensive Reporting**: ExtentReports and Allure reporting
- **Robust Configuration**: Centralized configuration management
- **Logging**: Log4j2 integration for detailed logging
- **Screenshot Capture**: Automatic screenshot on test failures
- **Database Support**: MySQL and PostgreSQL connectivity
- **API Testing**: RESTAssured integration for API testing

## 📁 Project Structure

```
robust-test-framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/automation/
│   │   │       ├── core/              # Core framework classes
│   │   │       │   ├── ConfigManager.java
│   │   │       │   ├── DriverManager.java
│   │   │       │   └── UIBaseTest.java
│   │   │       ├── utils/             # Utility classes
│   │   │       │   ├── ExcelDataReader.java
│   │   │       │   └── CSVDataReader.java
│   │   │       ├── pages/             # Page Object classes
│   │   │       ├── api/               # API testing components
│   │   │       │   └── APIBaseTest.java
│   │   │       └── database/          # Database testing components
│   │   │           └── DatabaseManager.java
│   │   └── resources/
│   │       ├── config.properties     # Framework configuration
│   │       └── log4j2.xml           # Logging configuration
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── ui/              # UI test classes
│       │       ├── api/             # API test classes
│       │       └── database/        # Database test classes
│       └── resources/
│           ├── testng.xml           # TestNG suite configuration
│           └── testdata/            # Test data files
│               ├── login_data.csv
│               └── user_api_data.csv
├── reports/                         # Test reports
├── logs/                           # Application logs
├── target/                         # Build artifacts
├── pom.xml                         # Maven configuration
└── README.md                       # This file
```

## 🛠️ Prerequisites

- **Java 11 or higher**
- **Maven 3.6 or higher**
- **Browser drivers** (handled automatically by WebDriverManager)
- **Database** (MySQL/PostgreSQL) - optional for database testing

## ⚡ Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd robust-test-framework
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure the Framework

Edit `src/main/resources/config.properties`:

```properties
# Browser Configuration
browser=chrome
headless=false
base.url=https://your-application-url.com

# API Configuration
api.base.url=https://your-api-url.com

# Database Configuration (if needed)
db.url=jdbc:mysql://localhost:3306/testdb
db.username=your-username
db.password=your-password
```

### 4. Run Tests

```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml

# Run with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true
```

## 📋 Framework Components

### Configuration Management

The `ConfigManager` class provides centralized configuration:

```java
ConfigManager config = ConfigManager.getInstance();
String baseUrl = config.getBaseUrl();
String browser = config.getBrowser();
```

### WebDriver Management

The `DriverManager` handles browser lifecycle:

```java
DriverManager.initializeDriver();
WebDriver driver = DriverManager.getDriver();
DriverManager.quitDriver();
```

### Data-Driven Testing

#### Excel Data Reader

```java
// Read all data from Excel
List<Map<String, String>> data = ExcelDataReader.readExcelData("path/to/file.xlsx", "Sheet1");

// Use with TestNG DataProvider
@DataProvider
public Object[][] testData() {
    return ExcelDataReader.readExcelDataForTestNG("testdata.xlsx", "TestData");
}
```

#### CSV Data Reader

```java
// Read all data from CSV
List<Map<String, String>> data = CSVDataReader.readCSVData("path/to/file.csv");

// Filter data
List<Map<String, String>> filtered = CSVDataReader.filterCSVData("file.csv", "status", "active");
```

### UI Testing

Extend `UIBaseTest` for UI tests:

```java
public class LoginTest extends UIBaseTest {
    
    @Test
    public void testLogin() {
        navigateTo("https://example.com/login");
        type(By.id("username"), "testuser");
        type(By.id("password"), "password");
        click(By.id("login-button"));
        
        Assert.assertTrue(isDisplayed(By.id("dashboard")));
    }
}
```

### API Testing

Extend `APIBaseTest` for API tests:

```java
public class UserAPITest extends APIBaseTest {
    
    @Test
    public void testCreateUser() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John Doe");
        userData.put("email", "john@example.com");
        
        Response response = performPost("/users", userData);
        validateStatusCode(response, 201);
        
        String userId = extractFromResponse(response, "id");
        Assert.assertNotNull(userId);
    }
}
```

### Database Testing

Use `DatabaseManager` for database operations:

```java
DatabaseManager dbManager = new DatabaseManager("mysql");

// Execute query
List<Map<String, Object>> users = dbManager.executeQuery("SELECT * FROM users WHERE active = ?", true);

// Execute update
int rows = dbManager.executeUpdate("UPDATE users SET status = ? WHERE id = ?", "active", 123);

// Transaction management
dbManager.commit();
dbManager.rollback();
```

## 📊 Test Data Management

### Excel Files

Create Excel files with headers in the first row:

| username | password | expected_result |
|----------|----------|----------------|
| admin    | admin123 | success        |
| user1    | password | success        |
| invalid  | wrong    | failure        |

### CSV Files

Create CSV files with comma-separated values:

```csv
username,password,expected_result
admin,admin123,success
user1,password,success
invalid,wrong,failure
```

## 🔧 Configuration Options

### Browser Configuration

```properties
# Supported browsers: chrome, firefox, edge, safari
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20
page.load.timeout=30
```

### Environment Configuration

```properties
environment=dev
base.url=https://dev.example.com
api.base.url=https://api.dev.example.com
```

### Database Configuration

```properties
# MySQL
db.url=jdbc:mysql://localhost:3306/testdb
db.username=testuser
db.password=testpass
db.driver=com.mysql.cj.jdbc.Driver

# PostgreSQL
postgres.url=jdbc:postgresql://localhost:5432/testdb
postgres.username=testuser
postgres.password=testpass
postgres.driver=org.postgresql.Driver
```

## 📈 Reporting

### ExtentReports

Reports are automatically generated in the `reports/` directory.

### Allure Reports

Generate Allure reports:

```bash
# Generate report
mvn allure:report

# Serve report
mvn allure:serve
```

### Screenshots

Screenshots are automatically captured on test failures and saved to `reports/screenshots/`.

## 🏃‍♂️ Running Tests

### Command Line Options

```bash
# Browser selection
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox

# Headless mode
mvn test -Dheadless=true

# Environment
mvn test -Denvironment=staging

# Specific test groups
mvn test -Dgroups=smoke
mvn test -Dgroups=regression

# Parallel execution
mvn test -Dparallel=methods -DthreadCount=3
```

### TestNG Groups

Tests are organized by groups:

- `smoke`: Critical functionality tests
- `regression`: Comprehensive test suite
- `ui`: User interface tests
- `api`: API tests
- `database`: Database tests

## 🔍 Debugging and Troubleshooting

### Logging

Logs are written to:
- Console output
- `logs/automation.log` (all logs)
- `logs/error.log` (errors only)

### Common Issues

1. **WebDriver Issues**: WebDriverManager automatically downloads drivers
2. **Database Connection**: Verify database URL, credentials, and network connectivity
3. **Element Not Found**: Check element locators and wait conditions
4. **API Timeouts**: Adjust timeout settings in configuration

### Debug Mode

Enable debug logging in `log4j2.xml`:

```xml
<Logger name="com.automation" level="DEBUG" additivity="false">
```

## 🤝 Contributing

1. Follow the existing code structure and naming conventions
2. Add comprehensive JavaDoc comments
3. Include unit tests for new utilities
4. Update documentation for new features
5. Use meaningful commit messages

### Code Style Guidelines

- Use descriptive method and variable names
- Add comments for complex logic
- Follow Java naming conventions
- Keep methods focused and small
- Use appropriate design patterns

## 📚 Examples

### Sample UI Test

```java
@Test(dataProvider = "loginData")
public void testDataDrivenLogin(Map<String, String> testData) {
    String username = testData.get("username");
    String password = testData.get("password");
    String expectedResult = testData.get("expected_result");
    
    navigateTo(config.getBaseUrl() + "/login");
    type(By.id("username"), username);
    type(By.id("password"), password);
    click(By.id("login-btn"));
    
    if ("success".equals(expectedResult)) {
        Assert.assertTrue(isDisplayed(By.id("dashboard")));
    } else {
        Assert.assertTrue(isDisplayed(By.className("error-message")));
    }
}

@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return CSVDataReader.readCSVDataForTestNG("src/test/resources/testdata/login_data.csv");
}
```

### Sample API Test

```java
@Test
public void testUserCRUD() {
    // Create user
    Map<String, Object> userData = new HashMap<>();
    userData.put("name", "Test User");
    userData.put("email", "test@example.com");
    
    Response createResponse = performPost("/users", userData);
    validateStatusCode(createResponse, 201);
    
    String userId = extractFromResponse(createResponse, "id");
    
    // Read user
    Response getResponse = performGet("/users/" + userId);
    validateStatusCode(getResponse, 200);
    
    // Update user
    userData.put("name", "Updated User");
    Response updateResponse = performPut("/users/" + userId, userData);
    validateStatusCode(updateResponse, 200);
    
    // Delete user
    Response deleteResponse = performDelete("/users/" + userId);
    validateStatusCode(deleteResponse, 204);
}
```

### Sample Database Test

```java
@Test
public void testUserDatabase() {
    DatabaseManager db = new DatabaseManager("mysql");
    
    // Create user
    int rows = db.executeUpdate(
        "INSERT INTO users (name, email) VALUES (?, ?)",
        "Test User", "test@example.com"
    );
    Assert.assertEquals(rows, 1);
    
    // Read user
    List<Map<String, Object>> users = db.executeQuery(
        "SELECT * FROM users WHERE email = ?",
        "test@example.com"
    );
    Assert.assertFalse(users.isEmpty());
    
    // Cleanup
    db.executeUpdate("DELETE FROM users WHERE email = ?", "test@example.com");
    db.commit();
    db.closeConnection();
}
```

## 📞 Support

For questions or issues:

1. Check the logs in `logs/` directory
2. Review configuration in `config.properties`
3. Verify test data format and location
4. Check network connectivity for API/database tests

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Happy Testing! 🎯**