package com.example.tests.ui;

import com.example.framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"ui"})
public class SampleUITest extends BaseTest {

    public void verifyHomePageTitle() {
        String expected = "Your Store"; // change according to base.url
        Assert.assertEquals(driver.getTitle(), expected, "Home page title mismatch");
    }
}