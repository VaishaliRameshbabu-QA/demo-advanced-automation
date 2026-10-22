
package com.demo.advanced.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.advanced.base.BaseTest;

public class EndToEndTest extends BaseTest {

    @Test
    public void completeOrderEndToEnd() {

        // 1. Read credentials
        String email = System.getenv("TEST_EMAIL");
        String password = System.getenv("TEST_PASSWORD");

        Assert.assertNotNull(
                email,
                "TEST_EMAIL is missing from Run Configurations.");

        Assert.assertNotNull(
                password,
                "TEST_PASSWORD is missing from Run Configurations.");

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(25));

        By spinner = By.cssSelector(".ngx-spinner-overlay");
        By productCards = By.cssSelector(".card");

        // 2. Open login page
        driver.get(
                "https://rahulshettyacademy.com/client/#/auth/login");

        // 3. Login
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("userEmail")))
                .sendKeys(email);

        driver.findElement(By.id("userPassword"))
                .sendKeys(password);

        driver.findElement(By.id("login")).click();

        // 4. Wait for product dashboard
        wait.until(
                ExpectedConditions.urlContains("/dashboard/dash"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        productCards));

        // 5. Identify the first product
        String productName = wait.until(d -> {
            try {
                List<WebElement> cards =
                        d.findElements(productCards);

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

        System.out.println(
                "Selected product: " + productName);

        // 6. Add the first product to cart
        By addToCart = By.xpath(
                "(//button[contains(normalize-space(.),"
                + "'Add To Cart')])[1]");

        wait.until(
                ExpectedConditions.elementToBeClickable(addToCart))
                .click();

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        spinner));

        // 7. Open cart
        By cartButton = By.cssSelector(
                "button[routerlink='/dashboard/cart']");

        wait.until(
                ExpectedConditions.elementToBeClickable(cartButton))
                .click();

        wait.until(
                ExpectedConditions.urlContains("/dashboard/cart"));

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        spinner));

        // 8. Verify product in cart
        wait.until(d ->
                d.findElement(By.tagName("body"))
                        .getText()
                        .contains(productName));

        String cartText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                cartText.contains(productName),
                "Product was not found in cart.");

        System.out.println(
                "PASS: Product verified in cart.");

        // 9. Click Checkout
        By checkoutButton = By.xpath(
                "//button[contains(normalize-space(.),"
                + "'Checkout')]");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        checkoutButton))
                .click();

        wait.until(
                ExpectedConditions.urlContains("/dashboard/order"));

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        spinner));

        // 10. Select expiry month and year
        By expiryLabel = By.xpath(
                "//div[contains(@class,'title')"
                + " and normalize-space()='Expiry Date']");

        WebElement expiryContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        expiryLabel));

        WebElement expiryMonthField =
                expiryContainer.findElement(
                        By.xpath("./following-sibling::select[1]"));

        WebElement expiryYearField =
                expiryContainer.findElement(
                        By.xpath("./following-sibling::select[2]"));

        Select monthSelect = new Select(expiryMonthField);
        monthSelect.selectByVisibleText("12");

        Select yearSelect = new Select(expiryYearField);
        yearSelect.selectByVisibleText("30");

        // 11. Enter CVV
        By cvvField = By.xpath(
                "//div[contains(@class,'title')"
                + " and contains(normalize-space(.),'CVV Code')]"
                + "/following-sibling::input[1]");

        WebElement cvvInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cvvField));

        cvvInput.clear();
        cvvInput.sendKeys("123");

        // 12. Enter cardholder name
        By cardholderField = By.xpath(
                "//div[contains(@class,'title')"
                + " and normalize-space()='Name on Card']"
                + "/following-sibling::input[1]");

        WebElement cardholderInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cardholderField));

        cardholderInput.clear();
        cardholderInput.sendKeys("Test User");

        // 13. Select India
        By countryField = By.cssSelector(
                "input[placeholder='Select Country']");

        WebElement countryInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        countryField));

        countryInput.click();
        countryInput.sendKeys("India");

        By indiaOption = By.xpath(
                "//*[normalize-space(text())='India']");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        indiaOption))
                .click();

        System.out.println(
                "PASS: Checkout details entered.");

        // 14. Locate Place Order link
        By placeOrderLink = By.cssSelector(
                "a.action__submit");

        WebElement placeOrder = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        placeOrderLink));

        System.out.println(
                "Place Order link text: ["
                + placeOrder.getText() + "]");

        Assert.assertTrue(
                placeOrder.isDisplayed(),
                "Place Order link is not displayed.");

        // Scroll link into the center of the viewport
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                placeOrder);

        // Wait until the link is clickable
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        placeOrderLink));

        // Normal Selenium click
        driver.findElement(placeOrderLink).click();

        System.out.println(
                "Place Order clicked successfully.");

        // 15. Verify order confirmation
        wait.until(
                ExpectedConditions.urlContains(
                        "/dashboard/thanks"));

        By thankYouMessage = By.xpath(
                "//*[contains(translate(normalize-space(.),"
                + "'thankyou for the order',"
                + "'THANKYOU FOR THE ORDER'),"
                + "'THANKYOU FOR THE ORDER')]");

        WebElement confirmation = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        thankYouMessage));

        Assert.assertTrue(
                confirmation.isDisplayed(),
                "Order confirmation message was not displayed.");

        System.out.println(
                "PASS: End-to-end order completed successfully.");
    }
}
