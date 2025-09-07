# LoggerManager Implementation Guide

## 🎯 Overview

The `LoggerManager` provides centralized, thread-safe logging across UI, API, and DB layers with:
- **Category-specific loggers** (UI, API, DB, Test, Framework)
- **Structured logging** with context and operation details
- **Automatic log file separation** by category
- **Performance tracking** for API calls
- **Error handling** with detailed context

## 🏗️ Architecture

### Logger Categories
```
LoggerManager
├── UI Logger      → ui.log (UI operations, element interactions)
├── API Logger     → api.log (HTTP requests/responses, timing)
├── DB Logger      → db.log (SQL queries, connection events)
├── Test Logger    → test.log (Test steps, assertions)
└── Framework Logger → framework.log (Driver setup, configuration)
```

### Log File Structure
```
target/logs/
├── automation.log      # All logs combined
├── ui.log             # UI-specific logs
├── api.log            # API-specific logs
├── db.log             # Database logs
├── test.log           # Test execution logs
└── framework.log      # Framework setup logs
```

## 🚀 Usage Examples

### 1. **UI Layer Logging**

#### SeleniumHelper Integration
```java
public class SeleniumHelper {
    private static final Logger logger = LoggerManager.getLogger(SeleniumHelper.class);
    
    public void click(By locator) {
        LoggerManager.logUIOperation("CLICK", locator.toString(), null);
        try {
            WebElement element = findElement(locator);
            element.click();
            logger.info("Successfully clicked element: {}", locator);
        } catch (Exception e) {
            LoggerManager.logError(logger, "UI Click", "Failed to click element", e);
            throw new ElementNotInteractableException("Element not clickable: " + locator, e);
        }
    }
    
    public void type(By locator, String text) {
        LoggerManager.logUIOperation("TYPE", locator.toString(), text);
        try {
            WebElement element = findElement(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed '{}' into element: {}", text, locator);
        } catch (Exception e) {
            LoggerManager.logError(logger, "UI Type", "Failed to type text", e);
            throw new ElementNotInteractableException("Element not typeable: " + locator, e);
        }
    }
}
```

#### BaseUITest Integration
```java
public class BaseUITest {
    private static final Logger logger = LoggerManager.getLogger(BaseUITest.class);
    
    @BeforeMethod
    public void setUp() {
        LoggerManager.logFrameworkEvent("WebDriver Initialized", 
            "Test: " + this.getClass().getSimpleName());
        // ... setup code
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            LoggerManager.logError(logger, "Test Failure", 
                "Test failed: " + result.getName(), result.getThrowable());
        }
    }
}
```

### 2. **API Layer Logging**

#### ApiClient Integration
```java
public class ApiClient extends APIBaseClass {
    private static final Logger logger = LoggerManager.getAPILogger();
    
    public Response get(String url, Map<String, ?> pathParams, Map<String, ?> queryParams,
                        Map<String, String> headers) {
        LoggerManager.logAPIRequest("GET", url, null);
        long startTime = System.currentTimeMillis();
        
        try {
            RequestSpecification req = given().spec(requestSpec);
            if (headers != null) req.headers(headers);
            if (pathParams != null) req.pathParams(pathParams);
            if (queryParams != null) req.queryParams(queryParams);
            
            Response response = req.get(url).then().extract().response();
            
            long responseTime = System.currentTimeMillis() - startTime;
            LoggerManager.logAPIResponse(response.getStatusCode(), response.asString(), responseTime);
            
            return response;
        } catch (Exception e) {
            LoggerManager.logError(logger, "API GET", "Request failed", e);
            throw new ApiRequestException("GET request failed: " + url, e);
        }
    }
    
    public Response post(String url, Object body, Map<String, String> headers) {
        String requestBody = null;
        if (body != null) {
            requestBody = JsonUtil.objectToJson(body);
        }
        
        LoggerManager.logAPIRequest("POST", url, requestBody);
        long startTime = System.currentTimeMillis();
        
        try {
            RequestSpecification req = given().spec(requestSpec);
            if (headers != null) req.headers(headers);
            if (body != null) req.body(requestBody);
            
            Response response = req.post(url).then().extract().response();
            
            long responseTime = System.currentTimeMillis() - startTime;
            LoggerManager.logAPIResponse(response.getStatusCode(), response.asString(), responseTime);
            
            return response;
        } catch (Exception e) {
            LoggerManager.logError(logger, "API POST", "Request failed", e);
            throw new ApiRequestException("POST request failed: " + url, e);
        }
    }
}
```

### 3. **Database Layer Logging**

