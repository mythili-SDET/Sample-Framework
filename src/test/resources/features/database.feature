@db @regression
Feature: Database Operations and Validation
  As a developer
  I want to test database operations
  So that I can ensure data integrity and correctness

  @smoke @db
  Scenario: Verify database connection and basic query
    When I execute the query "SELECT COUNT(*) as user_count FROM users"
    Then the query should return 1 row(s)
    And the column "user_count" in first row should be number 0

  @db @select
  Scenario: Select users from database
    When I execute the query:
      """
      SELECT id, name, email, created_at
      FROM users
      WHERE active = 1
      ORDER BY created_at DESC
      """
    Then the query should return at least 0 row(s)
    And I print the query results

  @db @parameterized
  Scenario: Execute parameterized query
    When I execute the parameterized query "SELECT * FROM users WHERE email = ? AND active = ?" with parameters:
      | john.doe@example.com |
      | 1                    |
    Then the query should return at least 0 row(s)

  @db @insert
  Scenario: Insert new user record
    When I execute the update query:
      """
      INSERT INTO users (name, email, age, active, created_at)
      VALUES ('Test User', 'test.user@example.com', 25, 1, NOW())
      """
    Then the update should affect 1 row(s)
    And the table "users" should contain at least 1 row(s)

  @db @update
  Scenario: Update existing user record
    Given I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Update Test', 'update@test.com', 30, 1)"
    When I execute the update query:
      """
      UPDATE users
      SET name = 'Updated User', age = 31
      WHERE email = 'update@test.com'
      """
    Then the update should affect 1 row(s)
    When I execute the query "SELECT name, age FROM users WHERE email = 'update@test.com'"
    Then the query should return 1 row(s)
    And the column "name" in first row should be "Updated User"
    And the column "age" in first row should be number 31

  @db @delete
  Scenario: Delete user record
    Given I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Delete Test', 'delete@test.com', 25, 1)"
    When I execute the update query "DELETE FROM users WHERE email = 'delete@test.com'"
    Then the update should affect 1 row(s)
    When I execute the query "SELECT * FROM users WHERE email = 'delete@test.com'"
    Then the query should return no rows

  @db @validation
  Scenario: Validate specific user data
    Given I execute the update query:
      """
      INSERT INTO users (name, email, age, active, created_at)
      VALUES ('Validation User', 'validation@test.com', 28, 1, NOW())
      """
    Then a record should exist in table "users" where "email = 'validation@test.com'"
    And the data in table "users" where "email = 'validation@test.com'" should be:
      | name   | Validation User      |
      | email  | validation@test.com  |
      | age    | 28                   |
      | active | 1                    |

  @db @transactions
  Scenario: Database transaction rollback
    Given I begin a database transaction
    When I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Transaction Test', 'transaction@test.com', 25, 1)"
    Then the update should affect 1 row(s)
    When I rollback the transaction
    Then no record should exist in table "users" where "email = 'transaction@test.com'"

  @db @transactions
  Scenario: Database transaction commit
    Given I begin a database transaction
    When I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Commit Test', 'commit@test.com', 25, 1)"
    Then the update should affect 1 row(s)
    When I commit the transaction
    Then a record should exist in table "users" where "email = 'commit@test.com'"

  @db @stored-procedure
  Scenario: Execute stored procedure
    When I execute the stored procedure "GetActiveUsers"
    Then the query should return at least 0 row(s)

  @db @stored-procedure
  Scenario: Execute stored procedure with parameters
    When I execute the stored procedure "GetUsersByAge" with parameters:
      | 25 |
      | 35 |
    Then the query should return at least 0 row(s)

  @db @data-extraction
  Scenario: Extract and store database values
    Given I execute the update query:
      """
      INSERT INTO users (name, email, age, active, created_at)
      VALUES ('Extract Test', 'extract@test.com', 30, 1, NOW())
      """
    When I execute the query "SELECT id, name FROM users WHERE email = 'extract@test.com'"
    Then the query should return 1 row(s)
    When I extract column "id" from first row and store as "extractedUserId"
    And I extract column "name" from first row and store as "extractedUserName"
    When I get single value from query "SELECT COUNT(*) as total FROM users" column "total" and store as "totalUsers"
    Then I print the query results

  @db @table-validation
  Scenario: Validate table structure and content
    When I execute the query "DESCRIBE users"
    Then the query should return at least 5 row(s)
    When I execute the query "SELECT COUNT(*) as count FROM users WHERE active = 1"
    Then the first row should contain:
      | count | 0 |

  @db @multiple-queries
  Scenario: Execute multiple related queries
    Given I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Multi Test 1', 'multi1@test.com', 25, 1)"
    And I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Multi Test 2', 'multi2@test.com', 30, 1)"
    When I execute the query "SELECT COUNT(*) as count FROM users WHERE email LIKE 'multi%@test.com'"
    Then the column "count" in first row should be number 2
    When I execute the query "SELECT AVG(age) as avg_age FROM users WHERE email LIKE 'multi%@test.com'"
    Then the column "avg_age" in first row should be number 27

  @db @cleanup
  Scenario: Database cleanup operations
    When I execute the update query "DELETE FROM users WHERE email LIKE '%@test.com'"
    Then I print the row count for table "users"
    When I execute the query "SELECT COUNT(*) as remaining FROM users"
    Then I print the query results