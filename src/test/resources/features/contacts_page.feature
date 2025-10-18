@contacts @ui
Feature: Contacts Page Display

  @page-load @high-priority
  Scenario: Verify Contacts page loads successfully
    Given PrimeSky homepage is loaded
    When user navigates to the Contacts page
    Then Contacts page is displayed
    And the page title should be "Contacts"