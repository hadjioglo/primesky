package pages;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Uses multiple selector strategies and explicit waits for robustness.
     * Captures screenshot and logs errors on failure.
     */
    public void searchForFlight(String from, String to, String departureDate, String returnDate, String passengers, String flightClass) {
        try {
            logger.info("Starting flight search: from={}, to={}, departureDate={}, returnDate={}, passengers={}, class={}", from, to, departureDate, returnDate, passengers, flightClass);
            // Wait for and fill 'from' field
            String fromSelector = "input[name='port_from']";
            page.waitForSelector(fromSelector, new Page.WaitForSelectorOptions().setTimeout(10000));
            page.fill(fromSelector, from);
            logger.info("Filled 'From' field with: {}", from);
            // Wait for airport suggestion and select
            String airportSuggestionSelector = "div.port_loc[data-code]";
            page.waitForSelector(airportSuggestionSelector, new Page.WaitForSelectorOptions().setTimeout(10000));
            page.click(airportSuggestionSelector + "[data-code='" + from + "']");
            logger.info("Selected airport suggestion for 'From': {}", from);
            // Fill 'to' field
            String toSelector = "input[name='port_to']";
            page.waitForSelector(toSelector, new Page.WaitForSelectorOptions().setTimeout(10000));
            page.fill(toSelector, to);
            logger.info("Filled 'To' field with: {}", to);
            // Wait for airport suggestion and select
            page.waitForSelector(airportSuggestionSelector, new Page.WaitForSelectorOptions().setTimeout(10000));
            page.click(airportSuggestionSelector + "[data-code='" + to + "']");
            logger.info("Selected airport suggestion for 'To': {}", to);
            // Fill departure and return dates
            page.fill("input[placeholder*='Dates']", departureDate + " - " + returnDate);
            logger.info("Filled dates: {} - {}", departureDate, returnDate);
            // Fill passengers
            page.fill("input[placeholder*='Travelers']", passengers);
            logger.info("Filled passengers: {}", passengers);
            // Fill class
            page.fill("input[placeholder*='Class']", flightClass);
            logger.info("Filled class: {}", flightClass);
            // Click 'SEARCH FLIGHT' button
            String searchButtonSelector = "button:has-text('SEARCH FLIGHT'), .search-flight__btn";
            page.waitForSelector(searchButtonSelector, new Page.WaitForSelectorOptions().setTimeout(10000));
            page.click(searchButtonSelector);
            logger.info("Clicked 'SEARCH FLIGHT' button");
        } catch (Exception e) {
            logger.error("Error during flight search: {}", e.getMessage(), e);
            String screenshotPath = "logs/flight_search_failure.png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            logger.info("Screenshot captured at: {}", screenshotPath);
            throw new RuntimeException("Flight search failed: " + e.getMessage(), e);
        }
    }
}
