package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Test Runner specifically for Flight Search scenarios
 * This runner focuses only on the flight search functionality
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/user_interactions.feature",
        glue = "steps",
        tags = "@flight-search",
        plugin = {
                "pretty",
                "html:target/flight-search-reports.html",
                "json:target/flight-search-reports.json",
                "junit:target/flight-search-reports.xml"
        },
        monochrome = true
)
public class FlightSearchTestRunner {
    // This class is used as a test runner for Cucumber tests
}