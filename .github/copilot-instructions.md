# Copilot Instructions for Playwright-Cucumber-Java Testing Project

## Project Overview
This is a **comprehensive test automation framework** using **Java 18**, **Playwright 1.44.0**, **Cucumber 7.15.0**, and **Maven** for end-to-end web application testing. The project focuses on **behavior-driven development (BDD)** and **robust web automation** for the PrimeSky website testing.

### Key Technologies
- **Java 18** - Primary programming language
- **Playwright 1.44.0** - Modern browser automation framework
- **Cucumber 7.15.0** - BDD testing framework with Gherkin syntax
- **JUnit 5.10.2** - Test execution and assertions
- **Maven 3.x** - Build automation and dependency management

### Project Goals
- Create maintainable, readable test automation scripts
- Implement BDD practices for business-readable test scenarios
- Handle complex web UI interactions including hidden elements
- Provide comprehensive test reporting and debugging capabilities
- Ensure cross-browser compatibility and robust error handling

## Coding Standards and Conventions

### Java Code Standards
- **Package Structure**: Follow Maven standard structure (`src/test/java/{package}`)
- **Class Naming**: 
  - Step definitions: `{FeatureName}Steps.java` (e.g., `PrimeSkyWebsiteSteps.java`)
  - Test runners: `{TestType}TestRunner.java` (e.g., `FlightSearchTestRunner.java`)
  - Page objects: `{PageName}Page.java` (e.g., `ContactFormPage.java`)
- **Method Naming**: Use descriptive camelCase names reflecting the action
- **Constants**: Use UPPER_SNAKE_CASE for static final variables
- **Indentation**: 4 spaces (no tabs)

### Cucumber Conventions
- **Feature Files**: Use descriptive names ending in `.feature`
- **Scenario Naming**: Start with action verbs and be descriptive
- **Step Definitions**: Use present tense and active voice
- **Tags**: Use `@{category}` for grouping (e.g., `@flight-search`, `@high-priority`)

### Playwright Best Practices
- **Selectors**: Prefer stable selectors (data-testid > id > class > text)
- **Waits**: Always use explicit waits instead of fixed sleeps
- **Error Handling**: Implement try-catch blocks with meaningful error messages
- **Screenshots**: Capture screenshots on failures for debugging

## File Organization and Structure

### Directory Structure
```
src/test/
├── java/
│   ├── steps/           # Cucumber step definitions
│   ├── runner/          # Test runners for different test suites
│   ├── pages/           # Page Object Model classes (if used)
│   └── exploration/     # Exploratory testing utilities
└── resources/
    ├── features/        # Cucumber feature files
    └── {test-data}/     # Test data files (JSON, CSV, etc.)
```

### File Naming Conventions
- **Feature files**: `{functionality}.feature` (e.g., `user_interactions.feature`)
- **Step definitions**: `{FeatureName}Steps.java`
- **Test runners**: `{TestSuite}TestRunner.java`
- **Configuration**: Use descriptive names for test data and config files

## Testing Guidelines and Patterns

### BDD Scenario Writing
- **Given**: Set up the initial state or context
- **When**: Describe the action being performed
- **Then**: Verify the expected outcome
- **And/But**: Chain multiple conditions or actions

### Cucumber Step Definition Patterns
```java
@Given("I am on the {string} page")
public void i_am_on_the_page(String pageName) {
    // Implementation
}

@When("I fill in the {string} field with {string}")
public void i_fill_in_the_field_with(String fieldName, String value) {
    // Implementation with error handling
}

@Then("I should see {string}")
public void i_should_see(String expectedText) {
    // Assertion with proper wait conditions
}
```

### Playwright Implementation Patterns

#### Element Interaction Best Practices
```java
// ✅ Good: Handle hidden elements
private void fillField(String selector, String value) {
    Locator element = page.locator(selector);
    if (element.getAttribute("hidden") != null || !element.isVisible()) {
        // Use JavaScript for hidden elements
        String jsScript = "arguments[0].value = '" + value + "'; arguments[0].dispatchEvent(new Event('change'));";
        element.evaluate(jsScript);
    } else {
        element.fill(value);
    }
}

// ✅ Good: Multiple selector strategies
String[] selectors = {
    "[data-testid='" + fieldName + "']",
    "input[name*='" + fieldName + "']",
    "input[id*='" + fieldName + "']"
};
```

