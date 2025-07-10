package com.automation.tests.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Test Runner for BDD testing
 * Integrates Cucumber with TestNG
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.stepdefinitions", "com.automation.core"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "junit:target/cucumber-reports/CucumberTestReport.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false,
    publish = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    private static final Logger logger = LogManager.getLogger(CucumberTestRunner.class);

    @Override
    public void setUpClass() {
        logger.info("Setting up Cucumber Test Runner");
        super.setUpClass();
    }

    @Override
    public void tearDownClass() {
        logger.info("Tearing down Cucumber Test Runner");
        super.tearDownClass();
    }
}