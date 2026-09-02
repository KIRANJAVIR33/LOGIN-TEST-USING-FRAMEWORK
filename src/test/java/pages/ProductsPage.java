package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By sortDropdown = By.className("product_sort_container");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductsPageDisplayed() {
        return isDisplayed(pageTitle);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * Adds a product to the cart using its saucedemo product id slug,
     * e.g. "sauce-labs-backpack" for the "Sauce Labs Backpack" item.
     * The add-to-cart button id follows the pattern: add-to-cart-<slug>
     */
    public void addProductToCart(String productSlug) {
        click(By.id("add-to-cart-" + productSlug));
    }

    public void removeProductFromCart(String productSlug) {
        click(By.id("remove-" + productSlug));
    }

    public int getCartItemCount() {
        if (!isDisplayed(cartBadge)) return 0;
        return Integer.parseInt(getText(cartBadge));
    }

    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }

    public void sortBy(String visibleOptionText) {
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(driver.findElement(sortDropdown));
        select.selectByVisibleText(visibleOptionText);
    }
}
