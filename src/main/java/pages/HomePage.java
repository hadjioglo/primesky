package pages;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ErrorHandlingUtil;

public class HomePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void load() {
        String baseUrl = "https://fdev.primesky.com/";
        page.navigate(baseUrl);
        page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(300000));
    }

    public void assertLoaded() {
        String title = page.title();
        if (!title.toLowerCase().contains("home")) {
            throw new AssertionError("PrimeSky homepage did not load as expected. Title: " + title);
        }
    }

    /**
     * Performs a flight search using the provided parameters.
     * Delegates each UI interaction to dedicated helper methods for maintainability.
     * Captures screenshot and logs errors on failure.
     */
    public void searchForFlight(String from, String to, String departureDate, String returnDate, String passengers, String flightClass) {
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            logger.info("Starting flight search: from={}, to={}, departureDate={}, returnDate={}, passengers={}, class={}", from, to, departureDate, returnDate, passengers, flightClass);
            fillFromField(from);
            fillToField(to);
            fillDates(departureDate, returnDate);
            fillPassengers(passengers);
            fillClass(flightClass);
            clickSearchButton();
            logger.info("Flight search completed for: from={}, to={}, departureDate={}, returnDate={}, passengers={}, class={}", from, to, departureDate, returnDate, passengers, flightClass);
        }, "logs/flight_search_failure.png", logger, page, "Flight search failed");
    }

    // Helper methods for each UI interaction
    private void fillFromField(String from) {
        String fromSelector = "input[name='port_from']";
        page.waitForSelector(fromSelector);
        page.fill(fromSelector, from);
        String airportSuggestionSelector = "div.port_loc[data-code]";
        page.waitForSelector(airportSuggestionSelector);
        page.click(airportSuggestionSelector + "[data-code='" + from + "']");
        logger.debug("Filled 'from' field with: {}", from);
    }

    private void fillToField(String to) {
        String toSelector = "input[name='port_to']";
        page.waitForSelector(toSelector);
        page.fill(toSelector, to);
        String airportSuggestionSelector = "div.port_loc[data-code]";
        page.waitForSelector(airportSuggestionSelector);
        page.click(airportSuggestionSelector + "[data-code='" + to + "']");
        logger.debug("Filled 'to' field with: {}", to);
    }

    private void fillDates(String departureDate, String returnDate) {
        page.fill("input[placeholder*='Dates']", departureDate + " - " + returnDate);
        logger.debug("Filled dates: {} - {}", departureDate, returnDate);
    }

    private void fillPassengers(String passengers) {
        page.fill("input[placeholder*='Travelers']", passengers);
        logger.debug("Filled passengers: {}", passengers);
    }

    private void fillClass(String flightClass) {
        page.fill("input[placeholder*='Class']", flightClass);
        logger.debug("Filled class: {}", flightClass);
    }

    private void clickSearchButton() {
        String searchButtonSelector = "button:has-text('SEARCH FLIGHT'), .search-flight__btn";
        page.waitForSelector(searchButtonSelector);
        page.click(searchButtonSelector);
        logger.debug("Clicked 'SEARCH FLIGHT' button");
    }
}
