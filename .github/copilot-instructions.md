# GitHub Copilot Instructions for PrimeSky Test Automation Framework

## 🎯 Project Overview

This is a **comprehensive test automation framework** using **Java 18**, **Playwright 1.44.0**, **Cucumber 7.15.0**, and **Maven** for end-to-end web application testing of the PrimeSky website. The project focuses on **behavior-driven development (BDD)** and **robust web automation** with advanced error handling and cross-browser support.

### Key Technologies & Versions

- **Java 18** - Primary programming language (verified working)
- **Playwright 1.44.0** - Modern browser automation framework
- **Cucumber 7.15.0** - BDD testing framework with Gherkin syntax
- **JUnit 5.10.2** - Test execution and assertions
- **Maven 3.9.9** - Build automation and dependency management

### Project Goals & Focus Areas

- Create maintainable, readable test automation scripts
- Implement BDD practices for business-readable test scenarios
- Handle complex web UI interactions including hidden elements
- Provide comprehensive test reporting and debugging capabilities
- Ensure cross-browser compatibility and robust error handling
- Test PrimeSky website functionality (flight search, contact forms, navigation)

## 📁 File Organization and Architecture

### Directory Structure

### File Naming Conventions

-

## 💻 Coding Standards and Best Practices

### Java Code Standards

- **Package Structure**: Follow Maven standard structure (`src/test/java/{package}`)
- **Class Naming**:

  - Step definitions: `{FeatureName}Steps.java`
  - Test runners: `{TestType}TestRunner.java`

- **Method Naming**: Use descriptive camelCase names reflecting the action
- **Indentation**: 4 spaces (no tabs)

### Cucumber BDD Conventions

- **Feature Files**: Use descriptive names and clear business language
- **Scenario Naming**: Start with action verbs and be descriptive
- **Step Definitions**: Use Passive Voice without "I" or "we". ex: "User is on the login page", "And page title is displayed"
- **Tags**: Use `@{category}` for grouping (e.g., `@flight-search`, `@high-priority`)
- **Given-When-Then**: Follow BDD structure strictly

### Playwright Implementation Standards

- **Selectors**: Prefer stable selectors (data-testid > id > class > text)
- **Waits**: Always use explicit waits instead of fixed sleeps
- **Screenshots**: Capture screenshots on failures for debugging
- **Browser Management**: Use a dedicated `BrowserManager` singleton class for Playwright browser lifecycle management. All browser, context, and page instances must be managed through this class for modularity and maintainability.
- **Page Interactions**: Use Page Object Model (POM) for all page interactions. Each web page should have a dedicated class (e.g., `HomePage`) encapsulating navigation, assertions, and UI actions. Service classes (e.g., `PlaywrightService`) must delegate page-specific logic to these page objects, ensuring maintainability and separation of concerns.

#### Page Object Model Example

```java
// HomePage.java
public class HomePage {
  private final Page page;
  public HomePage(Page page) { this.page = page; }
  public void load() { /* navigation logic */ }
  public void assertLoaded() { /* assertion logic */ }
}

// PlaywrightService.java
private static final HomePage homePage = new HomePage(page);
public static void loadPrimeSkyHomepage() {
  homePage.load();
  homePage.assertLoaded();
}
```

**Always use page objects for all page-specific actions. Never mix navigation, waits, or assertions directly in service or step definition classes.**

# Error Handling Standard for Playwright Actions

## Generic Error Handling Utility

All Playwright actions in page objects and service classes **must use the ErrorHandlingUtil utility** for error handling, logging, and screenshot capture. This removes repetitive try-catch blocks and ensures:
- Consistent error handling
- Automatic screenshot capture on failure
- Comprehensive logging
- Maintainable and readable code

### Usage Pattern

Wrap Playwright actions in:
```java
ErrorHandlingUtil.runWithErrorHandling(() -> {
    // Playwright actions here
}, "logs/<context>_failure.png", logger, page, "<Error message>");
```

