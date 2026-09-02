# Selenium Test Automation Framework

A Selenium + TestNG automation framework built around the **Page Object Model (POM)**, testing the [SauceDemo](https://www.saucedemo.com/) e-commerce demo site. Covers login validation, data-driven negative testing, and a full end-to-end checkout journey, with HTML reporting and screenshot capture on failure.

## Tech stack

| Tool | Purpose |
|---|---|
| Java 21 | Language |
| Maven | Build & dependency management |
| Selenium 4.21 | Browser automation |
| TestNG 7.10.2 | Test runner, assertions, data providers |
| WebDriverManager 5.8.0 | Auto-manages browser driver binaries |
| ExtentReports 5.1.1 | HTML test reports |
| Log4j2 2.23.1 | Logging |
| Apache POI | Excel data support (available for future data-driven use) |

## Project structure

```
Selenium-Framework/
├── pom.xml                          # Maven dependencies & build config
├── testng.xml                       # TestNG suite definition
├── src/test/java/
│   ├── base/
│   │   ├── DriverManager.java       # Creates & destroys the WebDriver instance
│   │   └── BaseTest.java            # @BeforeMethod/@AfterMethod setup-teardown
│   ├── pages/                       # Page Object Model classes
│   │   ├── BasePage.java            # Reusable click/type/wait helpers
│   │   ├── LoginPage.java
│   │   ├── ProductsPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutStepOnePage.java
│   │   ├── CheckoutStepTwoPage.java
│   │   └── CheckoutCompletePage.java
│   ├── tests/
│   │   ├── LoginTest.java           # Valid/invalid/locked-out/data-driven login tests
│   │   └── CheckoutTest.java        # Full cart-to-checkout user journey tests
│   ├── listeners/
│   │   └── TestListener.java        # TestNG hooks -> ExtentReports + screenshots
│   └── utils/
│       ├── ConfigReader.java        # Reads config.properties
│       └── ExtentReportManager.java # Builds the HTML report
└── src/test/resources/
    ├── config.properties            # URL, credentials, timeouts, paths
    └── log4j2.xml                   # Logging configuration
```

## How it works

1. **`config.properties`** holds all environment data — nothing is hardcoded in the test code.
2. **`ConfigReader`** loads that file once and exposes it to every other class.
3. **`DriverManager`** creates a `ThreadLocal` Chrome browser instance (supports parallel execution).
4. **`BaseTest`** opens the browser before each test and closes it after, via TestNG's `@BeforeMethod`/`@AfterMethod`.
5. **Page Objects** (`LoginPage`, `ProductsPage`, `CartPage`, etc.) each own the locators and actions for one screen, and chain into each other — e.g. `loginPage.loginAs(...)` returns a `ProductsPage`.
6. **Test classes** (`LoginTest`, `CheckoutTest`) orchestrate Page Objects and make assertions — they contain no locator or wait logic themselves.
7. **`TestListener`** hooks into TestNG's pass/fail events, logs results to ExtentReports, and captures a screenshot on failure.

## Test coverage

**`LoginTest`**
- Valid login
- Invalid password / invalid username
- Locked-out user
- Empty credentials (both fields / username only)
- Data-driven negative login across multiple bad credential combinations (`@DataProvider`)

**`CheckoutTest`**
- Full journey: login → add to cart → cart page → checkout form → order confirmation
- Adding multiple products and verifying cart count
- Removing a product and verifying cart count updates
- Checkout blocked when required customer info is missing

## Setup

**Prerequisites:** Java 21, Maven, Google Chrome installed.

```bash
git clone https://github.com/KIRANJAVIR33/LOGIN-TEST-USING-FRAMEWORK.git
cd LOGIN-TEST-USING-FRAMEWORK
```

No manual driver setup needed — WebDriverManager downloads the matching ChromeDriver automatically at runtime.

## Configuration

Edit `src/test/resources/config.properties` to change test data or behavior:

```properties
url=https://www.saucedemo.com/
headless=false          # set true to run without opening a visible browser
implicit.wait=30
explicit.wait=35
valid.username=standard_user
valid.password=secret_sauce
```

## Running the tests

```bash
mvn test
```

This runs the suite defined in `testng.xml` via the Maven Surefire plugin.

To run a single test class from your IDE, right-click the class (e.g. `LoginTest`) → **Run**.

## Reports & logs

- **HTML report:** generated at `reports/Report_<timestamp>.html` after every run — open it in a browser to see pass/fail status per test.
- **Screenshots on failure:** saved to `screenshots/<testName>.png`.
- **Execution logs:** written to `logs/automation.log`.

## Possible next steps

- Add Excel/CSV-driven test data using the already-included Apache POI dependency
- Parameterize browser choice (Chrome/Firefox/Edge) via `config.properties`
- Add CI pipeline (GitHub Actions) to run `mvn test` on every push
- Extend `CheckoutTest` with multi-item price total verification
