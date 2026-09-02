package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepTwoPage extends BasePage {

    private final By finishButton = By.id("finish");
    private final By summaryTotalLabel = By.className("summary_total_label");
    private final By summarySubtotalLabel = By.className("summary_subtotal_label");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public String getSubtotal() { return getText(summarySubtotalLabel); }
    public String getTotal() { return getText(summaryTotalLabel); }

    public CheckoutCompletePage clickFinish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }
}
