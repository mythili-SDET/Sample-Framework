Sample Hybrid Automation Framework

Production-ready Java framework for UI, API, and DB testing with clean layering, environment management, CI/CD-friendly test data handling, and Allure reporting with per-step screenshots.

## Project Structure (modules)
src/
- main/java/com/automation/
  - api/core/base, clients, endpoints, models
  - app/flows
  - config
  - db
  - driver
  - exceptions
  - helpers
  - listeners
  - reporting (AllureReporter)
  - ui/base, ui/pages
  - utils (Excel/JSON/CSV providers, JsonUtil, DBUtils, DateTimeUtil, RandomUtils, FileUtils)
- main/resources (config.properties, qa.properties, log4j2.xml)
- test/java/com/automation/ (hooks, runners, stepdefinitions)
- test/resources (features, testng.xml, testdata)

## Employee Search Flow (API -> DB -> Reuse)
- Call API via ApiClient; validate with ResponseValidator.
- Persist JSON response to DB with DBUtils and SqlQueries.
- Reuse cached JSON for later steps/assertions.

## Reporting and Attachments
- Allure Cucumber7 plugin enabled in CucumberTestRunner.
- TestHooks attaches per-step screenshots (@AfterStep) using DriverManager.
- AllureListener captures screenshots on TestNG failures.
- Results: target/allure-results; generate HTML with Allure CLI.

Per-step screenshot snippet:
@AfterStep
public void afterStep(Scenario scenario) {
    WebDriver driver = DriverManager.getDriver();
    if (driver != null) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.getLifecycle().addAttachment("Step Screenshot", "image/png", "png", screenshot);
    }
}

## Utils and Data Conversion to JSON
- Excel: ExcelUtil/ExcelDataProvider -> List/Map; JsonUtil.objectToJson to serialize.
- CSV: CSVDataProvider/CsvUtil -> List/Map -> JsonUtil.
- Properties: load and convert to Map<String,Object>, then JsonUtil.mapToJson.
- POJOs: JsonUtil.objectToJson(Object) via Jackson.

## Feasibility: UI -> API -> DB -> Assertions
- Framework supports mixed scenarios: UI actions, API calls, DB persistence/retrieval, and assertions across layers.

