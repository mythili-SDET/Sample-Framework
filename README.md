# Robust Test Automation Framework

A comprehensive test automation framework built with Java, Selenium WebDriver, REST Assured, and TestNG that supports UI, API, and Database testing with multiple data sources.

## 🚀 Features

### Core Capabilities
- **UI Testing**: Selenium WebDriver with Page Object Model
- **API Testing**: REST Assured for comprehensive API testing
- **Database Testing**: MySQL database connectivity and operations
- **Multi-Data Support**: Excel, CSV, and JSON data sources
- **Parallel Execution**: TestNG parallel test execution
- **Reporting**: Extent Reports for detailed test reporting
- **Screenshot Capture**: Automatic screenshots on test failure
- **Logging**: Comprehensive logging with Logback

### Framework Architecture
- **Maven-based**: Easy dependency management and build process
- **Page Object Model**: Maintainable and reusable UI components
- **Configuration Management**: Centralized configuration handling
- **Data-Driven Testing**: Support for multiple data formats
- **Cross-Browser Support**: Chrome, Firefox, Edge, Safari
- **Headless Mode**: Support for headless browser execution

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL Database (for database testing)
- Chrome/Firefox/Edge browser (for UI testing)

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd robust-test-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure the framework**
   - Update `src/test/resources/config/config.properties` with your settings
   - Set up database connection details
   - Configure browser preferences

## ⚙️ Configuration

### Configuration File: `src/test/resources/config/config.properties`

```properties
# Browser Configuration
browser.name=chrome
browser.headless=false
browser.implicit.wait=10

# Database Configuration
db.url=jdbc:mysql://localhost:3306/testdb
db.username=root
db.password=password

# API Configuration
api.base.url=https://jsonplaceholder.typicode.com
api.timeout=30

# Test Data Configuration
data.file.path=src/test/resources/data/
excel.file.name=testdata.xlsx
csv.file.name=testdata.csv
json.file.name=testdata.json
```

## 📁 Project Structure

```
robust-test-framework/
├── src/
│   ├── main/java/com/testframework/
│   │   ├── api/
│   │   │   └── ApiManager.java
│   │   ├── config/
│   │   │   └── ConfigManager.java
│   │   ├── core/
│   │   │   └── BaseTest.java
│   │   ├── database/
│   │   │   └── DatabaseManager.java
│   │   ├── ui/
│   │   │   └── WebDriverManager.java
│   │   └── utils/
│   │       ├── CsvDataProvider.java
│   │       ├── ExcelDataProvider.java
│   │       └── JsonDataProvider.java
│   └── test/
│       ├── java/com/testframework/
│       │   ├── pageobjects/
│       │   │   └── GoogleHomePage.java
│       │   └── tests/
│       │       ├── ApiTest.java
│       │       ├── DatabaseTest.java
│       │       └── UITest.java
│       └── resources/
│           ├── config/
│           │   └── config.properties
│           └── data/
│               ├── testdata.csv
│               ├── testdata.json
│               └── testdata.xlsx
├── logs/
├── reports/
├── pom.xml
├── testng.xml
└── README.md
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UITest
```

### Run Tests with TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run Tests in Parallel
```bash
mvn test -Dparallel=methods -DthreadCount=3
```

### Run Tests by Groups
```bash
mvn test -Dgroups=smoke
```

## 📊 Test Reports

After test execution, reports are generated in the `reports/` directory:
- **Extent Reports**: HTML reports with detailed test results
- **Screenshots**: Failed test screenshots
- **Logs**: Detailed execution logs

## 🔧 Framework Components

### 1. BaseTest Class
The foundation class that all test classes extend:
```java
public class MyTest extends BaseTest {
    @Override
    protected boolean isUITest() {
        return true; // or false for API/DB tests
    }
    
    @Test
    public void myTest() {
        // Your test logic here
    }
}
```

### 2. Page Object Model
Create page objects for UI elements:
```java
public class LoginPage {
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
    }
}
```

### 3. Data Providers
Use different data sources for test data:

#### Excel Data
```java
List<Map<String, String>> data = excelDataProvider.readExcelData("Sheet1");
```

#### CSV Data
```java
List<Map<String, String>> data = csvDataProvider.readCsvData();
```

#### JSON Data
```java
List<Map<String, Object>> data = jsonDataProvider.readJsonData();
```

### 4. API Testing
```java
// Simple GET request
ApiManager.ApiResponse response = apiManager.simpleGet("/posts");

// POST request with body
Map<String, Object> body = new HashMap<>();
body.put("title", "Test Post");
ApiManager.ApiResponse response = apiManager.simplePost("/posts", body);
```

### 5. Database Testing
```java
// Execute query
List<Map<String, Object>> results = databaseManager.executeQuery("SELECT * FROM users");

// Insert record
Map<String, Object> data = new HashMap<>();
data.put("name", "John Doe");
databaseManager.insertRecord("users", data);
```

## 🎯 Best Practices

### 1. Test Organization
- Group related tests in the same class
- Use descriptive test method names
- Add proper test descriptions using `@Test(description = "...")`

### 2. Data Management
- Store test data in appropriate files (Excel, CSV, JSON)
- Use data providers for data-driven tests
- Keep test data separate from test logic

### 3. Page Objects
- Create page objects for each web page
- Use `@FindBy` annotations for element location
- Implement wait strategies for dynamic elements

### 4. Error Handling
- Use try-catch blocks for expected exceptions
- Log errors appropriately
- Take screenshots on test failures

### 5. Configuration
- Use configuration files for environment-specific settings
- Avoid hardcoding values in test code
- Use environment variables for sensitive data

## 🔍 Debugging

### Enable Debug Logging
Update `config.properties`:
```properties
log.level=DEBUG
```

### View Test Execution
```bash
mvn test -X
```

### Check Reports
- Open `reports/TestReport_*.html` for detailed reports
- Check `logs/testframework.log` for execution logs

## 🚀 CI/CD Integration

### GitHub Actions Example
```yaml
name: Test Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Run Tests
        run: mvn clean test
      - name: Upload Reports
        uses: actions/upload-artifact@v2
        with:
          name: test-reports
          path: reports/
```

## 📈 Extending the Framework

### Adding New Browsers
1. Update `WebDriverManager.java`
2. Add browser-specific configuration
3. Update `config.properties`

### Adding New Data Sources
1. Create new data provider class
2. Implement required methods
3. Update `BaseTest.java`

### Adding New API Endpoints
1. Extend `ApiManager.java`
2. Add endpoint-specific methods
3. Create corresponding test classes

### Adding New Database Operations
1. Extend `DatabaseManager.java`
2. Add database-specific methods
3. Create corresponding test classes

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver Issues**
   - Ensure browser drivers are in PATH
   - Check browser version compatibility
   - Verify WebDriverManager setup

2. **Database Connection Issues**
   - Verify database credentials
   - Check database server status
   - Ensure proper JDBC driver

3. **API Testing Issues**
   - Verify API endpoint availability
   - Check network connectivity
   - Validate request/response format

4. **Data Provider Issues**
   - Verify file paths and formats
   - Check file encoding (UTF-8)
   - Ensure proper data structure

## 📞 Support

For issues and questions:
1. Check the logs in `logs/` directory
2. Review test reports in `reports/` directory
3. Verify configuration in `config.properties`
4. Check browser console for UI test issues

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Selenium WebDriver for UI automation
- REST Assured for API testing
- TestNG for test execution
- Extent Reports for reporting
- Apache POI for Excel handling
- OpenCSV for CSV handling
- Jackson for JSON handling

---

**Happy Testing! 🧪✨**