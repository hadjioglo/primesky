@smoke @critical-path
Feature: PrimeSky Website Core Functionality
  As a user of the PrimeSky website
  The user wants to access and navigate the core features
  So that tasks can be accomplished efficiently

  Background:
    Given PrimeSky homepage is loaded

  @page-load @high-priority
  Scenario: Verify homepage loads successfully
    When PrimeSky website is navigated to
    Then page loads completely
    And page title is displayed
    And all critical page elements are visible
    And no JavaScript errors are present

  @navigation @high-priority
  Scenario Outline: Verify main navigation functionality
    When "<navigation_item>" navigation item is clicked in the main menu
    Then "<expected_page>" page is navigated to
    And page loads within acceptable time
    And navigation item is highlighted as active

    Examples:
      | navigation_item | expected_page |
      | Home           | Homepage      |
      | About          | About Us      |
      | Services       | Services      |
      | Contact        | Contact       |

  @responsive @medium-priority
  Scenario Outline: Verify responsive design functionality
    Given website is viewed on "<device_type>"
    When browser is resized to "<viewport_size>"
    Then page layout adapts appropriately
    And all navigation elements remain accessible
    And content is readable and properly formatted

    Examples:
      | device_type | viewport_size |
      | Desktop     | 1920x1080    |
      | Tablet      | 768x1024     |
      | Mobile      | 375x667      |

  @performance @medium-priority
  Scenario: Verify page performance metrics
    When homepage is navigated to
    Then page loads within 3 seconds
    And all images load properly
    And the page should be interactive within 5 seconds
    And there should be no console errors

  @accessibility @medium-priority
  Scenario: Verify basic accessibility compliance
    When the user navigates to the homepage
    Then all images should have alt text
    And form elements should have proper labels
    And the page should be navigable using keyboard only
    And color contrast should meet WCAG guidelines