#### DBUtils Integration
```java
public class DBUtils {
    private static final Logger logger = LoggerManager.getDBLogger();
    
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        LoggerManager.logDBOperation("UPDATE", sql, params);
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            int result = ps.executeUpdate();
            logger.info("DB Update executed successfully, affected rows: {}", result);
            return result;
        } catch (SQLException e) {
            LoggerManager.logError(logger, "DB Update", "Failed to execute update", e);
            throw new DBQueryException("Update failed: " + sql, e);
        }
    }
    
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        LoggerManager.logDBOperation("SELECT", sql, params);
        try {
            Connection conn = DBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            setParameters(ps, params);
            ResultSet result = ps.executeQuery();
            logger.info("DB Query executed successfully");
            return result;
        } catch (SQLException e) {
            LoggerManager.logError(logger, "DB Query", "Failed to execute query", e);
            throw new DBQueryException("Query failed: " + sql, e);
        }
    }
}
```

### 4. **Test Layer Logging**

#### Step Definitions Integration
```java
public class EmployeeSearchSteps {
    private static final Logger logger = LoggerManager.getTestLogger();
    
    @Given("I search for employee with ID {string}")
    public void iSearchForEmployeeWithID(String employeeId) {
        LoggerManager.logTestStep("Search employee by ID: " + employeeId, "START");
        try {
            EmployeeBusinessFlow flow = new EmployeeBusinessFlow();
            flow.searchEmployeeById(employeeId);
            LoggerManager.logTestStep("Search employee by ID: " + employeeId, "PASS");
        } catch (Exception e) {
            LoggerManager.logTestStep("Search employee by ID: " + employeeId, "FAIL");
            LoggerManager.logError(logger, "Employee Search", "Search failed", e);
            throw e;
        }
    }
    
    @Then("the employee details should be stored in database")
    public void theEmployeeDetailsShouldBeStoredInDatabase() {
        LoggerManager.logTestStep("Verify employee in database", "START");
        try {
            // Verification logic
            LoggerManager.logTestStep("Verify employee in database", "PASS");
        } catch (Exception e) {
            LoggerManager.logTestStep("Verify employee in database", "FAIL");
            LoggerManager.logError(logger, "Database Verification", "Verification failed", e);
            throw e;
        }
    }
}
```

### 5. **Framework Layer Logging**

#### Configuration and Setup
```java
public class ConfigManager {
    private static final Logger logger = LoggerManager.getFrameworkLogger();
    
    public static ConfigManager getInstance() {
        LoggerManager.logFrameworkEvent("Config Manager", "Initializing configuration");
        // ... initialization logic
    }
    
    public String getProperty(String key) {
        LoggerManager.logFrameworkEvent("Config Property", "Retrieving: " + key);
        // ... property retrieval
    }
}
```

## 📊 Log Output Examples

### UI Logs (ui.log)
```
2024-01-15 10:30:15.123 [main] INFO  UI - UI Operation: CLICK on element 'By.id: submit-btn'
2024-01-15 10:30:15.456 [main] INFO  UI - Successfully clicked element: By.id: submit-btn
2024-01-15 10:30:16.789 [main] INFO  UI - UI Operation: TYPE on element 'By.id: username' with value 'testuser'
2024-01-15 10:30:17.012 [main] INFO  UI - Successfully typed 'testuser' into element: By.id: username
```

### API Logs (api.log)
```
2024-01-15 10:30:20.123 [main] INFO  API - API Request: GET https://api.example.com/employees/123
2024-01-15 10:30:20.456 [main] INFO  API - API Response: Status 200 in 333ms, Body: {"id":123,"name":"John Doe"}
2024-01-15 10:30:25.789 [main] INFO  API - API Request: POST https://api.example.com/employees with body: {"name":"Jane Doe","email":"jane@example.com"}
2024-01-15 10:30:26.012 [main] INFO  API - API Response: Status 201 in 223ms, Body: {"id":124,"name":"Jane Doe"}
```

### DB Logs (db.log)
```
2024-01-15 10:30:30.123 [main] INFO  DB - DB Operation: SELECT - Query: SELECT * FROM employees WHERE id = ? with parameters: [123]
2024-01-15 10:30:30.456 [main] INFO  DB - DB Query executed successfully
2024-01-15 10:30:35.789 [main] INFO  DB - DB Operation: UPDATE - Query: UPDATE employees SET name = ? WHERE id = ? with parameters: [John Smith, 123]
2024-01-15 10:30:35.912 [main] INFO  DB - DB Update executed successfully, affected rows: 1
```

