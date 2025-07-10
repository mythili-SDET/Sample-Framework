package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * API Test Runner
 * Executes only API/REST service tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/api-html-reports",
                "json:target/cucumber-reports/api-json-reports/API.json",
                "junit:target/cucumber-reports/api-junit-reports/API.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@api and not @ui and not @db",
        monochrome = true,
        publish = false
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}