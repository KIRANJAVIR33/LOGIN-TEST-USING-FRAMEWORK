package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.ConfigReader;

public class CheckoutTest extends BaseTest {

    private ProductsPage loginAndGetProductsPage() {
        LoginPage loginPage = new LoginPage(getDriver());
        return loginPage.loginAs(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );
    }

    @Test(description = "Full end-to-end journey: login -> add item -> cart -> checkout -> order confirmation")
    public void testCompleteCheckoutFlow() {
        ProductsPage productsPage = loginAndGetProductsPage();

        productsPage.addProductToCart("sauce-labs-backpack");
        Assert.assertEquals(productsPage.getCartItemCount(), 1, "Cart badge should show 1 item after adding a product");

        CartPage cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getItemCount(), 1, "Cart page should list exactly 1 item");
        Assert.assertTrue(cartPage.getItemNames().contains("Sauce Labs Backpack"), "Cart should contain the added product");

        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne.fillInfoAndContinue("John", "Doe", "12345");

        Assert.assertTrue(checkoutStepTwo.getTotal().contains("Total"), "Order summary should display a total");

        CheckoutCompletePage completePage = checkoutStepTwo.clickFinish();
        Assert.assertTrue(completePage.isOrderComplete(), "Order confirmation should be displayed");
        Assert.assertTrue(
                completePage.getConfirmationMessage().toLowerCase().contains("thank you"),
                "Confirmation message should thank the customer"
        );
    }

    @Test(description = "Adding multiple products should reflect the correct count on the cart badge and cart page")
    public void testAddMultipleProductsToCart() {
        ProductsPage productsPage = loginAndGetProductsPage();

        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        productsPage.addProductToCart("sauce-labs-bolt-t-shirt");

        Assert.assertEquals(productsPage.getCartItemCount(), 3, "Cart badge should reflect 3 added items");

        CartPage cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getItemCount(), 3, "Cart page should list exactly 3 items");
    }

    @Test(description = "Removing a product from the products page should update the cart badge count")
    public void testRemoveProductFromCart() {
        ProductsPage productsPage = loginAndGetProductsPage();

        productsPage.addProductToCart("sauce-labs-backpack");
        Assert.assertEquals(productsPage.getCartItemCount(), 1, "Cart badge should show 1 item after adding");

        productsPage.removeProductFromCart("sauce-labs-backpack");
        Assert.assertEquals(productsPage.getCartItemCount(), 0, "Cart badge should show 0 items after removing");
    }

    @Test(description = "Checkout should block progress and show an error when required customer info is missing")
    public void testCheckoutWithMissingInfo() {
        ProductsPage productsPage = loginAndGetProductsPage();
        productsPage.addProductToCart("sauce-labs-backpack");

        CartPage cartPage = productsPage.goToCart();
        CheckoutStepOnePage checkoutStepOne = cartPage.clickCheckout();

        // Leave all fields blank and try to continue
        checkoutStepOne.fillInfoAndContinue("", "", "");

        Assert.assertTrue(checkoutStepOne.isErrorDisplayed(), "Error should be shown when checkout info is incomplete");
    }
}
