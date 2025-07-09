Feature: Database Operations Testing
  As a database administrator
  I want to test database operations
  So that I can verify data integrity and functionality

  Background:
    Given I have access to the test database

  @smoke @database
  Scenario: Database Connection Test
    When I connect to the database
    Then the connection should be successful
    And I should be able to execute queries

  @database
  Scenario: Database Query Execution
    When I execute the query "SELECT 1 as test_value"
    Then the query should return results
    And the test_value should be 1

  @database
  Scenario: Database Record Count
    When I get the record count from "users" table
    Then the count should be greater than or equal to 0

  @database
  Scenario: Database Insert Operation
    When I insert a new user with the following data:
      | name      | email              | age |
      | John Doe  | john.doe@test.com | 30  |
    Then the insert operation should be successful
    And the user should exist in the database

  @database
  Scenario: Database Update Operation
    When I update user with ID 1 with the following data:
      | name           | email                 | age |
      | Updated User   | updated.user@test.com | 35  |
    Then the update operation should be successful

  @database
  Scenario: Database Delete Operation
    When I delete user with ID 999
    Then the delete operation should be successful

  @database
  Scenario: Database Record Existence Check
    When I check if user with ID 1 exists
    Then the result should be true or false

  @database
  Scenario: Database Table Structure
    When I get the structure of "users" table
    Then the table should have columns
    And the structure should not be empty

  @database
  Scenario: Database with Excel Data
    When I read user data from Excel file
    And I insert the first user from Excel data
    Then the insert operation should be successful

  @database
  Scenario: Database with CSV Data
    When I read user data from CSV file
    And I insert the first user from CSV data
    Then the insert operation should be successful

  @database
  Scenario Outline: Database Operations with Different Data
    When I insert a user with name "<name>" and email "<email>"
    Then the insert operation should be successful
    And the user should exist in the database

    Examples:
      | name        | email                    |
      | Alice Smith | alice.smith@test.com    |
      | Bob Johnson | bob.johnson@test.com    |
      | Carol Davis | carol.davis@test.com    |