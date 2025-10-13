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
```
src/test/
├── java/
│   ├── steps/           # Cucumber step definitions
│   │   ├── PrimeSkyWebsiteSteps.java
│   │   └── PrimeSkyAdvancedSteps.java
│   ├── runner/          # Test runners for different test suites
│   │   ├── PrimeSkyTestRunner.java
│   │   └── FlightSearchTestRunner.java
│   ├── pages/           # Page Object Model classes
│   │   ├── BasePage.java
│   │   ├── HomePage.java
│   │   ├── ContactPage.java
│   │   └── FlightSearchPage.java
│   └── exploration/     # Exploratory testing utilities
│       └── WebsiteExplorer.java
└── resources/
    └── features/        # Cucumber feature files
        ├── core_functionality.feature
        ├── user_interactions.feature
        └── primesky_exploration.feature
```

### File Naming Conventions
- **Feature files**: `{functionality}.feature` (e.g., `user_interactions.feature`)
- **Step definitions**: `{FeatureName}Steps.java` (e.g., `PrimeSkyWebsiteSteps.java`)
- **Test runners**: `{TestSuite}TestRunner.java` (e.g., `FlightSearchTestRunner.java`)
- **Page objects**: `{PageName}Page.java` (e.g., `ContactPage.java`)
- **Constants**: Use UPPER_SNAKE_CASE for static final variables

## 💻 Coding Standards and Best Practices

### Java Code Standards
- **Package Structure**: Follow Maven standard structure (`src/test/java/{package}`)
- **Class Naming**: 
  - Step definitions: `{FeatureName}Steps.java`
  - Test runners: `{TestType}TestRunner.java`
  - Page objects: `{PageName}Page.java`
- **Method Naming**: Use descriptive camelCase names reflecting the action
- **Indentation**: 4 spaces (no tabs)
- **Error Handling**: Always implement try-catch blocks with meaningful error messages
- **Logging**: Use `System.out.println()` for debugging and status messages

### Cucumber BDD Conventions
- **Feature Files**: Use descriptive names and clear business language
- **Scenario Naming**: Start with action verbs and be descriptive
- **Step Definitions**: Use present tense and active voice
- **Tags**: Use `@{category}` for grouping (e.g., `@flight-search`, `@high-priority`)
- **Given-When-Then**: Follow BDD structure strictly

### Playwright Implementation Standards
- **Selectors**: Prefer stable selectors (data-testid > id > class > text)
- **Waits**: Always use explicit waits instead of fixed sleeps
- **Screenshots**: Capture screenshots on failures for debugging
- **Browser Management**: Use singleton pattern for browser instances

## 🛠️ Framework-Specific Implementation Patterns

### Cucumber Step Definition Patterns
```java
@Given("User on the {string} page")
public void userOnThePage(String pageName) {
    System.out.println("Navigating to " + pageName + " page");
    // Implementation with error handling
    try {
        // Navigation logic
        System.out.println("Successfully navigated to " + pageName);
    } catch (Exception e) {
        System.out.println("Failed to navigate: " + e.getMessage());
        throw e;
    }
}

@When("User fill in the {string} field with {string}")
public void userFillsInTheFieldWith(String fieldName, String value) {
    System.out.println("Filling " + fieldName + " with: " + value);
    // Implementation with multiple selector strategies
}

@Then("User should see {string}")
public void userShouldSee(String expectedText) {
    // Assertion with proper wait conditions
    Assertions.assertTrue(condition, "Expected to see: " + expectedText);
}
```

### Page Object Model Pattern
```java
public class HomePage extends BasePage {
    private static final String HOME_URL = "/";
    
    // Locators
    private final String MAIN_CONTENT = "main, .main-content, .container";
    private final String NAVIGATION_MENU = "nav, .nav, .menu";
    
    public HomePage(Page page) {
        super(page);
    }
    
    public void open() {
        System.out.println("Opening homepage");
        navigateTo(HOME_URL);
        waitForPageLoad();
    }
    
    public boolean isOnHomePage() {
        return isElementVisible(MAIN_CONTENT) && 
               page.url().contains("primesky.com");
    }
}
```

### Advanced Element Interaction Patterns

#### Hidden Element Handling (Critical for PrimeSky)
```java
private void fillField(String selector, String value) {
    System.out.println("Attempting to fill field with selector: " + selector);
    Locator element = page.locator(selector);
    
    try {
        // Check if element is hidden
        if (element.getAttribute("hidden") != null || !element.isVisible()) {
            System.out.println("Element is hidden, using JavaScript execution");
            String jsScript = "arguments[0].value = '" + value + "'; " +
                            "arguments[0].dispatchEvent(new Event('change'));";
            element.evaluate(jsScript);
        } else {
            System.out.println("Element is visible, using standard fill");
            element.fill(value);
        }
        System.out.println("Successfully filled field with: " + value);
    } catch (PlaywrightException e) {
        System.out.println("Failed to fill field: " + e.getMessage());
        throw e;
    }
}
```

