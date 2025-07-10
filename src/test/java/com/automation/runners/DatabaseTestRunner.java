package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Database Test Runner
 * Executes only database-related tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/db-html-reports",
                "json:target/cucumber-reports/db-json-reports/DB.json",
                "junit:target/cucumber-reports/db-junit-reports/DB.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@db and not @ui and not @api",
        monochrome = true,
        publish = false
)
public class DatabaseTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}