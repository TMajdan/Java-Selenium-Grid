# Selenium Automation Framework

UI Automation Framework built with **Java 25**, **Selenium 4.x**, **TestNG** and **Allure**.
Chrome-only, supports local and remote (Grid) execution with parallel test support.

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Runtime |
| Maven | 3.9+ | Build |
| Selenium | 4.30.0 | Browser automation |
| TestNG | 7.11.0 | Test framework |
| Allure | 2.29.1 | Reporting |
| AssertJ | 3.27.3 | Assertions |
| Lombok | 1.18.38 | Boilerplate |
| SnakeYAML | 2.4 | YAML parsing |

---

## Project Structure

```
src/main/java/
├── config/          ConfigManager, TestProperties, TimeoutConfig, SeleniumProperties
├── driver/          BaseDriver (ThreadLocal), ChromeOptionsProvider, DriverListener
├── listeners/       TestListener, AllureListener, RetryAnalyzer, RetryListener
├── pages/           BasePage, LoginPage, GoogleHomePage, GoogleResultsPage
│   └── actions/     BaseActions, BrowserActions, CheckActions, ClickActions,
│                    GetActions, SendActions
├── database/        DatabaseConnection
├── api/             ApiClient, RequestResponseLogger
├── interfaces/      JsonObject
└── utils/
    ├── wait/        WaitUtils
    ├── screenshot/  ScreenshotUtils
    ├── date/        DateUtils
    └── db/          DatabaseUtils

src/main/resources/
├── BasicSettings.yaml           Base config (environment, selenium, grid)
├── setting/ZT004Settings.yaml   Environment-specific settings (URLs, credentials)
├── users/ZT004Users.yaml        Test users per environment
└── logback.xml                  Logging config with thread name

src/test/java/
├── base/TestBase.java
└── tests/GoogleSearchTest.java
```

---

## Architecture

### Thread-Safe Driver Management

`BaseDriver` manages WebDriver instances via `ThreadLocal<WebDriver>`, ensuring each test thread gets its own isolated browser instance:

```
Thread-1 → ThreadLocal → ChromeDriver #1
Thread-2 → ThreadLocal → ChromeDriver #2
```

Parallel execution is configured in `testng.xml`:
```xml
<suite name="App2You suite" parallel="methods" thread-count="4">
```

### Allure Steps

All action methods (`click`, `sendKeys`, `navigateTo`, etc.) automatically create Allure steps via `Allure.step()`, making the report show a clear tree of actions:

```
Test Body
├── Navigate to https://www.google.com
├── Click on By.id: L2AGLb
├── Type 'Selenium WebDriver' into By.name: q
├── Wait for By.cssSelector: div#search to be visible    ← ❌
└── Read text from By.cssSelector: h3
```

Screenshots on failure are captured automatically in `@AfterMethod` and attached to Allure.

---

## Quick Start

```bash
# Run tests locally
mvn clean test

# Generate and open Allure report
mvn allure:serve

# Or generate report only
mvn allure:report
# Open target/site/allure-report/index.html
```

### Remote (Selenium Grid)

1. Start Selenium standalone: `java -jar selenium-server-4.30.0.jar standalone`
2. Set `selenium.grid: true` in `BasicSettings.yaml`
3. Run: `mvn clean test`

---

## Configuration

**`BasicSettings.yaml`** – main configuration:

```yaml
environment: ZT004

execution:
    timeout: 20          # Default explicit wait timeout (seconds)
    pageLoadTimeout: 30  # Page load timeout
    pollingInterval: 250  # Wait polling interval (ms)
    threadCount: 4       # Parallel threads

selenium:
    grid: false          # true = RemoteWebDriver, false = local ChromeDriver
    debug: false
    headless: false
    maximizeWindow: true
    windowWidth: 1920
    windowHeight: 1080

grid:
    hubUrl: "http://localhost:4444/wd/hub"
```

### Environment Overrides

Set `environment` in `BasicSettings.yaml`, then add:

- `setting/{ENV}Settings.yaml` – environment-specific URLs, API endpoints, credentials
- `users/{ENV}Users.yaml` – test users

Example for ZT004:
```
setting/ZT004Settings.yaml
users/ZT004Users.yaml
```

---

## Running Tests

```bash
mvn clean test                          # all tests
mvn test -Dtest="GoogleSearchTest"      # single class
mvn test -Dtest="GoogleSearchTest#testSearchWithKeyword"  # single method
mvn test -Dgroups=smoke                 # by TestNG group
```

> `mvn clean test` is recommended to ensure old class files are removed.

---

## Reporting

Allure generates rich reports with:
- Test execution timeline
- Steps with parameters and attachments
- Screenshots on failure
- Test logs

```bash
mvn allure:serve
```

Logs include thread names for parallel execution tracing:
```
12:34:56.789 [TestNG-test-All Tests-1] INFO  pages.GoogleHomePage - Opening Google homepage
12:34:56.789 [TestNG-test-All Tests-2] INFO  pages.actions.BrowserActions - Navigating to: https://www.google.com
```

---

## Reports

```bash
mvn allure:report     # generate HTML
mvn allure:serve      # generate + open in browser
```
