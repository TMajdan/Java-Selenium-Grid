# 🚀 Selenium Grid Automation Framework

Enterprise-grade UI Automation Framework built with **Java 25**, **Selenium 4.x**, **TestNG**, and **Selenium Grid 4**.
Designed for parallel cross-browser testing with Allure reporting and CI/CD readiness.

---

## 📋 Table of Contents

- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
  - [Local Execution](#1-local-execution)
  - [Selenium Grid Execution](#2-selenium-grid-execution)
  - [Parallel Execution](#3-parallel-execution)
  - [Cross-Browser Testing](#4-cross-browser-testing)
  - [Test Groups](#5-test-groups)
  - [Environment Profiles](#6-environment-profiles)
  - [Headless Mode](#7-headless-mode)
- [Docker Selenium Grid](#docker-selenium-grid)
- [Reports](#reports)
  - [Allure Report](#allure-report)
  - [ExtentReports](#extentreports)
- [CI/CD](#cicd)
  - [GitHub Actions](#github-actions)
  - [Jenkins](#jenkins)
- [Framework Components](#framework-components)
  - [Page Object Model](#page-object-model)
  - [Configuration Management](#configuration-management)
  - [Driver Management](#driver-management)
  - [Listeners](#listeners)
  - [Utilities](#utilities)
- [Writing Tests](#writing-tests)
- [TestNG Annotations Reference](#testng-annotations-reference)
- [FAQ & Troubleshooting](#faq--troubleshooting)

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Test Layer                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │  LoginTest   │  │GoogleSearch  │  │   ... more tests     │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘  │
│         │                 │                      │              │
│         └─────────────────┴──────────────────────┘              │
│                            │                                     │
│                    ┌───────▼───────┐                             │
│                    │   TestBase    │                             │
│                    │ (Before/After │                             │
│                    │  Method/Suite)│                             │
│                    └───────┬───────┘                             │
├────────────────────────────┼─────────────────────────────────────┤
│                    ┌───────▼───────┐     Framework Core           │
│                    │  DriverManager│                             │
│                    │  (ThreadLocal)│                             │
│                    └───────┬───────┘                             │
│                            │                                     │
│              ┌─────────────┼─────────────┐                       │
│              ▼             ▼             ▼                       │
│     ┌────────────┐ ┌────────────┐ ┌────────────┐                │
│     │  Chrome    │ │  Firefox   │ │   Edge     │                │
│     │  Driver    │ │  Driver    │ │  Driver    │                │
│     └────────────┘ └────────────┘ └────────────┘                │
│                            │                                     │
│              ┌─────────────┼─────────────┐                       │
│              ▼                           ▼                       │
│     ┌────────────────┐       ┌────────────────────┐             │
│     │  Local Mode    │       │  Selenium Grid     │             │
│     └────────────────┘       │  (Remote WebDriver)│             │
│                              └────────────────────┘             │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────────┐  │
│  │Config-   │ │  Page    │ │Listeners │ │   Utilities      │  │
│  │Manager   │ │  Objects │ │(Allure,  │ │(Wait, Screenshot,│  │
│  │(YAML)    │ │ (POM)    │ │ TestNG,  │ │  DataFactory)    │  │
│  └──────────┘ └──────────┘ └──────────┘ └──────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Runtime & compilation |
| Maven | 3.9+ | Build & dependency management |
| Selenium | 4.30.0 | Browser automation |
| Selenium Grid | 4.30.0 | Remote / distributed execution |
| TestNG | 7.11.0 | Test framework (parallel, groups, data providers) |
| Allure | 2.29.1 | Rich reporting framework |
| WebDriverManager | 5.9.3 | Automatic driver binary management |
| AssertJ | 3.27.3 | Fluent assertions |
| Lombok | 1.18.38 | Boilerplate reduction |
| SLF4J + Logback | 2.0.x / 1.5.x | Logging |
| SnakeYAML | 2.4 | YAML parsing |
| Jackson | 2.18.3 | JSON handling |
| REST Assured | 5.5.1 | API test layer (future use) |
| JavaFaker | 1.0.2 | Realistic test data generation |
| ExtentReports | 5.1.2 | Optional HTML reporting |

---

## Project Structure

```
SeleniumGrid/
├── pom.xml                          # Maven – dependencies, plugins, profiles
├── testng.xml                       # TestNG suite – parallel, groups, browsers
├── README.md                        # This documentation
├── Jenkinsfile                      # Jenkins CI/CD pipeline
├── .github/workflows/maven.yml      # GitHub Actions workflow
│
├── src/main/java/org/example/
│   ├── config/                      # Configuration management
│   │   ├── BrowserType.java         #   CHROME, FIREFOX, EDGE enum
│   │   ├── ExecutionMode.java       #   LOCAL, GRID enum
│   │   ├── EnvironmentConfig.java   #   Environment model (Lombok)
│   │   └── ConfigManager.java       #   Thread-safe singleton, YAML + system props
│   │
│   ├── core/                        # Custom exceptions
│   │   ├── FrameworkException.java
│   │   ├── DriverInitializationException.java
│   │   ├── ConfigurationException.java
│   │   └── ScreenshotException.java
│   │
│   ├── driver/                      # WebDriver lifecycle
│   │   ├── DriverOptions.java       #   Browser options (headless, incognito, proxy)
│   │   ├── DriverFactory.java       #   Local + Remote (Grid) driver creation
│   │   └── DriverManager.java       #   ThreadLocal<WebDriver> management
│   │
│   ├── listeners/                   # TestNG listeners
│   │   ├── TestListener.java        #   Logging, screenshots on failure
│   │   ├── AllureListener.java      #   Allure attachments (screenshot, page source)
│   │   ├── RetryAnalyzer.java       #   Flaky test retry (configurable count)
│   │   ├── RetryListener.java       #   Auto-attach RetryAnalyzer to all tests
│   │   └── ExtentReporter.java      #   Optional ExtentReports HTML reporter
│   │
│   ├── pages/                       # Page Object Model
│   │   ├── BasePage.java            #   40+ methods: click, type, wait, JS, dropdowns
│   │   └── LoginPage.java           #   Example page object with @Step
│   │
│   ├── utils/                       # Utility classes
│   │   ├── WaitUtils.java           #   WebDriverWait, no Thread.sleep, configurable timeout
│   │   ├── ScreenshotUtils.java     #   Screenshot + Allure @Attachment
│   │   ├── DateUtils.java           #   Timestamps for filenames
│   │   ├── FileUtils.java           #   Directory creation / cleanup
│   │   ├── JsonUtils.java           #   Jackson serialization
│   │   ├── YamlUtils.java           #   Jackson YAML reading/writing
│   │   ├── DataFakerUtils.java      #   JavaFaker data generation
│   │   └── TestDataFactory.java     #   Predefined + random test data
│   │
│   ├── factory/
│   │   └── PageFactory.java         #   Reflective Page Object instantiation
│   │
│   ├── models/
│   │   ├── User.java                #   User data model (Lombok @Builder)
│   │   ├── Environment.java         #   Environment model
│   │   └── TestDataModel.java       #   Generic test data model
│   │
│   └── api/                         # REST Assured API layer (future)
│       └── ApiClient.java           #   GET/POST/PUT/PATCH/DELETE client
│
├── src/main/resources/
│   ├── config/application.yaml      # Multi-environment YAML config
│   └── logback.xml                  # Logging (console + rolling file)
│
└── src/test/java/org/example/test/
    ├── base/
    │   └── TestBase.java            # @BeforeSuite / @BeforeMethod / @AfterMethod
    ├── data/
    │   └── TestDataProvider.java    # 7 TestNG DataProviders
    └── tests/
        ├── LoginTest.java           # 8 tests: login, validation, remember me
        └── GoogleSearchTest.java    # 5 tests: search, suggestions, URL verification
```

---

## Prerequisites

- **Java 25** (JDK) – [Download](https://jdk.java.net/25/)
- **Maven 3.9+** – [Download](https://maven.apache.org/download.cgi)
- **Docker** (optional, for Selenium Grid) – [Download](https://www.docker.com/)
- **Allure CLI** (optional, for reports) – [Installation guide](https://docs.qameta.io/allure-report/#_installing_a_commandline)

Verify installations:

```bash
java -version
# java version "25" 2025-09-16

mvn --version
# Apache Maven 3.9.9

docker --version   # (optional)
# Docker version 27.x
```

---

## Quick Start

```bash
# 1. Clone the repository
git clone <repo-url>
cd SeleniumGrid

# 2. Compile the project (downloads dependencies)
mvn clean compile

# 3. Run all tests (default: qa, chrome, local)
mvn clean test

# 4. Generate and open Allure report
mvn allure:serve
```

---

## Configuration

All configuration is in `src/main/resources/config/application.yaml`:

```yaml
environments:
  dev:
    baseUrl: "https://dev.example.com"
  qa:
    baseUrl: "https://qa.example.com"
  stage:
    baseUrl: "https://stage.example.com"
  prod:
    baseUrl: "https://prod.example.com"

execution:
  mode: "LOCAL"           # LOCAL | GRID
  browser: "CHROME"       # CHROME | FIREFOX | EDGE
  headless: false
  timeout: 20             # Default wait timeout (seconds)
  threadCount: 4          # Parallel thread count
  retryCount: 2           # Flaky test retries

grid:
  hubUrl: "http://localhost:4444/wd/hub"

selenium:
  maximizeWindow: true
  incognitoMode: false
  disableNotifications: true
```

Configuration override priority:
1. **System properties** (`-Dbrowser=firefox`) – highest priority
2. **testng.xml parameters** – medium priority
3. **application.yaml** – lowest priority (defaults)

---

## Running Tests

### 1. Local Execution

Run all tests locally (default mode in YAML):

```bash
# Default (qa environment, chrome browser)
mvn clean test

# With custom environment and browser
mvn clean test -Denvironment=stage -Dbrowser=firefox

# Headless mode
mvn clean test -Dexecution.headless=true

# Increase parallel threads
mvn clean test -Dexecution.threadCount=8
```

### 2. Selenium Grid Execution

Start Selenium Grid (Docker):

```bash
# Start standalone Grid
docker run -d -p 4444:4444 --name selenium-grid \
  selenium/standalone-chrome:latest

# Or start a full Grid with hub + nodes
docker run -d -p 4442-4444:4442-4444 --name selenium-hub \
  selenium/hub:latest
docker run -d --link selenium-hub:hub \
  selenium/node-chrome:latest
docker run -d --link selenium-hub:hub \
  selenium/node-firefox:latest
docker run -d --link selenium-hub:hub \
  selenium/node-edge:latest
```

Run tests on Grid:

```bash
# Set Grid URL and execution mode
mvn clean test \
  -Dexecution.mode=GRID \
  -Dgrid.hubUrl=http://localhost:4444/wd/hub

# Grid + Firefox + headless
mvn clean test \
  -Dexecution.mode=GRID \
  -Dbrowser=firefox \
  -Dexecution.headless=true

# Grid with custom hub URL
mvn clean test \
  -Dexecution.mode=GRID \
  -Dgrid.hubUrl=http://192.168.1.100:4444/wd/hub
```

### 3. Parallel Execution

The framework supports three levels of parallelism:

**a) Method-level (via testng.xml):**

```xml
<suite name="Suite" parallel="methods" thread-count="4">
```

Each `@Test` method runs in its own thread with its own WebDriver instance (ThreadLocal).

**b) Test-level (cross-browser):**

```xml
<suite name="Suite" parallel="tests" thread-count="3">
  <test name="Chrome Tests"><parameter name="browser" value="CHROME"/></test>
  <test name="Firefox Tests"><parameter name="browser" value="FIREFOX"/></test>
  <test name="Edge Tests"><parameter name="browser" value="EDGE"/></test>
</suite>
```

**c) DataProvider parallel:**

```bash
mvn clean test -DdataProviderThreadCount=4
```

**Control parallelism:**

```bash
# From command line
mvn clean test -Dexecution.threadCount=8 -DdataProviderThreadCount=4
```

> **Important:** Each thread gets its own `WebDriver` instance via `ThreadLocal<WebDriver>` in `DriverManager`. Tests are fully isolated and thread-safe.

### 4. Cross-Browser Testing

The `testng.xml` defines separate `<test>` blocks for each browser:

```bash
# Run all browsers (default from testng.xml)
mvn clean test

# Run only Chrome tests via groups
mvn clean test -Dgroups=smoke

# Override browser for all tests
mvn clean test -Dbrowser=edge
```

### 5. Test Groups

Tests are organized by TestNG groups for targeted execution:

| Group | Description | Command |
|-------|-------------|---------|
| `smoke` | Critical path, fast checks | `mvn clean test -Dgroups=smoke` |
| `regression` | Full regression suite | `mvn clean test -Dgroups=regression` |
| `login` | Authentication tests | `mvn clean test -Dgroups=login` |
| `search` | Search functionality | `mvn clean test -Dgroups=search` |
| `google` | Google-specific tests | `mvn clean test -Dgroups=google` |
| `flaky` | Tests with retry mechanism | `mvn clean test -Dgroups=flaky` |

```bash
# Combine groups
mvn clean test -Dgroups="smoke,login"
```

### 6. Environment Profiles

```bash
# Using Maven profiles (sets -Denvironment automatically)
mvn clean test -Pqa        # qa profile (default)
mvn clean test -Pstage     # stage profile
mvn clean test -Pprod      # prod profile
mvn clean test -Pdev       # dev profile

# Or directly via system property
mvn clean test -Denvironment=stage
```

### 7. Headless Mode

Run tests without a visible browser UI:

```bash
# Via system property
mvn clean test -Dexecution.headless=true

# Via YAML config (set headless: true in application.yaml)
mvn clean test

# Headless + Grid + Firefox
mvn clean test \
  -Dexecution.headless=true \
  -Dexecution.mode=GRID \
  -Dbrowser=firefox
```

---

## Docker Selenium Grid

Quick Docker Compose setup for a full Selenium Grid:

```yaml
# docker-compose.yml
version: '3.8'
services:
  selenium-hub:
    image: selenium/hub:latest
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"

  chrome:
    image: selenium/node-chrome:latest
    shm_size: '2gb'
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=4

  firefox:
    image: selenium/node-firefox:latest
    shm_size: '2gb'
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=4

  edge:
    image: selenium/node-edge:latest
    shm_size: '2gb'
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SE_NODE_MAX_SESSIONS=4
```

```bash
# Start Grid
docker-compose up -d

# Verify Grid is running
curl http://localhost:4444/status

# Run tests on Grid
mvn clean test -Dexecution.mode=GRID

# Scale nodes
docker-compose up -d --scale chrome=3 --scale firefox=2

# Stop Grid
docker-compose down
```

---

## Reports

### Allure Report

Allure provides rich, interactive test reports with screenshots, logs, and step-by-step execution.

```bash
# Generate report
mvn allure:report

# Open report in browser automatically
mvn allure:serve

# Report location
open target/allure-report/index.html
```

![Allure Report Features](https://docs.qameta.io/allure-report/images/overview.png)

Allure attachments automatically added:
- ✅ **Screenshots** on test failure
- ✅ **Page Source HTML** on test failure
- ✅ **Test logs** as text attachments
- ✅ **`@Step` annotations** for each action
- ✅ **`@Severity`, `@Epic`, `@Feature`, `@Story`** for structured reporting

### ExtentReports

Optional HTML reporting. Enable in YAML:

```yaml
reporting:
  allureEnabled: true
  extentEnabled: true     # ← enable ExtentReports
  extentReportPath: "target/extent-reports"
```

Then run tests and open:

```
target/extent-reports/ExtentReport_2026-06-16.html
```

---

## CI/CD

### GitHub Actions

The `.github/workflows/maven.yml` workflow provides:

- **Matrix builds** across Chrome, Firefox, Edge
- **Trigger on push/PR** to main/develop
- **Scheduled runs** (daily at 6 AM UTC)
- **Manual trigger** with environment/browser/headless parameters
- **Allure Report** published to GitHub Pages
- **Artifacts** (screenshots, logs, reports) retained for 7 days

```bash
# Trigger manually via GitHub UI:
# Actions → Selenium Grid Test Suite → Run workflow
```

### Jenkins

The `Jenkinsfile` provides a complete pipeline:

- **Parameterized build** (environment, browser, Grid URL, thread count)
- **Maven build** with `mvn clean test`
- **Allure Report** published as HTML
- **JUnit results** integration
- **Email notifications** on success/failure
- **Workspace cleanup** after build

```groovy
// Jenkins pipeline parameters:
// - ENVIRONMENT: qa | stage | prod | dev
// - BROWSER: chrome | firefox | edge
// - EXECUTION_MODE: LOCAL | GRID
// - HEADLESS: true/false
// - THREAD_COUNT: 1-16
```

---

## Framework Components

### Page Object Model

All page objects extend `BasePage`, which provides 40+ ready-to-use methods:

```java
public class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");

    @Step("Login with username: {username}")
    public void login(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }
}
```

### Configuration Management

`ConfigManager` is a thread-safe singleton that loads `application.yaml` once:

```java
// Typed getters
ConfigManager config = ConfigManager.getInstance();
String baseUrl = config.getBaseUrl();
BrowserType browser = config.getBrowser();
ExecutionMode mode = config.getExecutionMode();
boolean isHeadless = config.isHeadless();
int timeout = config.getTimeout();
```

### Driver Management

`DriverManager` uses `ThreadLocal<WebDriver>` for parallel safety:

```java
// In test
WebDriver driver = DriverManager.getDriver();  // creates if null

// After test
DriverManager.quitDriver();  // quits and removes from ThreadLocal
```

### Listeners

| Listener | Purpose |
|----------|---------|
| `TestListener` | Logging, screenshot on failure |
| `AllureListener` | Allure attachments (screenshot, page source, logs) |
| `RetryAnalyzer` | Retries failed tests (count from YAML) |
| `RetryListener` | Auto-attaches RetryAnalyzer to all tests |
| `ExtentReporter` | Optional ExtentReports HTML output |

### Utilities

| Utility | Description |
|---------|-------------|
| `WaitUtils` | Smart waits via `WebDriverWait` – **zero `Thread.sleep()`** |
| `ScreenshotUtils` | PNG capture + Allure `@Attachment` |
| `DateUtils` | Timestamp formatting for filenames |
| `FileUtils` | Directory management |
| `JsonUtils` | Jackson JSON serialization |
| `YamlUtils` | Jackson YAML reading/writing |
| `DataFakerUtils` | JavaFaker data generation |
| `TestDataFactory` | Predefined + random test data |

---

## Writing Tests

### Basic test structure

```java
@Epic("My Feature")
@Feature("My Sub-Feature")
@Test(groups = {"myFeature", "smoke"})
public class MyTest extends TestBase {

    private MyPage myPage;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        myPage = new MyPage();
        myPage.open();
    }

    @Test(description = "TC-001: Verify something works")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Story XYZ")
    @Description("Detailed description of what this test verifies")
    public void testSomething() {
        myPage.performAction();
        assertThat(myPage.getResult())
                .as("Result should match expected")
                .isEqualTo("expected");
    }
}
```

### Data-driven test

```java
@DataProvider(name = "myData")
public static Object[][] myData() {
    return new Object[][]{
        {"input1", "expected1"},
        {"input2", "expected2"}
    };
}

@Test(dataProvider = "myData")
public void testDataDriven(String input, String expected) {
    // test logic
}
```

---

## TestNG Annotations Reference

| Annotation | Location | Description |
|------------|----------|-------------|
| `@BeforeSuite` | `TestBase` | Runs once before suite (config, dirs) |
| `@AfterSuite` | `TestBase` | Runs once after suite (cleanup) |
| `@BeforeMethod` | `TestBase` / Tests | Runs before each test method |
| `@AfterMethod` | `TestBase` | Runs after each test method (screenshot, quit driver) |
| `@Test` | Test methods | Marks a test method |
| `@Test(groups = {...})` | Test methods | TestNG groups for selective execution |
| `@Test(dataProvider = ...)` | Test methods | Data-driven test |
| `@Parameters` | `TestBase` | Suite/test parameter injection |

---

## FAQ & Troubleshooting

### Q: Tests fail with "Session ID is null"
**A:** Ensure Selenium Grid is running before executing Grid tests. Check `http://localhost:4444/status`.

### Q: How do I skip tests?
```bash
mvn clean test -Dgroups=smoke -DexcludedGroups=flaky
```

### Q: How do I run a single test method?
```bash
mvn clean test -Dtest=LoginTest#testSuccessfulLogin
```

### Q: How do I increase logging?
Edit `src/main/resources/logback.xml` and change `level` to `DEBUG`.

### Q: Screenshots are not being captured
Check `execution.screenshotOnFailure: true` in `application.yaml`.

### Q: Tests are running slowly
- Use headless mode: `-Dexecution.headless=true`
- Increase thread count: `-Dexecution.threadCount=8`
- Use Selenium Grid with multiple nodes

### Q: Port 4444 already in use
Change Grid port in `application.yaml`:
```yaml
grid:
  hubUrl: "http://localhost:4445/wd/hub"
```

### Q: How to add a new browser (e.g., Safari)?
1. Add enum value in `BrowserType.java`
2. Add options in `DriverOptions.java`
3. Add case in `DriverFactory.java`
4. Add test block in `testng.xml`

### Q: Allure report not generating?
```bash
# Ensure allure-results directory exists
mvn clean test
# Then generate report
mvn allure:report
```

---

## 📄 License

Enterprise-grade internal tool. All rights reserved.

---

## 👨‍💻 Author

Built with ❤️ for enterprise test automation.
