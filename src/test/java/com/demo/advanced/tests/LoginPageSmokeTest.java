package com.demo.advanced.tests;

import com.demo.advanced.base.BaseTest;
import com.demo.advanced.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageSmokeTest extends BaseTest {
    @Test
    public void verifyLoginPageElementsAreDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should display email, password, and Login button.");
    }
}
