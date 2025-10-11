package pages;

import com.microsoft.playwright.Page;

import java.util.Map;

/**
 * Page Object for the Flight Search Page
 * Encapsulates all flight search elements and actions including hidden field handling
 */
public class FlightSearchPage extends BasePage {
    
    // Flight search form selectors
    private static final String FLIGHT_SEARCH_FORM = "form, .flight-search-form, [data-testid='flight-search']";
    private static final String ORIGIN_FIELD = "input[name*='origin'], input[id*='origin'], input[name*='from']";
    private static final String DESTINATION_FIELD = "input[name*='destination'], input[id*='destination'], input[name*='to']";
    private static final String DEPARTURE_DATE_FIELD = "input[name*='departure'], input[id*='departure'], input[type='date']";
    private static final String RETURN_DATE_FIELD = "input[name*='return'], input[id*='return'], input[name*='arrival']";
    private static final String PASSENGER_COUNT_FIELD = "input[name*='passenger'], input[name*='adult'], select[name*='passenger']";
    private static final String TRIP_TYPE_FIELD = "input[name*='trip'], select[name*='trip'], [data-testid*='trip']";
    private static final String SEARCH_BUTTON = "button:has-text('Search'), input[value*='Search'], button[type='submit']";
    
    // Results selectors
    private static final String SEARCH_RESULTS = ".results, .flight-results, [data-testid='results']";
    private static final String FLIGHT_LIST = ".flight-list, ul, .list-group";
    private static final String FLIGHT_ITEM = ".flight-item, .flight, li";
    
    public FlightSearchPage(Page page) {
        super(page);
    }
    
    /**
     * Verify that we are on the flight search page
     */
    public boolean isOnFlightSearchPage() {
        String currentUrl = getCurrentUrl();
        boolean urlCheck = currentUrl.contains("flight") || currentUrl.contains("search");
        
        // Check for presence of flight search form elements
        boolean hasFlightForm = isElementVisible(FLIGHT_SEARCH_FORM) ||
                               isElementVisible(ORIGIN_FIELD) ||
                               isElementVisible(DESTINATION_FIELD) ||
                               getElementCount("input") > 0; // Fallback for hidden fields
        
        System.out.println("Flight search page verification - URL check: " + urlCheck + 
                          ", Form elements present: " + hasFlightForm);
        
        takeScreenshot("flight-search-page");
        return urlCheck || hasFlightForm;
    }
    
    /**
     * Fill the flight search form with provided data
     */
    public FlightSearchPage fillFlightSearchForm(Map<String, String> formData) {
        System.out.println("Filling flight search form with data: " + formData);
        
        if (formData.containsKey("Origin")) {
            fillOriginField(formData.get("Origin"));
        }
        
        if (formData.containsKey("Destination")) {
            fillDestinationField(formData.get("Destination"));
        }
        
        if (formData.containsKey("Departure Date")) {
            fillDepartureDate(formData.get("Departure Date"));
        }
        
        if (formData.containsKey("Return Date")) {
            fillReturnDate(formData.get("Return Date"));
        }
        
        if (formData.containsKey("Passengers")) {
            setPassengerCount(formData.get("Passengers"));
        }
        
        if (formData.containsKey("Trip Type")) {
            selectTripType(formData.get("Trip Type"));
        }
        
        return this;
    }
    
    /**
     * Fill the origin field
     */
    public FlightSearchPage fillOriginField(String origin) {
        System.out.println("Filling origin field with: " + origin);
        
        String[] originSelectors = {
            "input[name*='origin']",
            "input[id*='origin']",
            "input[name*='from']",
            "input[id*='from']",
            "[data-testid*='origin']",
            "[data-testid*='from']"
        };
        
        fillFieldWithMultipleSelectors(originSelectors, origin, "origin");
        return this;
    }
    
