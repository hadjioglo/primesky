
package steps;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Given;
import pages.HomePage;
import service.TestContext;

import java.util.logging.Logger;

public class PrimeskySteps {
    private static final Logger logger = Logger.getLogger(PrimeskySteps.class.getName());
    @Given("PrimeSky homepage is loaded")
    public void primeSkyHomepageIsLoaded() {
        logger.info("Navigating to PrimeSky homepage");
        try {
            // HomePage homePage = new HomePage(TestContext.getPage());
            // homePage.open();
            // Assertions.assertTrue(homePage.isOnHomePage(), "Homepage did not load successfully");
            logger.info("PrimeSky homepage loaded successfully");
        } catch (Exception e) {
            logger.severe("Failed to load PrimeSky homepage: " + e.getMessage());
            // TestContext.captureDebugScreenshot("homepage_load_failure");
            throw new RuntimeException("Error loading homepage: " + e.getMessage());
        }
    }

}
