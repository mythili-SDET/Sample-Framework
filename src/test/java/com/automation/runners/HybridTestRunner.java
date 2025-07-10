package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Hybrid Test Runner
 * Executes mixed scenarios combining UI, API, and Database tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automation.stepdefinitions",
                "com.automation.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/hybrid-html-reports",
                "json:target/cucumber-reports/hybrid-json-reports/Hybrid.json",
                "junit:target/cucumber-reports/hybrid-junit-reports/Hybrid.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@hybrid or (@ui and @api) or (@ui and @db) or (@api and @db) or (@ui and @api and @db)",
        monochrome = true,
        publish = false
)
public class HybridTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}