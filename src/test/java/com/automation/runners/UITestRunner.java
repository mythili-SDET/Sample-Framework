package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * UI Test Runner
 * Executes only UI/Web interface tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/ui-html-reports",
                "json:target/cucumber-reports/ui-json-reports/UI.json",
                "junit:target/cucumber-reports/ui-junit-reports/UI.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@ui and not @api and not @db",
        monochrome = true,
        publish = false
)
public class UITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}