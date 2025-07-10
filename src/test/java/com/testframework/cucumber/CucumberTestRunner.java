package com.testframework.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber Test Runner for BDD Testing
 * Configures Cucumber to run with TestNG and provides various execution options
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.testframework.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml"
    },
    monochrome = true,
    dryRun = false,
    snippets = io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    
    /**
     * Override to enable parallel execution
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * UI Test Runner - Runs only UI tests
 */
@CucumberOptions(
    features = "src/test/resources/features/ui_features.feature",
    glue = {"com.testframework.steps"},
    tags = "@ui",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/ui-tests.html",
        "json:target/cucumber-reports/ui-tests.json"
    },
    monochrome = true,
    dryRun = false
)
class UITestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * API Test Runner - Runs only API tests
 */
@CucumberOptions(
    features = "src/test/resources/features/api_features.feature",
    glue = {"com.testframework.steps"},
    tags = "@api",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api-tests.html",
        "json:target/cucumber-reports/api-tests.json"
    },
    monochrome = true,
    dryRun = false
)
class APITestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Database Test Runner - Runs only database tests
 */
@CucumberOptions(
    features = "src/test/resources/features/database_features.feature",
    glue = {"com.testframework.steps"},
    tags = "@database",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/database-tests.html",
        "json:target/cucumber-reports/database-tests.json"
    },
    monochrome = true,
    dryRun = false
)
class DatabaseTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Smoke Test Runner - Runs only smoke tests
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.testframework.steps"},
    tags = "@smoke",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/smoke-tests.html",
        "json:target/cucumber-reports/smoke-tests.json"
    },
    monochrome = true,
    dryRun = false
)
class SmokeTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Regression Test Runner - Runs only regression tests
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.testframework.steps"},
    tags = "@regression",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/regression-tests.html",
        "json:target/cucumber-reports/regression-tests.json"
    },
    monochrome = true,
    dryRun = false
)
class RegressionTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}