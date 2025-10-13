@user-interactions @forms
Feature: PrimeSky User Interaction and Forms
  As a user of the PrimeSky website
  The user wants to interact with forms and UI elements
  So that tasks can be completed and information provided

  Background:
    Given PrimeSky homepage is loaded

  @form-validation @high-priority
  Scenario: Verify contact form validation
    Given contact page is navigated to
    When contact form is submitted without filling required fields
    Then appropriate validation error messages are displayed
    And form is not submitted
    And error messages are clearly visible

  @form-submission @high-priority
  Scenario: Verify successful form submission
    Given contact page is navigated to
    When all required fields are filled with valid data
      | Field        | Value                    |
      | Name         | John Doe                |
      | Email        | john.doe@example.com    |
      | Subject      | Test Inquiry            |
      | Message      | This is a test message  |
    And contact form is submitted
    Then success confirmation message is displayed
    And form is reset or shows appropriate next steps

  @search-functionality @medium-priority
  Scenario Outline: Verify search functionality
    Given search feature is available
    When search for "<search_term>" is performed
    Then search results related to "<search_term>" are displayed
    And results are properly formatted
    And pagination works if applicable

    Examples:
      | search_term |
      | services    |
      | contact     |
      | about       |

  @interactive-elements @medium-priority
  Scenario: Verify interactive UI elements
    When dropdown menus are interacted with
    Then they expand and collapse properly
    When navigation items are hovered over
    Then appropriate hover effects are displayed
    When action buttons are clicked
    Then visual feedback is provided

  @error-handling @medium-priority
  Scenario: Verify error handling for invalid inputs
    Given page with input forms is open
    When invalid data is entered in form fields
      | Field    | Invalid_Value           |
      | Email    | invalid-email-format   |
      | Phone    | invalid-phone-123      |
      | Date     | invalid-date-format    |
    Then appropriate error messages are displayed
    And form prevents submission
    And error styling is applied to invalid fields

  @flight-search @high-priority
  Scenario: Search for flights with valid criteria
    Given flight search page is loaded
    When flight search form is filled with valid details
      | Field           | Value       |
      | Origin          | New York    |
      | Destination     | Los Angeles |
      | Departure Date  | 2025-12-15  |
      | Return Date     | 2025-12-22  |
      | Passengers      | 2           |
      | Trip Type       | Round Trip  |
    And flight search is submitted
    Then list of available flights is displayed
    And search results contain flights from "New York" to "Los Angeles"
    And results show the correct departure date
    And results display flight details like price, duration, and airline

  @flight-search @medium-priority
  Scenario: Search for one-way flights
    Given flight search page is loaded
    When "One Way" trip type is selected
    And one-way flight search form is filled
      | Field           | Value       |
      | Origin          | Chicago     |
      | Destination     | Miami       |
      | Departure Date  | 2025-11-20  |
      | Passengers      | 1           |
    And flight search is submitted
    Then list of available one-way flights is displayed
    And search results do not show return flight options
    And each flight result displays departure time and arrival time

  @flight-search @validation @medium-priority
  Scenario: Validate flight search form fields
    Given flight search page is loaded
    When flight search form is submitted without filling required fields
    Then validation errors for required fields are displayed
    And search is not executed
    When invalid departure date in the past is entered
    Then error message about invalid date is displayed
    When same city is entered for origin and destination
    Then error message about identical locations is displayed

  @flight-search @filters @low-priority
  Scenario: Apply filters to flight search results
    Given flight search page is loaded
    And flight search with results displayed has been performed
    When filters are applied to the search results
      | Filter Type    | Value       |
      | Price Range    | $200-$500   |
      | Departure Time | Morning     |
      | Airline        | American    |
    Then results are filtered accordingly
    And only flights matching the filter criteria are displayed
    And filter count is updated