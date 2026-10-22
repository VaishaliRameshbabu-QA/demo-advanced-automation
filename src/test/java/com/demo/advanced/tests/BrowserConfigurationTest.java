
package com.demo.advanced.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserConfigurationTest {

    @Test
    public void verifyBrowserConfiguration() {

        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();

            driver.get("https://rahulshettyacademy.com/client/#/auth/login");

            System.out.println("Browser Title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());

            Assert.assertTrue(
                driver.getCurrentUrl().contains("rahulshettyacademy.com"),
                "Website URL verification failed"
            );

        } finally {
            driver.quit();
        }
    }
}
