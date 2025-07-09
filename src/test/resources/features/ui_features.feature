Feature: Google Search Functionality
  As a user
  I want to search for information on Google
  So that I can find relevant results

  Background:
    Given I am on the Google homepage

  @smoke @ui
  Scenario: Basic Google Search
    When I search for "Selenium WebDriver"
    Then I should see search results
    And the page title should contain "Selenium WebDriver"

  @regression @ui
  Scenario: Google Search with Data Table
    When I search for the following terms:
      | SearchTerm    | ExpectedResult    |
      | Java          | Java Programming  |
      | Python        | Python Programming|
      | TestNG        | TestNG Framework  |
    Then I should see search results for each term

  @ui
  Scenario: Google Search Suggestions
    When I type "selenium" in the search box
    Then I should see search suggestions
    And the suggestions should contain "selenium"

  @ui
  Scenario: Google Search Box Functionality
    When I enter "Test Search" in the search box
    Then the search box should contain "Test Search"
    When I clear the search box
    Then the search box should be empty

  @ui
  Scenario: Google Page Navigation
    When I navigate to Google
    Then the page title should contain "Google"
    And the URL should contain "google.com"
    And the search box should be displayed
    And the search button should be displayed

  @ui
  Scenario Outline: Google Search with Different Terms
    When I search for "<search_term>"
    Then I should see search results
    And the page should contain "<expected_result>"

    Examples:
      | search_term | expected_result |
      | Selenium    | Selenium        |
      | Java        | Java            |
      | Python      | Python          |
      | TestNG      | TestNG          |