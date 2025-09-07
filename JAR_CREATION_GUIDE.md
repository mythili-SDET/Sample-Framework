# JAR Creation & Reusability Guide

## 🎯 Making Framework Reusable

### Step 1: Build JAR with Dependencies
```bash
# Clean and package with all dependencies
mvn clean package assembly:single

# This creates:
# target/automation-framework-1.0.0-jar-with-dependencies.jar
```

### Step 2: Install to Local Maven Repository
```bash
# Install to local .m2 repository
mvn clean install

# This makes it available for other projects
```

### Step 3: Use in Other Projects

#### Option A: Maven Dependency (Recommended)
```xml
<!-- Add to other project's pom.xml -->
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>automation-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Option B: System Dependency
```xml
<!-- Copy JAR to lib folder and reference -->
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>automation-framework</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/automation-framework.jar</systemPath>
</dependency>
```

## 🏗️ Framework Architecture for Scalability

### 1. **Modular Design Principles**
```
┌─────────────────────────────────────┐
│           Test Layer                │ ← Your specific tests
├─────────────────────────────────────┤
│         Business Flow Layer         │ ← Reusable business logic
├─────────────────────────────────────┤
│         Helper/Utility Layer        │ ← Common operations
├─────────────────────────────────────┤
│         Base/Driver Layer           │ ← Framework core
├─────────────────────────────────────┤
│         Configuration Layer         │ ← Environment management
└─────────────────────────────────────┘
```

### 2. **Exception Hierarchy**
```
RuntimeException
├── UIException
│   ├── ElementNotFoundException
│   └── ElementNotInteractableException
├── ApiRequestException
├── ApiResponseException
├── DBException
│   ├── DBConnectionException
│   └── DBQueryException
├── ConfigurationException
└── TestDataException
```

### 3. **Component Reusability**

#### Base Classes (Extend These)
- `BaseUITest` - For UI tests
- `BaseAPITest` - For API tests
- `BaseDBTest` - For DB tests

#### Helpers (Use These)
- `SeleniumHelper` - UI interactions
- `WaitHelper` - Explicit waits
- `ApiClient` - API calls
- `DBUtils` - Database operations

#### Utilities (Import These)
- `JsonUtil` - JSON operations
- `ExcelUtil` - Excel operations
- `DateTimeUtil` - Date/time operations
- `RandomUtils` - Random data generation

## 🚀 Usage Examples

### 1. **Simple UI Test**
```java
import com.automation.ui.base.BaseUITest;
import com.automation.helpers.SeleniumHelper;
import com.automation.exceptions.UIException;

public class LoginTest extends BaseUITest {
    
    @Test
    public void testLogin() {
        try {
            navigateToPath("/login");
            seleniumHelper.type(By.id("username"), "testuser");
            seleniumHelper.type(By.id("password"), "password123");
            seleniumHelper.click(By.id("login-btn"));
            waitHelper.waitForUrlContains("/dashboard");
        } catch (UIException e) {
            logger.error("UI test failed: {}", e.getMessage());
            throw e;
        }
    }
}
```

### 2. **API Test**
```java
import com.automation.api.core.clients.ApiClient;
import com.automation.api.core.endpoints.ApiEndpoints;
import com.automation.exceptions.ApiRequestException;

public class UserApiTest {
    
    @Test
    public void testCreateUser() {
        try {
            ApiClient client = new ApiClient();
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", "Test User");
            userData.put("email", "test@example.com");
            
            Response response = client.post(ApiEndpoints.USER_CREATE, userData, null);
            ResponseValidator.validateStatusCode(response, 201);
        } catch (ApiRequestException e) {
            logger.error("API test failed: {}", e.getMessage());
            throw e;
        }
    }
}
```

### 3. **Database Test**
```java
import com.automation.utils.DBUtils;
import com.automation.exceptions.DBException;

public class UserDbTest {
    
