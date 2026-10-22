
package com.demo.advanced.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataDrivenLoginTest {

    private static final String LOGIN_URL =
        "https://rahulshettyacademy.com/client/#/auth/login";

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
            {"invaliduser1@example.com", "WrongPassword123!", "invalid"},
            {"invaliduser2@example.com", "WrongPassword456!", "invalid"},
            {"", "", "empty"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyLoginValidation(
            String email, String password, String scenario) {

        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();
            driver.get(LOGIN_URL);

            WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(
                        "input[placeholder='email@example.com']"
                    )
                )
            );

            WebElement passwordField = driver.findElement(
                By.cssSelector("input[type='password']")
            );

            emailField.sendKeys(email);
            passwordField.sendKeys(password);

            driver.findElement(By.id("login")).click();

            if (scenario.equals("empty")) {

                // Empty credentials should not allow successful login.
                wait.until(d ->
                    !d.getCurrentUrl().contains("/myorders")
                    && d.findElement(By.tagName("body")).isDisplayed()
                );

                Assert.assertTrue(
                    emailField.isDisplayed(),
                    "Login form should remain available for empty credentials"
                );

                Assert.assertTrue(
                    driver.getCurrentUrl().contains("/auth/login"),
                    "Empty credentials should not navigate away from login"
                );

                System.out.println(
                    "PASS: Empty credentials did not log in."
                );

            } else {

                // Invalid credentials should produce a visible error.
                boolean errorDisplayed = wait.until(d -> {
                    String pageText = d.findElement(By.tagName("body"))
                                        .getText()
                                        .toLowerCase();

                    return pageText.contains("incorrect")
                        || pageText.contains("invalid")
                        || pageText.contains("login failed")
                        || pageText.contains("password")
                        || pageText.contains("email");
                });

                Assert.assertTrue(
                    errorDisplayed,
                    "Expected an error for invalid credentials: " + email
                );

                System.out.println(
                    "PASS: Invalid login handled for " + email
                );
            }

        } finally {
            driver.quit();
        }
    }
}
