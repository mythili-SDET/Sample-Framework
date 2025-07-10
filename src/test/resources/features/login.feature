@ui @regression
Feature: User Login Functionality
  As a user
  I want to log into the application
  So that I can access my account

  Background:
    Given I navigate to the application URL
    And I am on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When I login with credentials "admin" and "password123"
    Then the current URL should contain "dashboard"
    And the page title should be "Dashboard"

  @negative
  Scenario: Login with invalid credentials
    When I login with credentials "invalid" and "wrongpass"
    Then I should see the error message "Invalid username or password"
    And the current URL should contain "login"

  @negative
  Scenario: Login with empty credentials
    When I click the login button
    Then I should see the error message "Username and password are required"

  @positive
  Scenario: Login with remember me option
    When I enter username "admin"
    And I enter password "password123"
    And I login with remember me option
    Then the remember me checkbox should be selected
    And the current URL should contain "dashboard"

  @ui
  Scenario: Forgot password functionality
    When I click forgot password link
    Then the current URL should contain "forgot-password"
    And the page should contain text "Reset your password"

  @data-driven @excel
  Scenario: Login with Excel data
    When I login with data from Excel
    Then the current URL should contain "dashboard"

  @data-driven @json
  Scenario: Login with JSON data
    When I login with data from JSON
    Then the current URL should contain "dashboard"

  @data-driven @csv
  Scenario: Login with CSV data
    When I login with data from CSV
    Then the current URL should contain "dashboard"

  @data-table
  Scenario: Login with data table
    When I login with test data:
      | username | admin     |
      | password | password123 |
    Then the current URL should contain "dashboard"

  @ui @boundary
  Scenario Outline: Login validation with different inputs
    When I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should see the error message "<error_message>"

    Examples:
      | username | password    | error_message                    |
      | admin    |             | Password is required             |
      |          | password123 | Username is required             |
      | a        | p           | Username must be at least 3 characters |
      | admin    | 123         | Password must be at least 6 characters |

  @ui @navigation
  Scenario: Login page navigation features
    When I enter username "testuser"
    And I clear the username field
    Then the username field should contain ""
    When I refresh the page
    Then I am on the login page
    And the username field should contain ""