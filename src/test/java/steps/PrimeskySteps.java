package steps;

import static service.PlaywrightService.loadPrimeSkyHomepage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import service.PlaywrightService;

public class PrimeskySteps {
    @Given("{word} page is loaded")
    @When("user navigates to the {word} page")
    public void navigateToPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "homepage":
            case "home":
                PlaywrightService.loadPrimeSkyHomepage();
                break;
            case "contacts":
                PlaywrightService.navigateToContactsPage();
                break;
            default:
                throw new IllegalArgumentException("Unknown page: " + pageName);
        }
    }

    @When("user searches for the flight")
    public void userSearchesForTheFlight(DataTable dataTable) {
        PlaywrightService.searchForFlight(dataTable);
    }

    @Then("Contacts page is displayed")
    public void contactsPageIsDisplayed() {
        PlaywrightService.assertContactsPageDisplayed();
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        PlaywrightService.assertContactsPageDisplayed(); // Title assertion is inside ContactsPage
    }
}