### Test Logs (test.log)
```
2024-01-15 10:30:40.123 [main] INFO  TEST - Test Step: Search employee by ID: 123 - Status: START
2024-01-15 10:30:40.456 [main] INFO  TEST - Test Step: Search employee by ID: 123 - Status: PASS
2024-01-15 10:30:45.789 [main] INFO  TEST - Test Step: Verify employee in database - Status: START
2024-01-15 10:30:46.012 [main] INFO  TEST - Test Step: Verify employee in database - Status: PASS
```

### Framework Logs (framework.log)
```
2024-01-15 10:30:00.123 [main] INFO  FRAMEWORK - Framework Event: UI Test Suite Start - Details: Environment: qa
2024-01-15 10:30:05.456 [main] INFO  FRAMEWORK - Framework Event: WebDriver Initialized - Details: Test: EmployeeSearchTest
2024-01-15 10:30:10.789 [main] INFO  FRAMEWORK - Framework Event: Config Manager - Details: Initializing configuration
```

## 🔧 Configuration

### Log4j2 Configuration (log4j2.xml)
```xml
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_FILE_PATH">target/logs</Property>
    </Properties>
    
    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <ThresholdFilter level="INFO" onMatch="ACCEPT" onMismatch="DENY"/>
        </Console>
        
        <!-- Category-specific File Appenders -->
        <RollingFile name="UILogger" fileName="${LOG_FILE_PATH}/ui.log"
                     filePattern="${LOG_FILE_PATH}/ui-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="5MB"/>
            </Policies>
            <DefaultRolloverStrategy max="5"/>
        </RollingFile>
        
        <!-- Similar appenders for API, DB, Test, Framework -->
    </Appenders>
    
    <Loggers>
        <Logger name="UI" level="INFO" additivity="false">
            <AppenderRef ref="UILogger"/>
            <AppenderRef ref="Console"/>
        </Logger>
        
        <!-- Similar loggers for API, DB, Test, Framework -->
        
        <Root level="INFO">
            <AppenderRef ref="FileAppender"/>
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

## 🎯 Best Practices

### 1. **Use Appropriate Logger Categories**
```java
// UI operations
LoggerManager.logUIOperation("CLICK", locator.toString(), null);

// API operations
LoggerManager.logAPIRequest("POST", url, requestBody);
LoggerManager.logAPIResponse(statusCode, responseBody, responseTime);

// DB operations
LoggerManager.logDBOperation("SELECT", sql, parameters);

// Test steps
LoggerManager.logTestStep("User login", "PASS");

// Framework events
LoggerManager.logFrameworkEvent("Driver Setup", "Chrome browser initialized");
```

### 2. **Error Logging with Context**
```java
try {
    // Operation
} catch (Exception e) {
    LoggerManager.logError(logger, "Operation Context", "Detailed error message", e);
    throw new CustomException("User-friendly message", e);
}
```

### 3. **Performance Tracking**
```java
long startTime = System.currentTimeMillis();
// API call
long responseTime = System.currentTimeMillis() - startTime;
LoggerManager.logAPIResponse(statusCode, responseBody, responseTime);
```

### 4. **Structured Logging**
```java
// Good: Structured with context
LoggerManager.logUIOperation("TYPE", "username field", "testuser");

// Avoid: Unstructured logging
logger.info("Typing testuser into username field");
```

## 🚀 Benefits

1. **Centralized Logging**: Single point of control for all logging
2. **Category Separation**: Easy to filter logs by layer (UI/API/DB)
3. **Performance Tracking**: Built-in timing for API calls
4. **Error Context**: Detailed error information with context
5. **Thread Safety**: ConcurrentHashMap for thread-safe logger caching
6. **Configurable**: Easy to adjust log levels and outputs
7. **File Rotation**: Automatic log file rotation and cleanup
8. **Integration Ready**: Works seamlessly with Allure and other reporting tools

## 🔍 Troubleshooting

### Common Issues:
- **Log files not created**: Check `target/logs` directory permissions
- **Missing logs**: Verify log4j2.xml configuration
- **Performance impact**: Adjust log levels (DEBUG/INFO/WARN/ERROR)
- **File size**: Configure rotation policies in log4j2.xml

### Debug Tips:
- Enable DEBUG level for detailed logging
- Check console output for immediate feedback
- Use log file timestamps for correlation with test execution
- Monitor log file sizes and rotation

---

**Remember**: The LoggerManager provides comprehensive logging across all framework layers, making debugging and monitoring much easier while maintaining performance and organization.
