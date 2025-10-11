@user-interactions @forms
Feature: PrimeSky User Interaction and Forms
  As a user of the PrimeSky website
  The user wants to interact with forms and UI elements
  So that tasks can be completed and information provided

  Background:
    Given the user is on the PrimeSky homepage

  @form-validation @high-priority
  Scenario: Verify contact form validation
    Given the user navigates to the contact page
    When the contact form is submitted without filling required fields
    Then appropriate validation error messages should be displayed
    And the form should not be submitted
    And error messages should be clearly visible

  @form-submission @high-priority
  Scenario: Verify successful form submission
    Given the user navigates to the contact page
    When all required fields are filled with valid data
      | Field        | Value                    |
      | Name         | John Doe                |
      | Email        | john.doe@example.com    |
      | Subject      | Test Inquiry            |
      | Message      | This is a test message  |
    And the contact form is submitted
    Then a success confirmation message should be displayed
    And the form should be reset or show appropriate next steps

  @search-functionality @medium-priority
  Scenario Outline: Verify search functionality
    Given the search feature is available
    When a search for "<search_term>" is performed
    Then search results related to "<search_term>" should be displayed
    And the results should be properly formatted
    And pagination should work if applicable

    Examples:
      | search_term |
      | services    |
      | contact     |
      | about       |

  @interactive-elements @medium-priority
  Scenario: Verify interactive UI elements
    When dropdown menus are interacted with
    Then they should expand and collapse properly
    When navigation items are hovered over
    Then appropriate hover effects should be displayed
    When action buttons are clicked
    Then visual feedback should be provided

  @error-handling @medium-priority
  Scenario: Verify error handling for invalid inputs
    Given a page with input forms is open
    When invalid data is entered in form fields
      | Field    | Invalid_Value           |
      | Email    | invalid-email-format   |
      | Phone    | invalid-phone-123      |
      | Date     | invalid-date-format    |
    Then appropriate error messages should be displayed
    And the form should prevent submission
    And error styling should be applied to invalid fields

  @flight-search @high-priority
  Scenario: Search for flights with valid criteria
    Given the flight search page is open
    When the flight search form is filled with valid details
      | Field           | Value       |
      | Origin          | New York    |
      | Destination     | Los Angeles |
      | Departure Date  | 2025-12-15  |
      | Return Date     | 2025-12-22  |
      | Passengers      | 2           |
      | Trip Type       | Round Trip  |
    And the flight search is submitted
    Then a list of available flights should be displayed
    And the search results should contain flights from "New York" to "Los Angeles"
    And the results should show the correct departure date
    And the results should display flight details like price, duration, and airline

  @flight-search @medium-priority
  Scenario: Search for one-way flights
    Given the flight search page is open
    When "One Way" trip type is selected
    And the one-way flight search form is filled
      | Field           | Value       |
      | Origin          | Chicago     |
      | Destination     | Miami       |
      | Departure Date  | 2025-11-20  |
      | Passengers      | 1           |
    And the flight search is submitted
    Then a list of available one-way flights should be displayed
    And the search results should not show return flight options
    And each flight result should display departure time and arrival time

  @flight-search @validation @medium-priority
  Scenario: Validate flight search form fields
    Given the flight search page is open
    When the flight search form is submitted without filling required fields
    Then validation errors for required fields should be displayed
    And the search should not be executed
    When an invalid departure date in the past is entered
    Then an error message about invalid date should be displayed
    When the same city is entered for origin and destination
    Then an error message about identical locations should be displayed

  @flight-search @filters @low-priority
  Scenario: Apply filters to flight search results
    Given the flight search page is open
    And a flight search with results displayed has been performed
    When filters are applied to the search results
      | Filter Type    | Value       |
      | Price Range    | $200-$500   |
      | Departure Time | Morning     |
      | Airline        | American    |
    Then the results should be filtered accordingly
    And only flights matching the filter criteria should be displayed
    And the filter count should be updated