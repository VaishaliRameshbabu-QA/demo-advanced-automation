
package com.demo.advanced.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.demo.advanced.base.BaseTest;

public class CsvDataDrivenTest extends BaseTest {

    private final By emailField = By.id("userEmail");
    private final By passwordField = By.id("userPassword");
    private final By loginButton = By.id("login");

    @DataProvider(name = "loginData")
    public Iterator<Object[]> loginData() throws IOException {

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("login-test-data.csv");

        Assert.assertNotNull(
                inputStream,
                "CSV not found in src/test/resources/"
        );

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        inputStream, StandardCharsets.UTF_8))) {

            List<Object[]> data = new ArrayList<>();

            String header = reader.readLine();

            Assert.assertNotNull(
                    header,
                    "CSV file is empty."
            );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length != 3) {
                    throw new IOException(
                            "Invalid CSV row: " + line
                    );
                }

                String email = values[0].trim();
                String password = values[1].trim();
                String scenario = values[2].trim();

                data.add(new Object[] {
                        email, password, scenario
                });
            }

            Assert.assertEquals(
                    data.size(),
                    3,
                    "Expected 3 test rows in the CSV."
            );

            System.out.println(
                    "CSV test cases loaded: " + data.size()
            );

            return data.iterator();
        }
    }

    @Test(dataProvider = "loginData")
    public void verifyLoginScenarios(
            String email,
            String password,
            String scenario) {

        driver.get(
                "https://rahulshettyacademy.com/client/#/auth/login"
        );

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

        WebElement emailElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        emailField
                )
        );

        WebElement passwordElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        passwordField
                )
        );

        WebElement loginElement = wait.until(
                ExpectedConditions.elementToBeClickable(
                        loginButton
                )
        );

        emailElement.clear();
        passwordElement.clear();

        if (!email.isEmpty()) {
            emailElement.sendKeys(email);
        }

        if (!password.isEmpty()) {
            passwordElement.sendKeys(password);
        }

        if (email.isEmpty() && password.isEmpty()) {

            JavascriptExecutor js =
                    (JavascriptExecutor) driver;

            loginElement.click();

            Boolean stillInvalid = (Boolean) js.executeScript(
                    "return !arguments[0].checkValidity()" +
                    " || !arguments[1].checkValidity();",
                    emailElement,
                    passwordElement
            );

            Assert.assertTrue(
                    Boolean.TRUE.equals(stillInvalid),
                    "Expected empty fields to fail validation. "
                            + "Scenario: " + scenario
            );

            Assert.assertTrue(
                    emailElement.isDisplayed()
                            && passwordElement.isDisplayed(),
                    "Login form should remain displayed. "
                            + "Scenario: " + scenario
            );

            System.out.println(
                    scenario
                            + " - PASS: Empty fields validated."
            );

        } else {

            loginElement.click();

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            emailField
                    )
            );

            Assert.assertTrue(
                    driver.getCurrentUrl().contains("/auth/login"),
                    "Invalid credentials unexpectedly left "
                            + "the login page. Scenario: " + scenario
            );

            Assert.assertTrue(
                    emailElement.isDisplayed()
                            && passwordElement.isDisplayed(),
                    "Expected login form to remain displayed. "
                            + "Scenario: " + scenario
            );

            System.out.println(
                    scenario
                            + " - PASS: Login was not successful."
            );
        }
    }
}
