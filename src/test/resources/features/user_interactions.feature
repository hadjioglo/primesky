@user-interactions @forms
Feature: PrimeSky User Interaction and Forms
  As a user of the PrimeSky website
  I want to interact with forms and UI elements
  So that I can complete my tasks and provide information

  Background:
    Given I am on the PrimeSky homepage

  @form-validation @high-priority
  Scenario: Verify contact form validation
    Given I navigate to the contact page
    When I submit the contact form without filling required fields
    Then I should see appropriate validation error messages
    And the form should not be submitted
    And error messages should be clearly visible

  @form-submission @high-priority
  Scenario: Verify successful form submission
    Given I navigate to the contact page
    When I fill in all required fields with valid data
      | Field        | Value                    |
      | Name         | John Doe                |
      | Email        | john.doe@example.com    |
      | Subject      | Test Inquiry            |
      | Message      | This is a test message  |
    And I submit the contact form
    Then I should see a success confirmation message
    And the form should be reset or show appropriate next steps

  @search-functionality @medium-priority
  Scenario Outline: Verify search functionality
    Given the search feature is available
    When I search for "<search_term>"
    Then I should see search results related to "<search_term>"
    And the results should be properly formatted
    And pagination should work if applicable

    Examples:
      | search_term |
      | services    |
      | contact     |
      | about       |

  @interactive-elements @medium-priority
  Scenario: Verify interactive UI elements
    When I interact with dropdown menus
    Then they should expand and collapse properly
    When I hover over navigation items
    Then appropriate hover effects should be displayed
    When I click on action buttons
    Then they should provide visual feedback

  @error-handling @medium-priority
  Scenario: Verify error handling for invalid inputs
    Given I am on a page with input forms
    When I enter invalid data in form fields
      | Field    | Invalid_Value           |
      | Email    | invalid-email-format   |
      | Phone    | invalid-phone-123      |
      | Date     | invalid-date-format    |
    Then appropriate error messages should be displayed
    And the form should prevent submission
    And error styling should be applied to invalid fields

  @flight-search @high-priority
  Scenario: Search for flights with valid criteria
    Given I am on the flight search page
    When I fill in the flight search form with valid details
      | Field           | Value       |
      | Origin          | New York    |
      | Destination     | Los Angeles |
      | Departure Date  | 2025-12-15  |
      | Return Date     | 2025-12-22  |
      | Passengers      | 2           |
      | Trip Type       | Round Trip  |
    And I submit the flight search
    Then I should see a list of available flights
    And the search results should contain flights from "New York" to "Los Angeles"
    And the results should show the correct departure date
    And the results should display flight details like price, duration, and airline

  @flight-search @medium-priority
  Scenario: Search for one-way flights
    Given I am on the flight search page
    When I select "One Way" trip type
    And I fill in the one-way flight search form
      | Field           | Value       |
      | Origin          | Chicago     |
      | Destination     | Miami       |
      | Departure Date  | 2025-11-20  |
      | Passengers      | 1           |
    And I submit the flight search
    Then I should see a list of available one-way flights
    And the search results should not show return flight options
    And each flight result should display departure time and arrival time

  @flight-search @validation @medium-priority
  Scenario: Validate flight search form fields
    Given I am on the flight search page
    When I submit the flight search form without filling required fields
    Then I should see validation errors for required fields
    And the search should not be executed
    When I enter an invalid departure date in the past
    Then I should see an error message about invalid date
    When I enter the same city for origin and destination
    Then I should see an error message about identical locations

  @flight-search @filters @low-priority
  Scenario: Apply filters to flight search results
    Given I am on the flight search page
    And I have performed a flight search with results displayed
    When I apply filters to the search results
      | Filter Type    | Value       |
      | Price Range    | $200-$500   |
      | Departure Time | Morning     |
      | Airline        | American    |
    Then the results should be filtered accordingly
    And only flights matching the filter criteria should be displayed
    And the filter count should be updated