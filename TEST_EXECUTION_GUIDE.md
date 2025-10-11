# PrimeSky Website Test Execution Guide

## Overview
This guide provides instructions for executing the comprehensive test suite developed for the PrimeSky website (https://fdev.primesky.com/).

## Test Suite Structure

### Feature Files
- `core_functionality.feature` - Core website functionality tests
- `user_interactions.feature` - User interaction and form testing

### Step Definitions
- `PrimeSkyWebsiteSteps.java` - Comprehensive step definitions implementing all test scenarios

### Test Runners
- `PrimeSkyTestRunner.java` - JUnit Platform-based test runner with comprehensive reporting

## Prerequisites

### Software Requirements
- Java 18 or higher
- Maven 3.6 or higher
- Internet connection for accessing the target website

### Dependencies
All dependencies are managed through Maven:
- Playwright for Java (1.44.0)
- Cucumber JVM (7.15.0)
- JUnit Jupiter (5.10.2)

## Test Execution

### 1. Compile the Project
```bash
mvn clean compile test-compile
```

### 2. Install Playwright Browsers (First time only)
```bash
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
```

### 3. Run All Tests
```bash
mvn test
```

### 4. Run Specific Test Tags
```bash
# Run only smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run only high priority tests
mvn test -Dcucumber.filter.tags="@high-priority"

# Run navigation tests
mvn test -Dcucumber.filter.tags="@navigation"

# Run form tests
mvn test -Dcucumber.filter.tags="@forms"
```

### 5. Run Website Explorer
```bash
mvn exec:java
```

## Test Reports

### Generated Reports
After test execution, reports are generated in:
- `target/cucumber-reports.html` - HTML report with detailed results
- `target/cucumber-reports.json` - JSON report for CI/CD integration
- `target/cucumber-reports.xml` - JUnit XML report
- `target/surefire-reports/` - Maven Surefire test reports

### Screenshots
Website exploration screenshots are saved in:
- `target/screenshots/` - Contains exploration screenshots

## Test Categories

### 1. Core Functionality Tests (`@smoke @critical-path`)
- **Homepage Loading**: Verifies successful page load and basic elements
- **Navigation Testing**: Tests main navigation functionality
- **Responsive Design**: Validates layout across different viewport sizes
- **Performance**: Checks page load times and interactivity
- **Accessibility**: Basic accessibility compliance checks

### 2. User Interaction Tests (`@user-interactions @forms`)
- **Form Validation**: Tests client-side and server-side validation
- **Form Submission**: Verifies successful form submission workflows
- **Search Functionality**: Tests search features (if available)
- **Interactive Elements**: Validates dropdowns, buttons, and hover effects
- **Error Handling**: Tests error scenarios and user feedback

## Test Configuration

### Browser Configuration
- Default browser: Chromium (headless mode for CI/CD)
- Viewport: 1920x1080 (configurable)
- Timeout: 30 seconds (configurable)

### Test Data
Test data is embedded in feature files using:
- Scenario Outlines for multiple data sets
- Data Tables for complex form data
- Examples tables for parameterized testing

## Debugging and Troubleshooting

### Common Issues

1. **Browser Installation Issues**
   - Run: `mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"`
   - Ensure sufficient disk space and internet connectivity

2. **Test Timeout Issues**
   - Increase timeout values in `PrimeSkyWebsiteSteps.java`
   - Check network connectivity to target website

3. **Element Not Found Issues**
   - Review website structure for changes
   - Update locators in step definitions
   - Run website explorer to identify new elements

### Debug Mode
For debugging tests with visible browser:
1. Edit `PrimeSkyWebsiteSteps.java`
2. Change `setHeadless(true)` to `setHeadless(false)`
3. Add `setSlowMo(1000)` for slower execution

### Verbose Logging
Enable detailed logging by adding to JVM arguments:
```bash
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG
```

## Continuous Integration

### Jenkins/GitHub Actions Integration
```yaml
- name: Run Tests
  run: |
    mvn clean compile test-compile
    mvn test
    
- name: Publish Test Results
  uses: dorny/test-reporter@v1
  if: success() || failure()
  with:
    name: Cucumber Tests
    path: target/cucumber-reports.xml
    reporter: java-junit
```

### Docker Support
To run tests in Docker:
```dockerfile
FROM mcr.microsoft.com/playwright/java:v1.44.0-jammy
COPY . /app
WORKDIR /app
RUN mvn clean compile test-compile
CMD ["mvn", "test"]
```

## Extending the Test Suite

### Adding New Tests
1. Add scenarios to existing feature files or create new ones
2. Implement missing step definitions in `PrimeSkyWebsiteSteps.java`
3. Add appropriate tags for categorization
4. Update test runner configuration if needed

### Custom Locators
Update locator strategies in step definitions:
- Use data attributes for test-specific elements
- Implement page object pattern for complex pages
- Add fallback locators for robustness

### Performance Testing
For load testing integration:
- Use JMeter or Gatling with Cucumber scenarios
- Implement performance assertions in step definitions
- Add monitoring and alerting for performance regressions

## Best Practices

### Test Maintenance
- Regularly update locators based on UI changes
- Keep test data realistic and up-to-date
- Review and refactor test code for maintainability

### Test Organization
- Use clear, descriptive scenario names
- Group related tests with appropriate tags
- Maintain consistent naming conventions

### Reporting
- Review HTML reports for detailed test analysis
- Use JSON reports for trend analysis
- Integrate with test management tools

---

*This test suite provides comprehensive coverage of the PrimeSky website functionality and serves as a foundation for automated testing in CI/CD pipelines.*