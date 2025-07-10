package com.framework.stepdefinitions;

import com.framework.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final LoginPage loginPage = new LoginPage();

    @Given("^user is on the login page$")
    public void user_is_on_login_page() {
        String baseUrl = com.framework.config.ConfigManager.getInstance().get("uiBaseUrl");
        com.framework.drivers.DriverFactory.getDriver().get(baseUrl + "/login");
    }

    @When("^user logs in with username "([^"]*)" and password "([^"]*)"$")
    public void user_logs_in(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("^home page should be displayed$")
    public void home_page_displayed() {
        String current = com.framework.drivers.DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue(current.contains("home"), "Home page not displayed");
    }
}