@smoke @critical-path
Feature: PrimeSky Website Core Functionality

  @page-load @high-priority
  Scenario: Verify homepage loads successfully
    Given user navigates to the homepage page
    When user searches for the flight
      | from   | to       | departure date | return date | passengers | class |
      | London | New York | 2026-02-15     | 2026-02-25  | 1          | E     |
#    Then flight results are displayed
#    And the search results should contain flights from "London" to "New Yorks"
#    And the results should show the correct departure date
