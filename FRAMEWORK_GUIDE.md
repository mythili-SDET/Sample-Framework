# Automation Framework Guide for Newbies

## 🎯 What is this Framework?

This is a **hybrid automation framework** that supports:
- **UI Testing** (Selenium WebDriver)
- **API Testing** (REST Assured)
- **Database Testing** (JDBC)
- **Mixed Scenarios** (UI + API + DB in one test)

## 🏗️ Framework Architecture (Scalable & Modular)

### 1. **Layered Design**
```
┌─────────────────────────────────────┐
│           Test Layer                │ ← Your test classes
├─────────────────────────────────────┤
│         Business Flow Layer         │ ← Reusable business logic
├─────────────────────────────────────┤
│         Helper/Utility Layer        │ ← Common operations
├─────────────────────────────────────┤
│         Base/Driver Layer           │ ← WebDriver, API, DB setup
├─────────────────────────────────────┤
│         Configuration Layer         │ ← Environment settings
└─────────────────────────────────────┘
```

### 2. **Module Organization**
```
src/main/java/com/automation/
├── api/           # API testing components
├── app/flows/     # Business logic flows
├── config/        # Configuration management
├── db/            # Database utilities
├── driver/        # WebDriver management
├── exceptions/    # Custom exceptions
├── helpers/       # Reusable utilities
├── listeners/     # TestNG listeners
├── reporting/     # Allure reporting
├── ui/            # UI testing components
└── utils/         # Data handling utilities
```

## 🚀 Getting Started (Newbie Guide)

### Step 1: Setup Your Test Class
```java
public class MyTest extends BaseUITest {
    
    @Test
    public void testUserLogin() {
        // Navigate to login page
        navigateToPath("/login");
        
        // Use helpers for actions
        seleniumHelper.type(By.id("username"), "testuser");
        seleniumHelper.type(By.id("password"), "password123");
        seleniumHelper.click(By.id("login-btn"));
        
        // Wait for success
        waitHelper.waitForUrlContains("/dashboard");
    }
}
```

### Step 2: API Testing
```java
public class MyApiTest {
    
    @Test
    public void testCreateUser() {
        ApiClient client = new ApiClient();
        
        // Create user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User");
        userData.put("email", "test@example.com");
        
        // Make API call
        Response response = client.post(ApiEndpoints.USER_CREATE, userData, null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 201);
        String userId = ResponseValidator.getValueByJsonPath(response, "id");
    }
}
```

### Step 3: Database Testing
```java
public class MyDbTest {
    
    @Test
    public void testUserInDatabase() {
        // Insert test data
        DBUtils.executeUpdate("INSERT INTO users (name, email) VALUES (?, ?)", 
                             "Test User", "test@example.com");
        
        // Query and validate
        ResultSet rs = DBUtils.executeQuery("SELECT * FROM users WHERE email = ?", "test@example.com");
        assert rs.next();
    }
}
```

### Step 4: Mixed Scenario (UI + API + DB)
```java
public class EndToEndTest extends BaseUITest {
    
    @Test
    public void testCompleteUserFlow() {
        // 1. Create user via API
        ApiClient client = new ApiClient();
        Response response = client.post(ApiEndpoints.USER_CREATE, userData, null);
        String userId = ResponseValidator.getValueByJsonPath(response, "id");
        
        // 2. Verify in database
        ResultSet rs = DBUtils.executeQuery("SELECT * FROM users WHERE id = ?", userId);
        assert rs.next();
        
        // 3. Login via UI
        navigateToPath("/login");
        seleniumHelper.type(By.id("username"), "testuser");
        seleniumHelper.type(By.id("password"), "password123");
        seleniumHelper.click(By.id("login-btn"));
        
        // 4. Verify dashboard
        waitHelper.waitForUrlContains("/dashboard");
    }
}
```

## 🧩 Exception Handling (Best Practices)

