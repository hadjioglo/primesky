@smoke @critical-path
Feature: PrimeSky Website Core Functionality
  As a user of the PrimeSky website
  The user wants to access and navigate the core features
  So that tasks can be accomplished efficiently

  Background:
    Given the user is on the PrimeSky homepage

  @page-load @high-priority
  Scenario: Verify homepage loads successfully
    When the user navigates to the PrimeSky website
    Then the page should load completely
    And the page title should be displayed
    And all critical page elements should be visible
    And no JavaScript errors should be present

  @navigation @high-priority
  Scenario Outline: Verify main navigation functionality
    When the user clicks on the "<navigation_item>" in the main menu
    Then the user should be navigated to the "<expected_page>" page
    And the page should load within acceptable time
    And the navigation item should be highlighted as active

    Examples:
      | navigation_item | expected_page |
      | Home           | Homepage      |
      | About          | About Us      |
      | Services       | Services      |
      | Contact        | Contact       |

  @responsive @medium-priority
  Scenario Outline: Verify responsive design functionality
    Given the user is viewing the website on "<device_type>"
    When the user resizes the browser to "<viewport_size>"
    Then the page layout should adapt appropriately
    And all navigation elements should remain accessible
    And content should be readable and properly formatted

    Examples:
      | device_type | viewport_size |
      | Desktop     | 1920x1080    |
      | Tablet      | 768x1024     |
      | Mobile      | 375x667      |

  @performance @medium-priority
  Scenario: Verify page performance metrics
    When the user navigates to the homepage
    Then the page should load within 3 seconds
    And all images should load properly
    And the page should be interactive within 5 seconds
    And there should be no console errors

  @accessibility @medium-priority
  Scenario: Verify basic accessibility compliance
    When the user navigates to the homepage
    Then all images should have alt text
    And form elements should have proper labels
    And the page should be navigable using keyboard only
    And color contrast should meet WCAG guidelines