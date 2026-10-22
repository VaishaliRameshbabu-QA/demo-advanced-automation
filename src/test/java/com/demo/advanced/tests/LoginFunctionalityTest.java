
package com.demo.advanced.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.advanced.base.BaseTest;

public class LoginFunctionalityTest extends BaseTest {

    @Test
    public void verifyInvalidLogin() {

        driver.get(
            "https://rahulshettyacademy.com/client/#/auth/login"
        );

        WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='email@example.com']")
            )
        ).sendKeys("invaliduser@example.com");

        driver.findElement(
            By.cssSelector("input[type='password']")
        ).sendKeys("WrongPassword123!");

        driver.findElement(By.id("login")).click();

        boolean errorDisplayed = wait.until(d ->
            d.findElement(By.tagName("body"))
             .getText()
             .toLowerCase()
             .contains("incorrect")
        );

        Assert.assertTrue(
            errorDisplayed,
            "Expected an invalid-login error message"
        );

        System.out.println(
            "PASS: Invalid login error message was displayed."
        );
    }
}
