package service;

import com.microsoft.playwright.Page;
import io.cucumber.datatable.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.HomePage;

/**
 * Website Explorer for https://fdev.primesky.com/
 * This class explores the website to identify key functionalities and UI
 * elements for testing
 */
public class PlaywrightService {
    private static final BrowserManager browserManager = BrowserManager.getInstance();
    private static final Page page = browserManager.getPage();
    private static final HomePage homePage = new HomePage(page);

    /**
     * Loads the PrimeSky homepage using HomePage page object.
     * Handles navigation, waits, and error handling.
     */
    public static void loadPrimeSkyHomepage() {
        try {
            homePage.load();
            homePage.assertLoaded();
        } catch (Exception e) {
            // Optionally, capture screenshot on failure if implemented
            throw new RuntimeException("Failed to load PrimeSky homepage", e);
        }
    }

    /**
     * Performs a flight search using data from a Cucumber DataTable.
     * Handles input validation, logging, error handling, and delegates to HomePage.
     */
    public static void searchForFlight(DataTable dataTable) {
        Logger logger = LogManager.getLogger(PlaywrightService.class);
        ErrorHandlingUtil.runWithErrorHandling(() -> {
            if (dataTable == null || dataTable.asMaps().isEmpty()) {
                throw new IllegalArgumentException("Flight search data table is empty or null");
            }
            // Only use the first row for this scenario
            var row = dataTable.asMaps().get(0);
            String from = row.getOrDefault("from", "");
            String to = row.getOrDefault("to", "");
            String departureDate = row.getOrDefault("departure date", "");
            String returnDate = row.getOrDefault("return date", "");
            String passengers = row.getOrDefault("passengers", "");
            String flightClass = row.getOrDefault("class", "");
            // Input validation
            if (from.isBlank() || to.isBlank() || departureDate.isBlank() || returnDate.isBlank() || passengers.isBlank() || flightClass.isBlank()) {
                throw new IllegalArgumentException("One or more flight search parameters are missing: " + row);
            }
            logger.info("Delegating flight search to HomePage: {}", row);
            homePage.searchForFlight(from, to, departureDate, returnDate, passengers, flightClass);
        }, "logs/playwrightservice_flight_search_failure.png", logger, page, "Failed to perform flight search");
    }

    // ...existing code...
}
