# Selenium Automation Framework

UI Automation Framework built with **Java 25**, **Selenium 4.x**, **TestNG** and **Allure Report**.

## Tech Stack

Java 25, Selenium 4.30.0, TestNG 7.11.0, Allure 2.29.1, Maven, Lombok, AssertJ, REST Assured, SLF4J/Logback, SnakeYAML.

## Quick Start

```bash
mvn clean test
mvn allure:serve
```

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

## Running Tests

```bash
mvn clean test                          # all tests
mvn test -Dtest="GoogleSearchTest"      # single class
mvn test -Dgroups=smoke                 # by TestNG group
```

Override config via system properties:

```bash
mvn clean test -Dselenium.headless=true -Dselenium.debug=true
```

## Reporting

```bash
mvn allure:report     # generate HTML report
mvn allure:serve      # generate + open in browser
```

Allure reports include: execution timeline, steps with parameters, screenshots on failure, test logs, environment info.

## Features

- **Thread-safe** — `ThreadLocal<WebDriver>` for parallel execution
- **Selenium Grid** — local or remote driver
- **Allure reporting** — steps, screenshots on failure, browser console logs, environment info
- **Retry** — flaky tests retried up to 2x
- **YAML config** — environment-specific overrides
- **API testing** — REST Assured integration
