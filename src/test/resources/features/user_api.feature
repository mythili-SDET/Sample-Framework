@api @regression
Feature: User Management API
  As a developer
  I want to test the User Management API
  So that I can ensure it works correctly

  Background:
    Given I set the API base URL
    And I set the authentication token

  @smoke @get
  Scenario: Get all users
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response should be valid JSON
    And the response body should not be empty
    And the response time should be less than 2000 milliseconds

  @get @positive
  Scenario: Get user by ID
    Given I set path parameter "id" to "1"
    When I send a GET request to "/users/{id}"
    Then the response status code should be 200
    And the response should contain field "id"
    And the response should contain field "name"
    And the response should contain field "email"
    And the response field "id" should be number 1

  @get @negative
  Scenario: Get non-existent user
    Given I set path parameter "id" to "999999"
    When I send a GET request to "/users/{id}"
    Then the response status code should be 404
    And the response should contain "User not found"

  @post @positive
  Scenario: Create new user
    Given I set request body field "name" to "John Doe"
    And I set request body field "email" to "john.doe@example.com"
    And I set request body field "age" to number 30
    And I set request body field "active" to boolean true
    When I send a POST request to "/users"
    Then the response status code should be 201
    And the response should contain field "id"
    And the response field "name" should be "John Doe"
    And the response field "email" should be "john.doe@example.com"
    And the response field "age" should be number 30
    And the response field "active" should be boolean true
    When I extract "id" from response and store as "newUserId"

  @post @negative
  Scenario: Create user with invalid data
    Given I set request body field "name" to ""
    And I set request body field "email" to "invalid-email"
    When I send a POST request to "/users"
    Then the response status code should be 400
    And the response should contain "Validation error"

  @put @positive
  Scenario: Update existing user
    Given I set path parameter "id" to "1"
    And I set request body field "name" to "Jane Smith"
    And I set request body field "email" to "jane.smith@example.com"
    And I set request body field "age" to number 25
    When I send a PUT request to "/users/{id}"
    Then the response status code should be 200
    And the response field "name" should be "Jane Smith"
    And the response field "email" should be "jane.smith@example.com"
    And the response field "age" should be number 25

  @delete @positive
  Scenario: Delete user
    Given I set path parameter "id" to "1"
    When I send a DELETE request to "/users/{id}"
    Then the response status code should be 204

  @delete @negative
  Scenario: Delete non-existent user
    Given I set path parameter "id" to "999999"
    When I send a DELETE request to "/users/{id}"
    Then the response status code should be 404

  @api @search
  Scenario: Search users with query parameters
    Given I set query parameter "name" to "John"
    And I set query parameter "active" to "true"
    When I send a GET request to "/users/search"
    Then the response status code should be 200
    And the response should be valid JSON

  @api @pagination
  Scenario: Get users with pagination
    Given I set query parameter "page" to "1"
    And I set query parameter "limit" to "10"
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response should contain field "page"
    And the response should contain field "limit"
    And the response should contain field "total"
    And the response should contain field "data"

  @api @headers
  Scenario: Validate response headers
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response header "Content-Type" should be "application/json"
    And the response header "X-API-Version" should be "v1"

  @api @data-driven
  Scenario: Create multiple users with JSON data
    Given I set the request body:
      """
      {
        "name": "Test User",
        "email": "test@example.com",
        "age": 28,
        "active": true
      }
      """
    When I send a POST request to "/users"
    Then the response status code should be 201
    And the response field "name" should be "Test User"

  @api @chained
  Scenario: Create user and then retrieve it
    Given I set request body field "name" to "Chained User"
    And I set request body field "email" to "chained@example.com"
    When I send a POST request to "/users"
    Then the response status code should be 201
    When I extract "id" from response and store as "createdUserId"
    And I use extracted "createdUserId" as path parameter "id"
    And I reset the request body
    And I send a GET request to "/users/{id}"
    Then the response status code should be 200
    And the response field "name" should be "Chained User"
    And the response field "email" should be "chained@example.com"

  @api @utility
  Scenario: Debug API response
    When I send a GET request to "/users/1"
    Then I print the response status
    And I print the response headers
    And I print the response body