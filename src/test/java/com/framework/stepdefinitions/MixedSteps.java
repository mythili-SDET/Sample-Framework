package com.framework.stepdefinitions;

import com.framework.api.HttpGet;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class MixedSteps {

    private final HttpGet get = new HttpGet();
    private int statusCode;

    @Given("^a GET request is sent to "([^"]*)"$")
    public void send_get(String resource) {
        statusCode = get.get(resource).getStatusCode();
    }

    @Then("^the API response code should be (\d+)$")
    public void validate_status(int code) {
        Assert.assertEquals(statusCode, code);
    }
}