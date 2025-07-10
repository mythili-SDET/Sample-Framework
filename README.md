# Hybrid Automation Framework

This project is a modular, layered automation framework supporting UI, API, and Database testing using Java, Selenium WebDriver, Cucumber, TestNG, and RestAssured.

## Key Features
* **Page Object Model (POM)** for maintainable UI tests.
* **Reusable API wrappers** for GET, POST, PUT, DELETE.
* **Database utilities** using HikariCP.
* **Environment-driven configuration** (QA, UAT, STAGE) via `-Denv` flag.
* **Data-driven tests** from Excel, JSON, CSV using helper utilities.
* **Thread-safe singleton WebDriver** with parallel execution support.
* **Retry logic** for flaky tests via TestNG `RetryAnalyzer`.
* **Cucumber hooks** initialize/tear down WebDriver, API specs, and DB connections.

## Running Tests
```bash
# Default (QA) environment
mvn test

# Specify env and run in parallel
mvn test -Denv=UAT -Dcucumber.filter.tags="@ui and not @ignore"
```

## Project Structure
```
src/main/java/com/framework
 ├── api            # API base + HTTP method classes
 ├── config         # ConfigManager & env enums
 ├── drivers        # Thread-local WebDriver factory
 ├── retry          # RetryAnalyzer
 ├── ui             # Selenium base & page objects
 └── utilities      # Excel/JSON/CSV/DB/Logger helpers

src/test/java/com/framework
 ├── hooks          # Cucumber hooks
 ├── runners        # Cucumber TestNG runner
 └── stepdefinitions

src/test/resources
 ├── config         # Environment property files
 └── features       # .feature files
```

> Extend page objects, step definitions, and features as needed for your application.