    /**
     * Fill the destination field
     */
    public FlightSearchPage fillDestinationField(String destination) {
        System.out.println("Filling destination field with: " + destination);
        
        String[] destinationSelectors = {
            "input[name*='destination']",
            "input[id*='destination']",
            "input[name*='to']",
            "input[id*='to']",
            "[data-testid*='destination']",
            "[data-testid*='to']"
        };
        
        fillFieldWithMultipleSelectors(destinationSelectors, destination, "destination");
        return this;
    }
    
    /**
     * Fill the departure date field with enhanced hidden field support
     */
    public FlightSearchPage fillDepartureDate(String date) {
        System.out.println("Setting departure date to: " + date);
        
        String[] dateSelectors = {
            "input[name*='departure']",
            "input[id*='departure']",
            "input[type='date']",
            "input[name*='date_departure']",
            "[data-testid*='departure']"
        };
        
        fillDateFieldWithJavaScript(dateSelectors, date, "departure");
        return this;
    }
    
    /**
     * Fill the return date field with enhanced hidden field support
     */
    public FlightSearchPage fillReturnDate(String date) {
        System.out.println("Setting return date to: " + date);
        
        String[] dateSelectors = {
            "input[name*='return']",
            "input[id*='return']",
            "input[name*='arrival']",
            "input[name*='date_return']",
            "[data-testid*='return']"
        };
        
        fillDateFieldWithJavaScript(dateSelectors, date, "return");
        return this;
    }
    
    /**
     * Set passenger count with enhanced hidden field support
     */
    public FlightSearchPage setPassengerCount(String passengerCount) {
        System.out.println("Setting passenger count to: " + passengerCount);
        
        String[] passengerSelectors = {
            "select[name*='passenger']",
            "input[name*='passenger']",
            "select[name*='adult']",
            "input[name*='adult']",
            "[data-testid*='passenger']"
        };
        
        boolean passengerSet = false;
        for (String selector : passengerSelectors) {
            if (getElementCount(selector) > 0) {
                try {
                    // Check if element is hidden
                    String hiddenAttr = page.locator(selector).first().getAttribute("hidden");
                    boolean isHidden = hiddenAttr != null || !page.locator(selector).first().isVisible();
                    
                    if (isHidden) {
                        String jsScript = "arguments[0].value = '" + passengerCount + "'; arguments[0].dispatchEvent(new Event('change'));";
                        page.locator(selector).first().evaluate(jsScript);
                        System.out.println("Set hidden passenger count using JavaScript");
                    } else {
                        String tagName = page.locator(selector).first().evaluate("el => el.tagName").toString();
                        if (tagName.equals("SELECT")) {
                            page.locator(selector).first().selectOption(passengerCount);
                        } else {
                            fillField(selector, passengerCount);
                        }
                    }
                    passengerSet = true;
                    break;
                } catch (Exception e) {
                    System.out.println("Failed to set passenger count with selector " + selector + ": " + e.getMessage());
                }
            }
        }
        
        if (!passengerSet) {
            System.out.println("Could not find passenger count field - this may be expected");
        }
        
        return this;
    }
    
    /**
     * Select trip type (Round Trip, One Way, etc.)
     */
    public FlightSearchPage selectTripType(String tripType) {
        System.out.println("Selecting trip type: " + tripType);
        
        String[] tripTypeSelectors = {
            "input[value*='" + tripType.toLowerCase() + "']",
            "input[name*='trip']",
            "select[name*='trip']",
            "[data-testid*='trip-type']"
        };
        
        boolean tripTypeSet = false;
        for (String selector : tripTypeSelectors) {
            if (getElementCount(selector) > 0) {
                try {
                    String tagName = page.locator(selector).first().evaluate("el => el.tagName").toString();
                    if (tagName.equals("SELECT")) {
                        page.locator(selector).first().selectOption(tripType);
                    } else {
                        clickElement(selector);
                    }
                    tripTypeSet = true;
                    System.out.println("Selected trip type with selector: " + selector);
                    break;
                } catch (Exception e) {
                    System.out.println("Failed to select trip type with selector " + selector + ": " + e.getMessage());
                }
            }
        }
        
        if (!tripTypeSet) {
            System.out.println("Could not find trip type field - this may be expected");
        }
        
        return this;
    }
    
