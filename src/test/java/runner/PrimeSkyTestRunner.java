package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * Test Runner for PrimeSky Website Cucumber Tests
 * Executes all feature files with comprehensive reporting
 */
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
    // This class remains empty - annotations do all the work
}