package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) { type(usernameField, username); }
    public void enterPassword(String password) { type(passwordField, password); }
    public void clickLogin() { click(loginButton); }

    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage(driver);
    }

    /**
     * Use for negative tests where login is expected to fail
     * and the browser is expected to stay on the login page.
     */
    public void attemptLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isErrorDisplayed() { return isDisplayed(errorMessage); }

    public String getErrorMessage() { return getText(errorMessage); }
}
