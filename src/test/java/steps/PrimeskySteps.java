package steps;

import static service.PlaywrightService.loadPrimeSkyHomepage;
import io.cucumber.java.en.Given;

public class PrimeskySteps {
    @Given("PrimeSky homepage is loaded")
    public void primeSkyHomepageIsLoaded() {
        loadPrimeSkyHomepage();
    }
}
