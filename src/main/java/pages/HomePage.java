package pages;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.ErrorHandlingUtil;
import util.PageInteractionUtil;

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
        String[] fromSelectors = {
            "input[data-testid='from']", "input[name*='from']", "input[id*='from']", "input[class*='from']", "input[placeholder*='From']"
        };
        boolean filled = java.util.Arrays.stream(fromSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, from,
                PageInteractionUtil.ElementAction.FILL,
                "logs/fill_from_failure.png",
                "Failed to fill 'from' field"
            ));
        if (!filled) {
            logger.error("Could not fill 'from' field with any selector");
        }
    }

    private void fillToField(String to) {
        String[] toSelectors = {
            "input[data-testid='to']", "input[name*='to']", "input[id*='to']", "input[class*='to']", "input[placeholder*='To']"
        };
        boolean filled = java.util.Arrays.stream(toSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, to,
                PageInteractionUtil.ElementAction.FILL,
                "logs/fill_to_failure.png",
                "Failed to fill 'to' field"
            ));
        if (!filled) {
            logger.error("Could not fill 'to' field with any selector");
        }
    }

    private void fillDates(String departureDate, String returnDate) {
        String[] dateSelectors = {
            "input[name*='date_departure']", "input[id*='date_departure']", "input[data-testid*='date_departure']", "input[placeholder*='Dates']"
        };
        boolean filled = java.util.Arrays.stream(dateSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, departureDate,
                PageInteractionUtil.ElementAction.FILL,
                "logs/fill_date_failure.png",
                "Failed to fill departure date"
            ));
        if (!filled) {
            logger.error("Could not fill departure date with any selector");
        }
    }

    private void fillPassengers(String passengers) {
        String[] passengerSelectors = {
            "input[name*='passenger']", "input[id*='passenger']", "input[data-testid*='passenger']", "input[placeholder*='Travelers']", "input[name*='adult']"
        };
        boolean filled = java.util.Arrays.stream(passengerSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, passengers,
                PageInteractionUtil.ElementAction.FILL,
                "logs/fill_passengers_failure.png",
                "Failed to fill passengers"
            ));
        if (!filled) {
            logger.error("Could not fill passengers with any selector");
        }
    }

    private void fillClass(String flightClass) {
        String[] classSelectors = {
            "input[name*='class']", "input[id*='class']", "input[data-testid*='class']", "input[placeholder*='Class']"
        };
        boolean filled = java.util.Arrays.stream(classSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, flightClass,
                PageInteractionUtil.ElementAction.FILL,
                "logs/fill_class_failure.png",
                "Failed to fill class"
            ));
        if (!filled) {
            logger.error("Could not fill class with any selector");
        }
    }

    private void clickSearchButton() {
        String[] buttonSelectors = {
            "button:has-text('SEARCH FLIGHT')", ".search-flight__btn", "button[type='submit']", "button[data-testid*='search']"
        };
        boolean clicked = java.util.Arrays.stream(buttonSelectors)
            .anyMatch(selector -> PageInteractionUtil.interactWithElement(
                page, logger, selector, null,
                PageInteractionUtil.ElementAction.CLICK,
                "logs/click_search_failure.png",
                "Failed to click search button"
            ));
        if (!clicked) {
            logger.error("Could not click search button with any selector");
        }
    }
}
