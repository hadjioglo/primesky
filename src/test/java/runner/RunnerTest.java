package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        tags = "@Run and not @Manual",
        plugin = {"pretty", "html:target/cucumber-report.html"},
        stepNotifications = true
)
public class RunnerTest {
}