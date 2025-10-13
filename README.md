# PrimeSky Website Test Automation Framework

[![Java](https://img.shields.io/badge/Java-18-orange.svg)](https://openjdk.java.net/)
[![Playwright](https://img.shields.io/badge/Playwright-1.44.0-blue.svg)](https://playwright.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15.0-green.svg)](https://cucumber.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9.9-red.svg)](https://maven.apache.org/)

A comprehensive **behavior-driven development (BDD)** test automation framework for the PrimeSky website using **Java 18**, **Playwright**, **Cucumber**, and **Maven**. This framework implements modern web testing practices with robust error handling, cross-browser compatibility, and detailed reporting.

## 🚀 Key Features

- **Modern Tech Stack**: Java 18, Playwright 1.44.0, Cucumber 7.15.0, JUnit 5.10.2
- **BDD Testing**: Human-readable test scenarios using Gherkin syntax
- **Cross-Browser Support**: Chrome, Firefox, Safari, Edge automation
- **Page Object Model**: Maintainable and scalable test architecture
- **Robust Element Handling**: Advanced strategies for hidden and dynamic elements
- **Comprehensive Reporting**: HTML, JSON, and XML test reports
- **CI/CD Ready**: Maven-based build system with parallel execution support
- **Advanced Debugging**: Screenshot capture and detailed logging

## 🏗️ Project Architecture

```
src/test/
├── java/
│   ├── steps/              # Cucumber step definitions
│   │   ├── PrimeSkyWebsiteSteps.java
│   │   └── PrimeSkyAdvancedSteps.java
│   ├── runner/             # Test runners for different test suites
│   │   ├── PrimeSkyTestRunner.java
│   │   └── FlightSearchTestRunner.java
│   ├── pages/              # Page Object Model classes
│   │   ├── BasePage.java
│   │   ├── HomePage.java
│   │   ├── ContactPage.java
│   │   └── FlightSearchPage.java
│   └── exploration/        # Exploratory testing utilities
│       └── WebsiteExplorer.java
└── resources/
    └── features/           # Cucumber feature files
        ├── core_functionality.feature
        ├── user_interactions.feature
        └── primesky_exploration.feature
```

## 🛠️ Technology Stack

| Component | Version | Purpose |
|-----------|---------|----------|
| **Java** | 18 | Primary programming language |
| **Playwright** | 1.44.0 | Modern browser automation framework |
| **Cucumber** | 7.15.0 | BDD testing framework with Gherkin syntax |
| **JUnit** | 5.10.2 | Test execution and assertions |
| **Maven** | 3.9.9 | Build automation and dependency management |

## 📋 Prerequisites

- **Java 18** or higher
- **Maven 3.6+**
- **Internet connection** (for browser downloads and website access)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/hadjioglo/primesky.git
cd primesky
```

### 2. Verify Environment
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

### 3. Build the Project
```bash
# Clean and compile
mvn clean compile

# Compile test classes
mvn test-compile
```

### 4. Install Playwright Browsers (First Time Only)
```bash
# Install required browsers
npx playwright install
```

### 5. Run Tests
```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -Dtest=PrimeSkyTestRunner

# Run tests with specific tags
mvn test -Dcucumber.filter.tags="@smoke"
```

## 🎯 Test Execution Options

### Run Tests by Category
```bash
# Smoke tests (critical functionality)
mvn test -Dcucumber.filter.tags="@smoke"

# Flight search specific tests
mvn test -Dtest=FlightSearchTestRunner

# Form validation tests
mvn test -Dcucumber.filter.tags="@form-validation"

# High priority tests only
mvn test -Dcucumber.filter.tags="@high-priority"
```

### Run Tests with Different Configurations
```bash
# Run in headed mode (visible browser)
mvn test -Dplaywright.headless=false

# Run with specific browser
mvn test -Dplaywright.browser=firefox

# Run with debugging
mvn test -X
```

## 📊 Test Reports

After test execution, reports are generated in the `target/` directory:

- **HTML Report**: `target/cucumber-reports.html` - Interactive test results
- **JSON Report**: `target/cucumber-reports.json` - Machine-readable results
- **XML Report**: `target/cucumber-reports.xml` - JUnit compatible format
- **Screenshots**: `target/screenshots/` - Failure screenshots for debugging

## 🧪 Available Test Scenarios

### Core Functionality
- ✅ Homepage loading verification
- ✅ Navigation functionality testing
- ✅ Page title and critical elements validation
- ✅ JavaScript error detection

### User Interactions
- ✅ Contact form validation
- ✅ Form submission testing
- ✅ Flight search functionality
- ✅ Date picker interactions
- ✅ Multi-step form workflows

### Flight Search Features
- ✅ Round-trip flight searches
- ✅ One-way flight searches
- ✅ Form validation scenarios
- ✅ Search result verification

## 🔧 Configuration

### Browser Configuration
Tests run in **headless Chrome** by default. Modify in `PrimeSkyWebsiteSteps.java`:

```java
browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
    .setHeadless(false)  // Set to false for visible browser
    .setTimeout(60000));
```

### Base URL Configuration
The target website URL is configured in `PrimeSkyWebsiteSteps.java`:

```java
private static final String BASE_URL = "https://fdev.primesky.com/";
```

## 🐛 Debugging and Troubleshooting

### Common Issues and Solutions

1. **Browser Installation Issues**
   ```bash
   npx playwright install
   ```

2. **Element Not Found Errors**
   - Check browser dev tools for selector changes
   - Use multiple selector strategies implemented in the framework
   - Enable screenshots on failure for visual debugging

3. **Test Timeouts**
   - Increase timeout values in `PrimeSkyWebsiteSteps.java`
   - Check network connectivity to target website

### Debug Mode
```bash
# Run with verbose output
mvn test -X

# Run specific failing test
mvn test -Dtest=PrimeSkyTestRunner -X
```

## 📈 Advanced Features

### Hidden Element Handling
The framework includes advanced strategies for interacting with hidden form elements:

```java
// Automatic detection and JavaScript execution for hidden elements
if (element.getAttribute("hidden") != null || !element.isVisible()) {
    String jsScript = "arguments[0].value = '" + value + "'; arguments[0].dispatchEvent(new Event('change'));";
    element.evaluate(jsScript);
}
```

### Multiple Selector Strategies
```java
// Robust element detection with fallback selectors
String[] selectors = {
    "[data-testid='" + fieldName + "']",
    "input[name*='" + fieldName + "']",
    "input[id*='" + fieldName + "']"
};
```

### Screenshot Capture
Automatic screenshot capture on test failures for debugging:

```java
page.screenshot(new Page.ScreenshotOptions()
    .setPath(Paths.get("target/screenshots/failure.png")));
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow the coding standards in `.github/copilot-instructions.md`
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

## 📝 Writing New Tests

### 1. Create Feature File
```gherkin
@new-feature @high-priority
Feature: New functionality testing
  As a user
  I want to test new feature
  So that I can ensure it works correctly

  Scenario: Test new feature
    Given I am on the homepage
    When I interact with new feature
    Then the expected result should be displayed
```

### 2. Implement Step Definitions
```java
@When("I interact with new feature")
public void i_interact_with_new_feature() {
    // Implementation with error handling
}
```

### 3. Run and Debug
```bash
mvn test -Dcucumber.filter.tags="@new-feature"
```

## 📚 Documentation

- [Playwright Java Documentation](https://playwright.dev/java/)
- [Cucumber Documentation](https://cucumber.io/docs/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Maven Documentation](https://maven.apache.org/guides/)

## 🎯 Best Practices Implemented

- ✅ **Page Object Model** for maintainable test code
- ✅ **Explicit waits** instead of fixed sleeps
- ✅ **Multiple selector strategies** for robust element detection
- ✅ **Comprehensive error handling** with meaningful messages
- ✅ **Screenshot capture** on failures for debugging
- ✅ **BDD scenarios** in business-readable language
- ✅ **Tag-based test execution** for flexible test runs
- ✅ **Detailed logging** for troubleshooting

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏢 About PrimeSky

This test automation framework is designed specifically for testing the PrimeSky flight booking website, focusing on:
- Flight search functionality
- Contact form interactions
- User journey validation
- Cross-browser compatibility
- Performance monitoring

---

**Last Updated**: October 2025  
**Framework Version**: 1.0  
**Maintained by**: Alexandr Hadjioglo
