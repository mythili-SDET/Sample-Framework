# Hybrid Automation Framework

A comprehensive Java-based automation framework supporting UI, API, and Database testing with Cucumber BDD, TestNG, and parallel execution capabilities.

## 🚀 Framework Features

### 🔹 **Modular and Layered Architecture**
- **Page Object Model (POM)** for UI tests
- **Separate base classes** for UI, API, and Database testing
- **Utility classes** for reusable functions and common actions
- **Comprehensive logging** with Log4j2

### 🔹 **Multi-Layer Testing Support**
- **UI Testing**: Selenium WebDriver with cross-browser support
- **API Testing**: RestAssured with comprehensive HTTP method support
- **Database Testing**: JDBC with transaction management
- **Mixed Scenarios**: Combined UI + API + DB in single test flows

### 🔹 **Cucumber BDD Integration**
- **Hooks** for setup/teardown (@Before/@After)
- **Smart component initialization** based on scenario tags
- **Mixed scenario support** (API + UI + DB in one test)
- **Environment-specific hooks** (@qa, @uat, @stage, @prod)

### 🔹 **Data-Driven Testing**
- **Excel support** (Apache POI) - .xlsx and .xls formats
- **JSON support** (Jackson) - multiple JSON structures
- **CSV support** (OpenCSV) - custom separators and configurations
- **Dynamic data injection** into UI, API, and DB tests
- **Unified DataProvider** with auto-format detection

### 🔹 **Advanced Features**
- **Thread-safe execution** with singleton pattern
- **Parallel execution** with Cucumber and TestNG
- **Retry logic** for failed tests
- **Environment configuration** (QA, UAT, STAGE, PROD)
- **Comprehensive reporting** (Allure, Cucumber HTML, TestNG)
- **Screenshot capture** on test failures

## 📁 Project Structure

```
src/
├── main/java/com/automation/
│   ├── core/
│   │   ├── ConfigManager.java          # Configuration management
│   │   ├── DriverManager.java          # WebDriver management
│   │   ├── UIBaseTest.java             # UI base class
│   │   ├── APIBaseTest.java            # API base class
│   │   ├── DatabaseBaseTest.java       # Database base class
│   │   └── TokenManager.java           # API token management
│   └── utils/
│       ├── ExcelDataReader.java        # Excel data reader
│       ├── CSVDataReader.java          # CSV data reader
│       ├── JSONDataReader.java         # JSON data reader
│       └── DataProvider.java           # Unified data provider
├── test/java/com/automation/
│   ├── hooks/
│   │   ├── TestHooks.java              # Cucumber hooks
│   │   └── TestContext.java            # Test context management
│   ├── pages/
│   │   ├── BasePage.java               # Page Object base class
│   │   └── LoginPage.java              # Sample page object
│   ├── stepdefinitions/
│   │   ├── UIStepDefinitions.java      # UI step definitions
│   │   ├── APIStepDefinitions.java     # API step definitions
│   │   └── DatabaseStepDefinitions.java # DB step definitions
│   └── runners/
│       ├── CucumberTestRunner.java     # Main test runner
│       ├── SmokeTestRunner.java        # Smoke tests
│       ├── UITestRunner.java           # UI-only tests
│       ├── APITestRunner.java          # API-only tests
│       ├── DatabaseTestRunner.java     # DB-only tests
│       └── HybridTestRunner.java       # Mixed scenarios
└── test/resources/
    ├── features/                       # Cucumber feature files
    ├── testdata/                       # Test data files
    ├── config.properties               # Configuration file
    ├── testng.xml                      # TestNG configuration
    └── log4j2.xml                      # Logging configuration
```

## 🛠️ Setup Instructions

### Prerequisites
- **Java 11+**
- **Maven 3.6+**
- **Chrome/Firefox/Edge browser**
- **Database** (MySQL/PostgreSQL - optional)

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd hybrid-automation-framework
```

2. **Install dependencies**
```bash
mvn clean install -DskipTests
```

3. **Configure the framework**
   - Update `src/test/resources/config.properties`
   - Set your application URLs, database connections, and credentials

4. **Run tests**
```bash
# Run all tests
mvn test

