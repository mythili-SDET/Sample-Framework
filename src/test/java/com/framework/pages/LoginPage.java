package com.framework.pages;

import com.framework.ui.UiBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends UiBase {

    @FindBy(id = "username")
    private WebElement userField;

    @FindBy(id = "password")
    private WebElement passField;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    public LoginPage() {
        PageFactory.initElements(driver, this);
    }

    public void login(String user, String pass) {
        wait.waitForVisibility(userField);
        userField.clear();
        userField.sendKeys(user);
        passField.clear();
        passField.sendKeys(pass);
        loginButton.click();
    }
}