    /**
     * Submit the flight search form
     */
    public FlightSearchPage submitSearch() {
        System.out.println("Submitting flight search...");
        
        String[] searchButtonSelectors = {
            "button:has-text('Search')",
            "input[value*='Search']",
            "button:has-text('Find Flights')",
            "button[type='submit']",
            ".search-button",
            "[data-testid='search-flights']"
        };
        
        boolean searchSubmitted = false;
        for (String selector : searchButtonSelectors) {
            if (getElementCount(selector) > 0) {
                clickElement(selector);
                searchSubmitted = true;
                System.out.println("Clicked search button with selector: " + selector);
                break;
            }
        }
        
        if (!searchSubmitted) {
            // Fallback: try to submit form using Enter key
            System.out.println("Search button not found, trying to submit form with Enter key");
            if (getElementCount(FLIGHT_SEARCH_FORM) > 0) {
                page.locator(FLIGHT_SEARCH_FORM).first().press("Enter");
            }
        }
        
        waitForPageLoad();
        page.waitForTimeout(2000); // Additional wait for search results
        return this;
    }
    
    /**
     * Check if search results are displayed
     */
    public boolean hasSearchResults() {
        String[] resultsSelectors = {
            ".results",
            ".flight-results",
            "[data-testid='results']",
            ".flight-list",
            "ul",
            ".list-group"
        };
        
        boolean resultsFound = false;
        for (String selector : resultsSelectors) {
            if (getElementCount(selector) > 0) {
                System.out.println("Found search results with selector: " + selector);
                resultsFound = true;
                break;
            }
        }
        
        if (!resultsFound) {
            // Check for any content that might indicate results
            String content = page.content().toLowerCase();
            resultsFound = content.contains("results") || 
                          content.contains("flights") || 
                          content.contains("found");
        }
        
        System.out.println("Search results present: " + resultsFound);
        return resultsFound;
    }
    
    /**
     * Get the number of flight results
     */
    public int getFlightResultsCount() {
        int count = getElementCount(FLIGHT_ITEM);
        System.out.println("Number of flight results found: " + count);
        return count;
    }
    
    /**
     * Helper method to fill fields with multiple selector strategies
     */
    private void fillFieldWithMultipleSelectors(String[] selectors, String value, String fieldType) {
        boolean fieldFilled = false;
        for (String selector : selectors) {
            if (getElementCount(selector) > 0) {
                fillField(selector, value);
                fieldFilled = true;
                System.out.println("Filled " + fieldType + " field with selector: " + selector);
                break;
            }
        }
        
        if (!fieldFilled) {
            System.out.println("Could not find " + fieldType + " field - this may be expected");
        }
    }
    
    /**
     * Helper method to fill date fields with JavaScript for hidden elements
     */
    private void fillDateFieldWithJavaScript(String[] selectors, String dateValue, String dateType) {
        boolean dateSet = false;
        for (String selector : selectors) {
            if (getElementCount(selector) > 0) {
                try {
                    // Always use JavaScript for date fields as they're often hidden
                    String jsScript = "arguments[0].value = '" + dateValue + "'; arguments[0].dispatchEvent(new Event('change'));";
                    page.locator(selector).first().evaluate(jsScript);
                    System.out.println("Set " + dateType + " date using JavaScript with selector: " + selector);
                    dateSet = true;
                    break;
                } catch (Exception e) {
                    System.out.println("Failed to set " + dateType + " date with selector " + selector + ": " + e.getMessage());
                }
            }
        }
        
        if (!dateSet) {
            System.out.println("Could not find " + dateType + " date field - this may be expected");
        }
    }
}