package com.framework.ui;

import com.framework.drivers.DriverFactory;
import com.framework.utilities.WaitUtil;
import org.openqa.selenium.WebDriver;

public class UiBase {

    protected WebDriver driver;
    protected WaitUtil wait;

    public UiBase() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WaitUtil(driver);
    }
}