
package com.demo.advanced.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.advanced.base.BaseTest;

public class ExplicitWaitTest extends BaseTest {

    @Test
    public void verifyLoginPageWithExplicitWait() {

        driver.get("https://rahulshettyacademy.com/client/#/auth/login");

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(10)
        );

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='email@example.com']")
            )
        );

        Assert.assertTrue(
            driver.findElement(
                By.cssSelector("input[placeholder='email@example.com']")
            ).isDisplayed(),
            "Email field should be visible"
        );

        System.out.println(
            "PASS: Email field appeared using Explicit Wait."
        );
    }
}