    @Test
    public void testUserCreation() {
        try {
            // Insert test data
            DBUtils.executeUpdate("INSERT INTO users (name, email) VALUES (?, ?)", 
                                 "Test User", "test@example.com");
            
            // Verify insertion
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM users WHERE email = ?", "test@example.com");
            assert rs.next();
        } catch (DBException e) {
            logger.error("DB test failed: {}", e.getMessage());
            throw e;
        }
    }
}
```

### 4. **Mixed Scenario (UI + API + DB)**
```java
public class EndToEndTest extends BaseUITest {
    
    @Test
    public void testCompleteUserFlow() {
        try {
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
            
        } catch (Exception e) {
            logger.error("End-to-end test failed: {}", e.getMessage());
            throw e;
        }
    }
}
```

## 🔧 Configuration for Reusability

### 1. **Environment Configuration**
```properties
# config.properties
environment=qa
browser=chrome
headless=false

# UI Configuration
ui.base.url.qa=https://qa.example.com
ui.base.url.uat=https://uat.example.com

# API Configuration
api.base.url.qa=https://api.qa.example.com
api.base.url.uat=https://api.uat.example.com

# Database Configuration
db.host.qa=qa-db.example.com
db.host.uat=uat-db.example.com
```

### 2. **Command Line Overrides**
```bash
# Override environment
mvn test -Denv=uat

# Override browser
mvn test -Dbrowser=firefox

# Override headless mode
mvn test -Dheadless=true
```

## 📊 Data Handling for CI/CD

### 1. **Excel to JSON Conversion**
```java
// Read Excel data
Object[][] data = TestDataProvider.fromExcel("TestData.xlsx", "Sheet1");

// Convert to JSON for CI/CD
String json = JsonUtil.objectToJson(data);
FileUtils.writeStringToFile("target/test-data.json", json);
```

### 2. **Properties to JSON**
```java
// Load properties
Properties props = ConfigManager.getInstance().getProperties();

// Convert to JSON
Map<String, Object> map = new HashMap<>();
props.forEach((k,v) -> map.put(k.toString(), v));
String json = JsonUtil.mapToJson(map);
```

### 3. **Test Data Versioning**
```java
// Generate timestamped test data
String timestamp = DateTimeUtil.getCurrentTimestamp();
String testDataFile = "target/test-data-" + timestamp + ".json";
FileUtils.writeJson(testDataFile, testDataMap);
```

## 🎯 Best Practices for Scalability

### 1. **Modular Design**
- Keep UI, API, DB logic separate
- Use business flows for complex scenarios
- Create reusable helper methods

### 2. **Exception Handling**
- Use custom exceptions for specific failures
- Implement retry mechanisms
- Graceful failure handling

### 3. **Configuration Management**
- Environment-specific properties
- Command-line overrides
- Centralized configuration

### 4. **Data Management**
- External test data files
- JSON conversion for CI/CD
- Data providers for TestNG

### 5. **Reporting**
- Allure integration
- Screenshot capture
- Step-by-step logging

## 🚀 Deployment Options

### 1. **JAR Distribution**
```bash
# Create executable JAR
mvn clean package assembly:single

# Distribute JAR file
cp target/automation-framework-1.0.0-jar-with-dependencies.jar /path/to/distribution/
```

### 2. **Maven Repository**
```bash
# Deploy to Maven repository
mvn clean deploy

# Use in other projects
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>automation-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 3. **Docker Container**
```dockerfile
FROM openjdk:11-jre-slim
COPY target/automation-framework-1.0.0-jar-with-dependencies.jar /app/
WORKDIR /app
CMD ["java", "-jar", "automation-framework-1.0.0-jar-with-dependencies.jar"]
```

## 🔍 Troubleshooting

### Common Issues:
- **JAR not found**: Check Maven repository or system path
- **Class not found**: Ensure all dependencies are included
- **Config errors**: Verify properties file paths
- **Driver issues**: Check browser driver locations

### Debug Tips:
- Enable debug logging
- Check Allure attachments
- Use @Step annotations
- Implement retry mechanisms

---

**Remember**: This framework is designed to be **modular**, **scalable**, and **reusable**. Start with simple tests and gradually build complex scenarios using the provided components.
