@Database @Validation
Feature: Database Validation
  As a test engineer
  I want to validate database operations
  So that I can ensure data integrity

  Background:
    Given the database connection is established
    And the test data is prepared

  @Smoke @Positive
  Scenario: Verify user data in database after API creation
    When I create a user via API
    Then the user should exist in the database
    And the user details should match the API response

  @Positive
  Scenario: Verify data consistency across tables
    Given a user exists in the users table
    When I check the related records in user_profiles table
    Then the user profile should exist
    And the data should be consistent

  @Positive
  Scenario: Validate database constraints
    When I try to insert duplicate user data
    Then the operation should fail with constraint violation
    And the error message should indicate the constraint

  @DataDriven
  Scenario Outline: Validate different data types
    When I insert data with type "<data_type>"
    Then the data should be stored correctly
    And the data type should be "<expected_type>"

    Examples:
      | data_type | expected_type |
      | string    | VARCHAR       |
      | integer   | INT           |
      | decimal   | DECIMAL       |
      | date      | DATE          |
      | boolean   | BOOLEAN       |

  @Mixed @API_DB_Validation
  Scenario: End-to-end data validation
    When I create a user via API
    And I verify the user exists in the database
    And I update the user via API
    Then the database should reflect the updated data
    And the API response should match the database state

  @Cleanup
  Scenario: Clean up test data
    When I delete test data from the database
    Then the test data should be removed
    And the database should be in a clean state