### Custom Exceptions Available:
- **UIException**: Base for UI failures
- **ElementNotFoundException**: Element not found
- **ElementNotInteractableException**: Element not clickable/typeable
- **ApiRequestException**: API request failures
- **ApiResponseException**: API response validation failures
- **DBException**: Database operation failures
- **DBConnectionException**: Connection failures
- **DBQueryException**: Query execution failures
- **ConfigurationException**: Config loading failures
- **TestDataException**: Test data processing failures

### Usage Example:
```java
try {
    seleniumHelper.click(By.id("submit-btn"));
} catch (ElementNotFoundException e) {
    logger.error("Submit button not found: {}", e.getMessage());
    // Take screenshot, retry, or fail gracefully
} catch (ElementNotInteractableException e) {
    logger.error("Submit button not clickable: {}", e.getMessage());
    // Try JavaScript click or wait
}
```

## 📊 Data Handling

### Excel to JSON:
```java
// Read Excel
Object[][] data = TestDataProvider.fromExcel("TestData.xlsx", "Sheet1");

// Convert to JSON
String json = JsonUtil.objectToJson(data);
```

### Properties to JSON:
```java
Properties props = new Properties();
props.load(new FileInputStream("config.properties"));

Map<String, Object> map = new HashMap<>();
props.forEach((k,v) -> map.put(k.toString(), v));
String json = JsonUtil.mapToJson(map);
```

### CSV to JSON:
```java
List<Map<String, String>> csvData = TestDataProvider.fromCsv("data.csv");
String json = JsonUtil.objectToJson(csvData);
```

## 📈 Reporting & Screenshots

### Allure Integration:
- **Per-step screenshots** automatically captured
- **API request/response** logging
- **Test execution timeline**
- **Environment information**

### Generate Reports:
```bash
# Run tests
mvn test -Denv=qa

# Generate Allure report
allure generate target/allure-results -o target/allure-report --clean
allure open target/allure-report
```

## 🔧 Making Framework Reusable (JAR Creation)

### Step 1: Create JAR
```bash
# Build JAR with dependencies
mvn clean package

# Or create executable JAR
mvn clean package assembly:single
```

### Step 2: Use in Other Projects
```xml
<!-- Add to other project's pom.xml -->
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>automation-framework</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/automation-framework.jar</systemPath>
</dependency>
```

### Step 3: Import in Your Tests
```java
import com.automation.ui.base.BaseUITest;
import com.automation.api.core.clients.ApiClient;
import com.automation.utils.DBUtils;
import com.automation.exceptions.UIException;
```

## 🎯 Best Practices for Scalability

### 1. **Modular Design**
- Keep UI, API, DB logic separate
- Use business flows for complex scenarios
- Create reusable helper methods

### 2. **Configuration Management**
- Environment-specific properties
- Command-line overrides
- Centralized configuration

### 3. **Error Handling**
- Use custom exceptions
- Implement retry mechanisms
- Graceful failure handling

### 4. **Data Management**
- External test data files
- Data providers for TestNG
- JSON conversion for CI/CD

### 5. **Reporting**
- Allure integration
- Screenshot capture
- Step-by-step logging

## 🚀 Quick Start Checklist

1. ✅ **Setup**: Clone framework, configure properties
2. ✅ **Test Data**: Add Excel/CSV/JSON files to `src/test/resources/testdata/`
3. ✅ **Configuration**: Update `config.properties` with your URLs/credentials
4. ✅ **Write Tests**: Extend `BaseUITest` or create API/DB tests
5. ✅ **Run**: `mvn test -Denv=qa`
6. ✅ **Report**: Generate Allure reports
7. ✅ **Package**: Create JAR for reuse

## 🔍 Troubleshooting

### Common Issues:
- **Driver not found**: Check browser drivers in `drivers/` folder
- **Config errors**: Verify `config.properties` paths and values
- **DB connection**: Check database credentials and connectivity
- **API failures**: Verify endpoints and authentication tokens

### Debug Tips:
- Enable debug logging in `log4j2.xml`
- Check Allure attachments for screenshots
- Use `@Step` annotations for better reporting
- Implement retry mechanisms for flaky tests

---

**Remember**: This framework is designed to be **modular**, **scalable**, and **reusable**. Start with simple tests and gradually build complex scenarios using the provided components.
