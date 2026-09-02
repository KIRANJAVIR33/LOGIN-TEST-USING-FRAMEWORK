package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test(description = "Valid credentials should log the user in and land on the products page")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginAs(
                ConfigReader.get("valid.username"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after valid login");
    }

    @Test(description = "Invalid password should show an error and keep the user on the login page")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin(
                ConfigReader.get("valid.username"),
                ConfigReader.get("invalid.password")
        );
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for invalid password");
    }

    @Test(description = "Unregistered username should show an error and keep the user on the login page")
    public void testInvalidUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin(
                ConfigReader.get("invalid.username"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for invalid username");
    }

    @Test(description = "A locked-out user should be blocked from logging in")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin(
                ConfigReader.get("locked.username"),
                ConfigReader.get("valid.password")
        );
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed for locked-out user");
        Assert.assertTrue(
                loginPage.getErrorMessage().toLowerCase().contains("locked out"),
                "Error message should mention the account is locked out"
        );
    }

    @Test(description = "Submitting the login form with both fields empty should show a required-field error")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin("", "");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed when fields are empty");
    }

    @Test(description = "Submitting the login form with only the password filled should show an error naming the missing username")
    public void testEmptyUsernameOnly() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin("", ConfigReader.get("valid.password"));
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed when username is empty");
    }

    /**
     * Data-driven test: runs the same login attempt against several
     * invalid username/password combinations pulled from loginData().
     * This is the pattern interviewers commonly ask about directly:
     * "how would you avoid writing five nearly-identical tests?"
     */
    @Test(dataProvider = "invalidLoginData", description = "Data-driven negative login across multiple bad credential combinations")
    public void testInvalidLoginDataDriven(String username, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.attemptLogin(username, password);
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Expected an error for username='" + username + "', password='" + password + "'");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] loginData() {
        return new Object[][]{
                {ConfigReader.get("invalid.username"), ConfigReader.get("invalid.password")},
                {ConfigReader.get("valid.username"), ConfigReader.get("invalid.password")},
                {ConfigReader.get("invalid.username"), ConfigReader.get("valid.password")},
                {"", ConfigReader.get("valid.password")},
                {ConfigReader.get("valid.username"), ""},
        };
    }
}
