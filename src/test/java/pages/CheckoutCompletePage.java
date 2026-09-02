package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderComplete() { return isDisplayed(completeHeader); }
    public String getConfirmationMessage() { return getText(completeHeader); }
}
