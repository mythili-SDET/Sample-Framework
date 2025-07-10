@UI @Login
Feature: User Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  Background:
    Given the user is on the login page

  @Smoke @Positive
  Scenario: Successful login with valid credentials
    When the user enters valid username "testuser@example.com"
    And the user enters valid password "TestPassword123"
    And the user clicks the login button
    Then the user should be successfully logged in
    And the user should see the dashboard

  @Negative
  Scenario: Failed login with invalid credentials
    When the user enters invalid username "invalid@example.com"
    And the user enters invalid password "WrongPassword"
    And the user clicks the login button
    Then the user should see an error message
    And the user should remain on the login page

  @DataDriven
  Scenario Outline: Login with different user types
    When the user enters username "<username>"
    And the user enters password "<password>"
    And the user clicks the login button
    Then the user should see "<expected_result>"

    Examples:
      | username              | password        | expected_result |
      | admin@example.com     | AdminPass123    | dashboard       |
      | user@example.com      | UserPass123     | dashboard       |
      | invalid@example.com   | WrongPassword   | error message   |

  @API_Setup
  Scenario: Login after API user creation
    Given a new user is created via API
    When the user logs in with the created credentials
    Then the user should be successfully logged in