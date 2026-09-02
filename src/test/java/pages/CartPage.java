package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return getElements(cartItems).size();
    }

    public List<String> getItemNames() {
        return getElements(itemNames).stream()
                .map(org.openqa.selenium.WebElement::getText)
                .collect(Collectors.toList());
    }

    public CheckoutStepOnePage clickCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }

    public ProductsPage continueShopping() {
        click(continueShoppingButton);
        return new ProductsPage(driver);
    }
}
