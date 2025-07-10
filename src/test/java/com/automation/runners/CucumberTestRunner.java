package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Main Cucumber TestNG Runner
 * Supports parallel execution and comprehensive test configuration
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-reports",
                "json:target/cucumber-reports/cucumber-json-reports/Cucumber.json",
                "junit:target/cucumber-reports/cucumber-junit-reports/Cucumber.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@regression",
        monochrome = true,
        publish = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    /**
     * DataProvider for parallel execution
     * @return scenarios for parallel execution
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}