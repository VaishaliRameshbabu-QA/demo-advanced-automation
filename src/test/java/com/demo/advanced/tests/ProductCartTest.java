
package com.demo.advanced.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductCartTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final By spinner =
            By.cssSelector(".ngx-spinner-overlay");

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(
                driver, Duration.ofSeconds(25));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addProductToCartAndVerify() {

        // 1. Read credentials from Eclipse Run Configurations
        String email = System.getenv("TEST_EMAIL");
        String password = System.getenv("TEST_PASSWORD");

        Assert.assertNotNull(
                email,
                "TEST_EMAIL is missing from Run Configurations.");

        Assert.assertNotNull(
                password,
                "TEST_PASSWORD is missing from Run Configurations.");

        // 2. Open the login page
        driver.get(
                "https://rahulshettyacademy.com/client/#/auth/login");

        // 3. Enter credentials and log in
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("userEmail")))
                .sendKeys(email);

        driver.findElement(By.id("userPassword"))
                .sendKeys(password);

        driver.findElement(By.id("login")).click();

        // 4. Wait for the product dashboard
        wait.until(ExpectedConditions.urlContains(
                "/dashboard/dash"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".card")));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                spinner));

        // 5. Identify the first product
        String productName = wait.until(d -> {
            try {
                List<WebElement> cards =
                        d.findElements(By.cssSelector(".card"));

                for (WebElement card : cards) {
                    if (!card.isDisplayed()) {
                        continue;
                    }

                    List<WebElement> headings =
                            card.findElements(By.cssSelector("h5"));

                    if (!headings.isEmpty()) {
                        String name =
                                headings.get(0).getText().trim();

                        if (!name.isEmpty()) {
                            return name;
                        }
                    }
                }
            } catch (StaleElementReferenceException e) {
                // Retry if the product list refreshes.
            }

            return null;
        });

        Assert.assertNotNull(
                productName,
                "Could not identify the first product.");

        System.out.println("Selected product: " + productName);

        // 6. Add the first product to the cart
        By addToCart = By.xpath(
                "(//button[contains(normalize-space(.),"
                + "'Add To Cart')])[1]");

        wait.until(ExpectedConditions.elementToBeClickable(
                addToCart)).click();

        // IMPORTANT: Wait for the loading overlay to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                spinner));

        System.out.println("PASS: Product added to cart.");

        // 7. Wait for Cart button and click it
        By cartButton = By.cssSelector(
                "button[routerlink='/dashboard/cart']");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                spinner));

        wait.until(ExpectedConditions.elementToBeClickable(
                cartButton)).click();

        // 8. Wait for the cart page
        wait.until(ExpectedConditions.urlContains(
                "/dashboard/cart"));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                spinner));

        // 9. Verify that the selected product is in the cart
        Boolean productFound = wait.until(d ->
                d.findElement(By.tagName("body"))
                        .getText()
                        .contains(productName));

        Assert.assertTrue(
                productFound,
                "Product was not found in the cart: "
                        + productName);

        System.out.println(
                "PASS: Product verified in cart.");
    }
}
