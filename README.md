# Selenium Automation Framework

UI Automation Framework built with **Java 25**, **Selenium 4.x**, **TestNG** and **Allure**.
Chrome-only, supports local and remote execution.

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
├── config/          ConfigManager, TestConfig, TestProperties, BrowserType
├── core/            Custom exceptions
├── driver/          DriverFactory, DriverManager, DesktopCapabilitiesManager,
│                    DriverListener, SeleniumProperties
├── listeners/       TestListener, AllureListener, RetryAnalyzer, RetryListener
├── pages/           BasePage, LoginPage
├── utils/           WaitUtils, ScreenshotUtils, DateUtils, FileUtils, etc.
├── factory/         PageFactory
├── models/          User, Environment, TestDataModel
└── api/             ApiClient (REST Assured)

src/main/resources/
├── BasicSettings.yaml           Base config (env, selenium flags, paths, grid)
├── setting/ZT004Settings.yaml   Environment-specific settings
├── users/ZT004Users.yaml        Environment users
└── logback.xml

src/test/java/test/
├── base/TestBase.java
├── data/TestDataProvider.java
└── tests/GoogleSearchTest.java
```

---

## Quick Start

```bash
# Run tests locally
mvn clean test

# Generate Allure report
mvn allure:report
```

### Remote (Selenium Grid)

1. Start Selenium standalone: `java -jar selenium-server-4.30.0.jar standalone`
2. Set `selenium.grid: true` in `BasicSettings.yaml`
3. Run: `mvn clean test`

---

## Configuration

**`BasicSettings.yaml`** – all settings in one file:

```yaml
environment: ZT004

selenium:
    grid: false          # true = RemoteWebDriver, false = local
    headless: false
    maximizeWindow: true

execution:
    timeout: 20
    pageLoadTimeout: 30
    implicitlyWait: 5
    threadCount: 4
    retryCount: 2

grid:
    hubUrl: "http://localhost:4444/wd/hub"

paths:
    downloadDir: "target/downloads"
    screenshotDir: "target/screenshots"
    allureResultsDir: "target/allure-results"
```

Environment overrides: `setting/{ENV}Settings.yaml`, `users/{ENV}Users.yaml`.

---

## Running Tests

```bash
mvn clean test                          # all tests
mvn test -Dtest="GoogleSearchTest#..."  # single test
mvn test -Dgroups=smoke                 # by group
```

---

## Reports

```bash
mvn allure:report     # generate HTML
mvn allure:serve      # generate + open in browser
```
