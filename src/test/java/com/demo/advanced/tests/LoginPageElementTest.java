
package com.demo.advanced.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageElementTest {

    @Test
    public void verifyLoginPageElements() {

        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();

            driver.get(
                "https://rahulshettyacademy.com/client/#/auth/login"
            );

            WebElement emailField = driver.findElement(
                By.cssSelector("input[placeholder='email@example.com']")
            );

            WebElement passwordField = driver.findElement(
                By.cssSelector("input[type='password']")
            );

            WebElement loginButton = driver.findElement(
                By.id("login")
            );

            Assert.assertTrue(
                emailField.isDisplayed(),
                "Email field is not displayed"
            );

            Assert.assertTrue(
                passwordField.isDisplayed(),
                "Password field is not displayed"
            );

            Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed"
            );

            System.out.println(
                "PASS: Email, Password and Login button are displayed."
            );

        } finally {
            driver.quit();
        }
    }
}