### Example
```java
public void searchForFlight(...) {
    ErrorHandlingUtil.runWithErrorHandling(() -> {
        // Playwright actions
    }, "logs/flight_search_failure.png", logger, page, "Flight search failed");
}
```

**Apply this pattern everywhere Playwright actions are performed.**

## 🛠️ Framework-Specific Implementation Patterns

## 🧪 Testing Guidelines and Patterns

### BDD Scenario Writing Standards

```gherkin
# Good scenario structure
@flight-search @high-priority
Scenario: Search for flights with valid criteria
Given PrimeSky homepage is loaded
When user searches for the flight
| from   | to       | departure date | return date | passengers | class |
| London | New-York | 2026-02-15     | 2026-02-25  | 1          | E     |
Then flight results are displayed
And the search results should contain flights from "New York" to "Los Angeles"
And the results should show the correct departure date
```

### Test Execution Patterns

```java
// Test Runner Configuration
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        tags = "@Run and not @Manual",
        plugin = {},
        stepNotifications = true
)
public class RunnerTest {
}
```

### Debugging and Logging Patterns

## 🎯 Project-Specific Considerations

### PrimeSky Website Characteristics

### Known Issues and Solutions

### Test Environment Configuration

```java
// Base configuration for PrimeSky testing
private static final String BASE_URL = "https://fdev.primesky.com/";
private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds
private static final boolean HEADLESS_MODE = false; // Set to false for debugging

// Browser setup with PrimeSky optimizations
browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
    .setHeadless(HEADLESS_MODE)
    .setTimeout(60000));

context = browser.newContext(new Browser.NewContextOptions()
    .setViewportSize(1920, 1080));
```

## 🚀 Development Workflow Guidelines

### When Creating New Tests

1. **Start with Feature File**: Write readable BDD scenarios first
2. **Generate Step Definitions**: Create corresponding step definitions with proper error handling
3. **Implement Robust Selectors**: Use multiple selector strategies
4. **Add Comprehensive Logging**: Include debugging information
5. **Handle Edge Cases**: Consider hidden elements, slow loading, dynamic content
6. **Test Cross-Browser**: Verify functionality across different browsers

### When Fixing Test Failures

1. **Analyze the Error**: Understand the root cause (element not found, timeout, etc.)
2. **Add Multiple Selector Strategies**: Increase element detection reliability
3. **Enhance Error Handling**: Provide graceful degradation
4. **Update Documentation**: Document any special handling requirements

### Code Generation Guidelines

When generating new code, always include:

- Multiple selector strategies for element detection
- Screenshot capture on failures
- Meaningful assertion messages
- Proper wait conditions instead of fixed sleeps

### Maven Commands for Development

```bash
# Build and test
mvn clean compile test-compile

# Run specific test suite
mvn test -Dtest=PrimeSkyTestRunner

# Run with tags
mvn test -Dcucumber.filter.tags="@smoke"

# Run with debugging
mvn test -X

# Generate reports only
mvn test -DskipTests=false
```

## 📝 Code Quality Standards

### Required Elements in Every Test Method

1. **Clear method documentation**
2. **Input parameter validation**
3. **Comprehensive error handling**
4. **Detailed logging statements**
5. **Proper wait conditions**
6. **Meaningful assertions**
7. **Screenshot capture on failure**

### Forbidden Practices

- ❌ Never use `Thread.sleep()` - use explicit waits
- ❌ Never use hardcoded selectors without fallbacks
- ❌ Never skip error handling in step definitions
- ❌ Never commit tests without proper logging
- ❌ Never use brittle selectors (index-based, xpath with position)

## 🎉 Success Criteria

Every piece of generated code should:

- ✅ Follow the established patterns and conventions
- ✅ Include comprehensive error handling
- ✅ Provide detailed logging for debugging
- ✅ Use multiple selector strategies for robustness
- ✅ Be maintainable and readable
- ✅ Handle PrimeSky-specific UI patterns
- ✅ Include proper BDD scenario structure
- ✅ Support cross-browser testing

---

