package steps;

import static service.PlaywrightService.loadPrimeSkyHomepage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import service.PlaywrightService;

public class PrimeskySteps {
    @Given("PrimeSky homepage is loaded")
    public void primeSkyHomepageIsLoaded() {
        loadPrimeSkyHomepage();
    }

    @When("user searches for the flight")
    public void userSearchesForTheFlight(DataTable dataTable) {
        PlaywrightService.searchForFlight(dataTable);
    }
}
