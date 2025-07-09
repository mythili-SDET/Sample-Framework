# Automation Framework (Selenium + API + DB)

Welcome to the **Automation Framework** built in Java. The goal is to provide a single, extensible test-automation platform that covers:

* Web UI tests using Selenium WebDriver and TestNG
* RESTful API tests using REST-Assured
* Database validation using JDBC
* Data-driven testing with Excel (Apache POI) and CSV (OpenCSV)

The framework is in **pilot stage** and purposely opinionated but easy to extend while requirements crystallise.

---
## 1. Prerequisites

1. **Java 17** or newer (confirm with `java -version`)
2. **Maven 3.9+** (confirm with `mvn -v`)
3. **Chrome / Edge / Firefox** browsers installed
4. (Optional) Docker for containerised browsers via Selenium Grid

> **Tip:** You can use [SDKMAN](https://sdkman.io/) to manage multiple Java versions locally.

---
## 2. Getting Started

```bash
# clone the repo
$ git clone <repo-url>
$ cd automation-framework

# run all test suites (UI + API + DB)
$ mvn clean test
```

Run an individual suite defined in `testng.xml`:

```bash
mvn clean test -Dsuite=ui
```

Run tests in parallel threads (default is 2):

```bash
mvn clean test -Dthreads=4
```

---
## 3. Project Structure

```
automation-framework
 ├── pom.xml                ↠ Maven build file with all dependencies
 ├── testng.xml             ↠ Master TestNG suite
 ├── src
 │   ├── main
 │   │   └── java
 │   │       └── com.example.framework
 │   │           ├── base        ↠ Base test classes
 │   │           ├── config      ↠ Property loader / ConfigManager
 │   │           ├── driver      ↠ WebDriverFactory implementing ThreadLocal pattern
 │   │           └── utils       ↠ Excel, CSV, API, DB helper utilities
 │   └── test
 │       └── java
 │           └── com.example.tests
 │               ├── api
 │               ├── db
 │               └── ui
 └── src
     └── test
         └── resources
             ├── config.properties  ↠ Environment specific settings
             └── testdata.xlsx      ↠ Sample data
```

---
## 4. Configuration

All runtime settings are managed via `src/test/resources/config.properties` and can be overridden from the command line:

```properties
base.url=https://demo.opencart.com
api.base.url=https://jsonplaceholder.typicode.com
browser=chrome
implicit.wait=10
mysql.url=jdbc:mysql://localhost:3306/demo
mysql.user=root
mysql.password=secret
```

Override at runtime:

```bash
mvn test -Dbrowser=firefox -Dbase.url=https://staging-url
```

---
## 5. Writing Tests

### UI Example (`LoginTest.java`)
```java
@Test
public void userCanLogin() {
    LoginPage login = new LoginPage();
    HomePage home  = login.loginAs("user","pass");
    Assert.assertTrue(home.isAvatarDisplayed());
}
```

### API Example (`UserAPITest.java`)
```java
Response response = APIUtils.get("/users/1");
response.then().statusCode(200)
        .body("username", equalTo("Bret"));
```

### DB Example (`DBConnectionTest.java`)
```java
String query = "SELECT * FROM users WHERE id = 1";
ResultSet rs = DBUtils.executeQuery(query);
Assert.assertTrue(rs.next());
```

---
## 6. Extending the Framework

1. **Page Object Model** – Create a new class inside `com.example.framework.pages` representing each screen.
2. **Reusable components** – Prefer composition via utility classes in `utils` package.
3. **Reporting** – Integrate Allure or Extent Reports easily; hooks available in `BaseTest`.
4. **Parallel execution** – Controlled by the `parallel` and `thread-count` attributes in TestNG suite.

---
## 7. CI / CD

A sample GitHub Actions workflow is provided in `.github/workflows/ci.yml` to run tests on every push. Replace secrets and tune matrix as needed.

---
## 8. Troubleshooting

* **Drivers not found** – WebDriverManager automatically resolves binaries. Ensure the chosen browser is installed.
* **SSL issues on APIs** – Use `RelaxedHTTPSValidation` in `APIUtils`.
* **Database connection refused** – Check `mysql.url`, ensure DB server is reachable, and port open.

---
## 9. Contributing

1. Fork & clone the repository
2. Create a new branch `git checkout -b feature/my-awesome-change`
3. Follow existing code style (Google Java Style)
4. Submit a Pull Request

---
## 10. License

[MIT](LICENSE)

---

Happy testing! 🚀