**Remember**: Always prioritize **maintainability**, **readability**, and **robust error handling** when creating or modifying test code. The framework is designed to handle complex web applications like PrimeSky with advanced UI patterns and hidden elements.

### Step Definition Best Practice

**Step definitions must only delegate actions to service or page object classes.**
Never perform browser/page setup, navigation, waits, assertions, or error handling directly in step definitions. Instead, call a method in a service class (e.g., `PlaywrightService.loadPrimeSkyHomepage()`) that encapsulates all logic. This ensures maintainability, separation of concerns, and clean BDD code.

**Example:**

```java
@Given("PrimeSky homepage is loaded")
public void primeSkyHomepageIsLoaded() {
  PlaywrightService.loadPrimeSkyHomepage();
}
```

All browser/page logic (navigation, waits, assertions, logging, error handling, screenshots) must be implemented in the service class method.

# Framework Improvement Recommendations

1. **Expand Page Object Coverage**  
   - Create dedicated page object classes for all major pages (e.g., SearchResultsPage, ContactPage) to improve modularity and test clarity.

2. **Strict Service Layer Delegation**  
   - Ensure all step definitions delegate actions to service or page object classes. No direct Playwright or assertion logic should be present in step files.

3. **Multiple Selector Strategies**  
   - All element locators in page objects must use multiple strategies (data-testid, id, class, text) and fallback logic for robustness.

4. **Screenshot on Failure**  
   - Confirm that PlaywrightService or test hooks capture screenshots on every failure and attach them to reports.

5. **Assertion Messages**  
   - All assertions should have meaningful, descriptive messages for easier debugging.

6. **Comprehensive Logging**  
   - Every major action (navigation, click, assertion, error) must be logged with context (e.g., which page, which element, what data).

7. **Input Validation**  
   - All public methods in page objects and services should validate input parameters and handle null/invalid values gracefully.

8. **Error Handling**  
   - Service and page object methods should catch exceptions, log errors, and take screenshots before rethrowing or handling gracefully.

9. **Test Data Management**  
   - Consider a strategy for managing test data (e.g., using data tables, external files, or a test data factory).

10. **Cross-Browser Testing**  
    - Ensure BrowserManager supports launching different browsers (Chromium, Firefox, WebKit) and that tests are run across all supported browsers.

11. **Advanced Reporting**  
    - Integrate advanced reporting (e.g., Allure, ExtentReports) for better visibility of test results, logs, and screenshots.

12. **Code Duplication**  
    - Review for any duplicated logic in page objects or services and refactor into utility/helper classes if needed.

13. **Edge Case Handling**  
    - Ensure your framework handles dynamic content, hidden elements, and slow-loading pages robustly.

14. **Documentation**  
    - Keep your README and framework documentation up to date, especially regarding setup, running tests, and troubleshooting.

# Playwright Wait Strategy

- **Default Timeout Usage**: Always use Playwright's default timeout for `waitForSelector` and other wait operations unless a specific scenario requires a custom timeout. This leverages Playwright's built-in waiting logic and avoids unnecessary hardcoding.
- **Custom Timeout**: Only override the default timeout for elements known to be slow or flaky, and document the reason for the override.
- **Maintainability**: Avoid using repeated hardcoded timeout values (e.g., `new Page.WaitForSelectorOptions().setTimeout(10000)`) throughout the codebase. If a custom timeout is needed, use a named constant or parameterize the timeout for flexibility.
- **Code Example**:

```java
// Use default timeout
page.waitForSelector(searchButtonSelector);

// If custom timeout is needed, use a named constant or parameter
private static final int SLOW_ELEMENT_TIMEOUT_MS = 20000;
page.waitForSelector(slowElementSelector, new Page.WaitForSelectorOptions().setTimeout(SLOW_ELEMENT_TIMEOUT_MS));
```

- **Rationale**: This approach improves maintainability, reduces duplication, and ensures context-aware waiting. Timeout changes can be made centrally, and Playwright's auto-waiting features are fully utilized.