# Run specific test suites
mvn test -Dtest=SmokeTestRunner
mvn test -Dtest=UITestRunner
mvn test -Dtest=APITestRunner
mvn test -Dtest=DatabaseTestRunner
mvn test -Dtest=HybridTestRunner
```

## 🏷️ Scenario Tags

### Component Tags
- `@ui` - UI/Web interface tests
- `@api` - REST API tests  
- `@db` - Database tests
- `@hybrid` - Mixed component tests

### Environment Tags
- `@qa` - QA environment
- `@uat` - UAT environment
- `@stage` - Staging environment
- `@prod` - Production environment

### Test Type Tags
- `@smoke` - Critical functionality tests
- `@regression` - Full regression suite
- `@positive` - Happy path scenarios
- `@negative` - Error/edge case scenarios

### Data Source Tags
- `@excel` - Excel data-driven tests
- `@json` - JSON data-driven tests
- `@csv` - CSV data-driven tests

## 📊 Data-Driven Testing

### Excel Data Format
```excel
| username | password    | expectedResult |
|----------|-------------|----------------|
| admin    | password123 | success        |
| user1    | userpass    | success        |
| invalid  | wrongpass   | failure        |
```

### JSON Data Format
```json
{
  "loginData": [
    {
      "username": "admin",
      "password": "password123",
      "expectedResult": "success"
    }
  ]
}
```

### CSV Data Format
```csv
username,password,expectedResult
admin,password123,success
user1,userpass,success
invalid,wrongpass,failure
```

## 🎯 Usage Examples

### Simple UI Test
```gherkin
@ui @smoke
Scenario: Successful login
  Given I am on the login page
  When I login with credentials "admin" and "password123"
  Then the current URL should contain "dashboard"
```

### API Test
```gherkin
@api @smoke
Scenario: Create user via API
  Given I set the API base URL
  And I set the authentication token
  When I send a POST request to "/users"
  Then the response status code should be 201
```

### Database Test
```gherkin
@db @smoke
Scenario: Validate user in database
  When I execute the query "SELECT * FROM users WHERE email = 'test@example.com'"
  Then the query should return 1 row(s)
  And the column "name" in first row should be "Test User"
```

### Hybrid Test (UI + API + DB)
```gherkin
@ui @api @db @hybrid
Scenario: Complete user workflow
  # Database: Verify initial state
  When I execute the query "SELECT COUNT(*) FROM users"
  Then I extract column "count" from first row and store as "initialCount"
  
  # API: Create user
  Given I set the API base URL
  When I send a POST request to "/users"
  Then the response status code should be 201
  
  # Database: Verify user created
  When I execute the query "SELECT * FROM users WHERE email = 'new@user.com'"
  Then the query should return 1 row(s)
  
  # UI: Verify user appears in interface
  Given I am on the login page
  When I login with credentials "admin" and "password123"
  Then the current URL should contain "dashboard"
```

## ⚙️ Configuration

### Environment Configuration
```properties
# Environment
environment=qa

# Browser Settings
browser=chrome
headless=false

# URLs
base.url=https://qa.example.com
api.base.url=https://qa-api.example.com/v1

# Database
db.url=jdbc:mysql://qa-db:3306/testdb
db.username=testuser
db.password=testpass
```

### Parallel Execution
```xml
<!-- TestNG XML -->
<suite name="Parallel Tests" parallel="methods" thread-count="5">
  <test name="API Tests">
    <classes>
      <class name="com.automation.runners.APITestRunner"/>
    </classes>
  </test>
</suite>
```

## 📈 Reporting

### Available Reports
- **Allure Reports**: Detailed test execution reports
- **Cucumber HTML**: BDD scenario reports
- **TestNG Reports**: TestNG execution reports
- **Logs**: Comprehensive logging with different levels

### Generate Reports
```bash
# Generate Allure reports
mvn allure:serve

# View Cucumber reports
open target/cucumber-reports/cucumber-html-reports/overview-features.html
```

## 🔧 Advanced Features

### Custom Step Definitions
```java
@When("I perform custom action with {string}")
public void performCustomAction(String parameter) {
    // Your custom implementation
    logger.info("Executing custom action with: {}", parameter);
}
```

### Data Extraction and Chaining
```gherkin
When I extract "id" from response and store as "userId"
And I use extracted "userId" as path parameter "id"
And I send a GET request to "/users/{id}"
```

### Environment Switching
```bash
# Run tests in different environments
mvn test -Denvironment=qa
mvn test -Denvironment=uat
mvn test -Denvironment=stage
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For questions and support:
- Create an issue in the repository
- Check the documentation in the `/docs` folder
- Review the example tests in `/src/test/resources/features`

---

**Happy Testing! 🚀**