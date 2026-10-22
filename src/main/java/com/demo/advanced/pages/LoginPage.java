
package com.demo.advanced.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    private final By emailField =
            By.cssSelector("input[placeholder='email@example.com']");

    private final By passwordField =
            By.cssSelector("input[type='password']");

    private final By loginButton =
            By.id("login");

    private final By pageBody =
            By.tagName("body");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(
            "https://rahulshettyacademy.com/client/#/auth/login"
        );
    }

    public boolean isLoginPageDisplayed() {
        return driver.findElement(emailField).isDisplayed()
                && driver.findElement(passwordField).isDisplayed()
                && driver.findElement(loginButton).isDisplayed();
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getPageText() {
        return driver.findElement(pageBody).getText();
    }

    public boolean isEmailFieldDisplayed() {
        return driver.findElement(emailField).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return driver.findElement(passwordField).isDisplayed();
    }

    public boolean isLoginButtonDisplayed() {
        return driver.findElement(loginButton).isDisplayed();
    }
}
