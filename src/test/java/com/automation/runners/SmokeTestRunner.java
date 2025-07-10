package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Smoke Test Runner
 * Executes only critical smoke tests for quick validation
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/smoke-html-reports",
                "json:target/cucumber-reports/smoke-json-reports/Smoke.json",
                "junit:target/cucumber-reports/smoke-junit-reports/Smoke.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@smoke",
        monochrome = true,
        publish = false
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}