@API @UserManagement
Feature: User Management API
  As a system administrator
  I want to manage users through the API
  So that I can create, update, and delete user accounts

  Background:
    Given the API base URL is configured
    And the API authentication token is set

  @Smoke @Positive
  Scenario: Create a new user successfully
    When I send a POST request to "/users" with user data
    Then the response status code should be 201
    And the response should contain the created user details
    And the user should be created in the database

  @Positive
  Scenario: Get user details by ID
    Given a user exists with ID "123"
    When I send a GET request to "/users/123"
    Then the response status code should be 200
    And the response should contain user details

  @Positive
  Scenario: Update user details
    Given a user exists with ID "123"
    When I send a PUT request to "/users/123" with updated data
    Then the response status code should be 200
    And the user details should be updated in the database

  @Positive
  Scenario: Delete user
    Given a user exists with ID "123"
    When I send a DELETE request to "/users/123"
    Then the response status code should be 204
    And the user should be deleted from the database

  @Negative
  Scenario: Create user with invalid data
    When I send a POST request to "/users" with invalid data
    Then the response status code should be 400
    And the response should contain validation errors

  @DataDriven
  Scenario Outline: Create users with different roles
    When I send a POST request to "/users" with role "<role>"
    Then the response status code should be "<expected_status>"
    And the user should have role "<role>"

    Examples:
      | role      | expected_status |
      | admin     | 201             |
      | user      | 201             |
      | guest     | 201             |
      | invalid   | 400             |

  @Mixed @UI_Validation
  Scenario: Create user via API and verify in UI
    When I create a new user via API
    And I login to the application
    And I navigate to the user management page
    Then I should see the created user in the list