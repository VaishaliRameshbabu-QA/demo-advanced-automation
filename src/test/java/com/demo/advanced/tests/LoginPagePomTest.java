
package com.demo.advanced.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.advanced.pages.LoginPage;

public class LoginPagePomTest {

    @Test
    public void verifyLoginPageUsingPageObjectModel() {

        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();

            LoginPage loginPage = new LoginPage(driver);

            loginPage.open();

            Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Login page controls should be displayed"
            );

            System.out.println(
                "PASS: Login page verified using Page Object Model."
            );

        } finally {
            driver.quit();
        }
    }
}
