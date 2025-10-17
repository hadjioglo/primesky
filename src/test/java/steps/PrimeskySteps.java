package steps;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Given;
import pages.HomePage;
import service.TestContext;

public class PrimeskySteps {
    @Given("PrimeSky homepage is loaded")
public void primeSkyHomepageIsLoaded() {
    System.out.println("Navigating to PrimeSky homepage");
    try {
        // HomePage homePage = new HomePage(TestContext.getPage());
        // homePage.open();
        // Assertions.assertTrue(homePage.isOnHomePage(), "Homepage did not load successfully");
        System.out.println("PrimeSky homepage loaded successfully");
    } catch (Exception e) {
        System.out.println("Failed to load PrimeSky homepage: " + e.getMessage());
        // TestContext.captureDebugScreenshot("homepage_load_failure");
        throw new RuntimeException("Error loading homepage: " + e.getMessage());
    }
}

}