#### Multiple Selector Strategy (Robust Element Detection)
```java
private Locator findElementWithFallback(String fieldType) {
    String[] selectors = {
        "[data-testid='" + fieldType + "']",
        "input[name*='" + fieldType + "']",
        "input[id*='" + fieldType + "']",
        "input[placeholder*='" + fieldType + "']"
    };
    
    for (String selector : selectors) {
        System.out.println("Trying selector: " + selector);
        Locator elements = page.locator(selector);
        if (elements.count() > 0) {
            System.out.println("Found element with selector: " + selector);
            return elements.first();
        }
    }
    
    throw new RuntimeException("Could not find element for: " + fieldType);
}
```

#### Comprehensive Error Handling Pattern
```java
try {
    // Primary action
    element.click();
    System.out.println("Successfully clicked element");
} catch (PlaywrightException e) {
    System.out.println("Primary click failed: " + e.getMessage());
    
    // Fallback strategy
    try {
        element.evaluate("arguments[0].click()");
        System.out.println("JavaScript click successful");
    } catch (Exception fallbackError) {
        System.out.println("All click strategies failed");
        // Capture screenshot for debugging
        captureDebugScreenshot("click_failure");
        throw new RuntimeException("Could not click element: " + e.getMessage());
    }
}
```

### Test Data Management Patterns
```java
// Using DataTable for structured test data
@When("all required fields are filled with valid data")
public void fill_required_fields(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : data) {
        String field = row.get("Field");
        String value = row.get("Value");
        fillFieldWithStrategy(field, value);
    }
}
```

## 🧪 Testing Guidelines and Patterns

### BDD Scenario Writing Standards
```gherkin
# Good scenario structure
@flight-search @high-priority
Scenario: Search for flights with valid criteria
  Given the flight search page is open
  When the flight search form is filled with valid details
    | Field           | Value       |
    | Origin          | New York    |
    | Destination     | Los Angeles |
    | Departure Date  | 2025-12-15  |
    | Return Date     | 2025-12-22  |
    | Passengers      | 2           |
    | Trip Type       | Round Trip  |
  And the flight search is submitted
  Then a list of available flights should be displayed
  And the search results should contain flights from "New York" to "Los Angeles"
  And the results should show the correct departure date
```

### Test Execution Patterns
```java
// Test Runner Configuration
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = 
    "pretty," +
    "html:target/cucumber-reports.html," +
    "json:target/cucumber-reports.json," +
    "junit:target/cucumber-reports.xml"
)
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps")
public class PrimeSkyTestRunner {
    // Annotations do all the work
}
```

### Debugging and Logging Patterns
```java
// Enhanced logging for troubleshooting
System.out.println("=== Starting " + scenarioName + " ===");
System.out.println("Attempting to fill " + fieldType + " with: " + value);
System.out.println("Found " + elements.count() + " elements with selector: " + selector);
System.out.println("Current page URL: " + page.url());
System.out.println("Page title: " + page.title());

// Screenshot capture for debugging
private void captureDebugScreenshot(String context) {
    String timestamp = String.valueOf(System.currentTimeMillis());
    String filename = String.format("debug_%s_%s.png", context, timestamp);
    Path screenshotPath = Paths.get("target/screenshots/" + filename);
    page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
    System.out.println("Debug screenshot saved: " + filename);
}
```

## 🎯 Project-Specific Considerations

### PrimeSky Website Characteristics
- **Hidden Form Fields**: Many form elements use `hidden` attribute requiring JavaScript interaction
- **Dynamic Content**: Website uses modern UI patterns that may require special handling
- **Flight Search**: Complex form interactions with date pickers and multi-step processes
- **Contact Forms**: Multiple validation scenarios and error message handling
- **Navigation**: Dynamic menu systems with hover effects

### Known Issues and Solutions
- **TimeoutError on Hidden Elements**: Use JavaScript execution for hidden form fields
- **Date Field Interactions**: Implement special handling for date inputs that may be hidden
- **Form Submission**: Multiple button detection strategies due to varying implementations
- **Selector Specificity**: Use multiple fallback selectors for robust element detection

### Test Environment Configuration
```java
// Base configuration for PrimeSky testing
private static final String BASE_URL = "https://fdev.primesky.com/";
private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds
private static final boolean HEADLESS_MODE = true; // Set to false for debugging

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
2. **Implement JavaScript Fallbacks**: For hidden or complex UI elements
3. **Add Multiple Selector Strategies**: Increase element detection reliability
4. **Enhance Error Handling**: Provide graceful degradation
5. **Update Documentation**: Document any special handling requirements

### Code Generation Guidelines
When generating new code, always include:
- Comprehensive error handling with try-catch blocks
- Detailed logging statements for debugging
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