#### Error Handling Pattern
```java
try {
    // Attempt primary action
    element.click();
    System.out.println("Successfully interacted with element");
} catch (PlaywrightException e) {
    System.out.println("Failed to interact: " + e.getMessage());
    // Implement fallback strategy or graceful degradation
}
```

### Test Data Management
- Use **DataTable** for structured test data in scenarios
- Create **parameterized scenarios** for testing multiple data sets
- Store complex test data in **JSON or CSV files** in `src/test/resources`

## Framework-Specific Instructions

### Playwright Integration
- **Browser Management**: Use singleton pattern for browser instances
- **Page Management**: Create page instances in step definitions or use dependency injection
- **Waits and Timing**: 
  - Use `page.waitForLoadState()` for page loads
  - Use `element.waitFor()` for element states
  - Avoid `Thread.sleep()` - use `page.waitForTimeout()` sparingly

### Cucumber Configuration
- **Test Runners**: Use `@CucumberOptions` for configuration
- **Tags**: Implement tag-based test execution (`@RunWith`, `@IncludeEngines`)
- **Hooks**: Use `@Before` and `@After` for setup/teardown
- **Reporting**: Configure multiple report formats (HTML, JSON, JUnit XML)

### Maven Configuration
- **Dependencies**: Keep versions aligned and up-to-date
- **Plugins**: Configure Surefire plugin for test execution
- **Profiles**: Create profiles for different test environments

## Common Patterns and Solutions

### Hidden Element Handling
```java
// For hidden form fields (common pattern in this project)
String hiddenAttr = element.getAttribute("hidden");
boolean isHidden = hiddenAttr != null || !element.isVisible();
if (isHidden) {
    String jsScript = "arguments[0].value = '" + value + "'; arguments[0].dispatchEvent(new Event('change'));";
    element.evaluate(jsScript);
}
```

### Flexible Element Detection
```java
// Multiple selector strategy for robust element detection
String[] selectors = getFieldSelectors(fieldType);
for (String selector : selectors) {
    if (page.locator(selector).count() > 0) {
        // Found element, proceed with interaction
        break;
    }
}
```

### Debugging and Logging
```java
// Enhanced logging for troubleshooting
System.out.println("Attempting to fill " + fieldType + " with: " + value);
System.out.println("Found " + elements.count() + " elements with selector: " + selector);

// Screenshot capture for debugging
page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("target/screenshots/debug.png")));
```

## Test Execution and Debugging

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -Dtest=FlightSearchTestRunner

# Run tests with specific tags
mvn test -Dcucumber.filter.tags="@flight-search and @high-priority"
```

### Debugging Tips
1. **Enable Debug Logging**: Add detailed `System.out.println()` statements
2. **Screenshot Capture**: Take screenshots at key points for visual debugging
3. **Element Inspection**: Use browser dev tools to verify selectors
4. **Wait Strategies**: Ensure proper wait conditions are in place
5. **Error Messages**: Provide meaningful error messages in assertions

## Code Generation Guidelines

### When Creating New Tests
1. **Start with Feature File**: Write readable BDD scenarios first
2. **Generate Step Definitions**: Create corresponding step definitions with proper error handling
3. **Implement Robust Selectors**: Use multiple selector strategies
4. **Add Comprehensive Logging**: Include debugging information
5. **Handle Edge Cases**: Consider hidden elements, slow loading, dynamic content

### When Fixing Test Failures
1. **Analyze the Error**: Understand the root cause (element not found, timeout, etc.)
2. **Implement JavaScript Fallbacks**: For hidden or complex UI elements
3. **Add Multiple Selector Strategies**: Increase element detection reliability
4. **Enhance Error Handling**: Provide graceful degradation
5. **Update Documentation**: Document any special handling requirements

## Project-Specific Notes

### PrimeSky Website Testing
- **Hidden Form Fields**: Many form elements use `hidden` attribute requiring JavaScript interaction
- **Dynamic Content**: Website uses modern UI patterns that may require special handling
- **Flight Search**: Complex form interactions with date pickers and multi-step processes

### Known Issues and Solutions
- **TimeoutError on Hidden Elements**: Use JavaScript execution for hidden form fields
- **Date Field Interactions**: Implement special handling for date inputs that may be hidden
- **Form Submission**: Multiple button detection strategies due to varying implementations

Remember: Always prioritize **maintainability**, **readability**, and **robust error handling** when creating or modifying test code.
