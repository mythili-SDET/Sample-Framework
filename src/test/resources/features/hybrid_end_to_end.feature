@hybrid @end-to-end @regression
Feature: Hybrid End-to-End User Management
  As a test engineer
  I want to test complete user workflows
  So that I can validate the entire system works together

  @ui @api @db @smoke
  Scenario: Complete user creation workflow
    # Database: Verify initial state
    When I execute the query "SELECT COUNT(*) as initial_count FROM users"
    Then the query should return 1 row(s)
    And I extract column "initial_count" from first row and store as "initialUserCount"

    # API: Create user via API
    Given I set the API base URL
    And I set the authentication token
    And I set request body field "name" to "Hybrid Test User"
    And I set request body field "email" to "hybrid@test.com"
    And I set request body field "age" to number 25
    And I set request body field "active" to boolean true
    When I send a POST request to "/users"
    Then the response status code should be 201
    And the response field "name" should be "Hybrid Test User"
    When I extract "id" from response and store as "apiCreatedUserId"

    # Database: Verify user was created in database
    When I execute the query "SELECT * FROM users WHERE email = 'hybrid@test.com'"
    Then the query should return 1 row(s)
    And the column "name" in first row should be "Hybrid Test User"
    And the column "email" in first row should be "hybrid@test.com"
    And the column "age" in first row should be number 25
    And the column "active" in first row should be number 1

    # UI: Login and verify user appears in UI
    Given I navigate to the application URL
    And I am on the login page
    When I login with credentials "admin" and "password123"
    Then the current URL should contain "dashboard"
    # Additional UI steps would go here to navigate to user list and verify the user

    # Database: Verify final count
    When I execute the query "SELECT COUNT(*) as final_count FROM users"
    Then the query should return 1 row(s)
    # The final count should be initial + 1

  @ui @api @db @workflow
  Scenario: User update workflow across all layers
    # Database: Create initial user
    Given I execute the update query:
      """
      INSERT INTO users (name, email, age, active, created_at)
      VALUES ('Original User', 'original@test.com', 20, 1, NOW())
      """
    When I execute the query "SELECT id FROM users WHERE email = 'original@test.com'"
    Then I extract column "id" from first row and store as "dbUserId"

    # API: Update user via API
    Given I set the API base URL
    And I set the authentication token
    And I use extracted "dbUserId" as path parameter "id"
    And I set request body field "name" to "Updated User"
    And I set request body field "age" to number 30
    When I send a PUT request to "/users/{id}"
    Then the response status code should be 200
    And the response field "name" should be "Updated User"
    And the response field "age" should be number 30

    # Database: Verify update in database
    When I execute the query "SELECT name, age FROM users WHERE email = 'original@test.com'"
    Then the query should return 1 row(s)
    And the column "name" in first row should be "Updated User"
    And the column "age" in first row should be number 30

    # UI: Verify update appears in UI (would require navigation to user details)
    Given I navigate to the application URL
    And I am on the login page
    When I login with credentials "admin" and "password123"
    Then the current URL should contain "dashboard"
    # Additional UI verification steps would go here

  @api @db @data-integrity
  Scenario: API and Database data consistency validation
    # API: Create user
    Given I set the API base URL
    And I set the authentication token
    And I set request body field "name" to "Consistency Test"
    And I set request body field "email" to "consistency@test.com"
    And I set request body field "age" to number 35
    When I send a POST request to "/users"
    Then the response status code should be 201
    When I extract "id" from response and store as "consistencyUserId"

    # Database: Verify API data matches database
    When I execute the query "SELECT * FROM users WHERE email = 'consistency@test.com'"
    Then the query should return 1 row(s)
    And the column "name" in first row should be "Consistency Test"
    And the column "age" in first row should be number 35

    # API: Retrieve same user
    Given I use extracted "consistencyUserId" as path parameter "id"
    When I send a GET request to "/users/{id}"
    Then the response status code should be 200
    And the response field "name" should be "Consistency Test"
    And the response field "email" should be "consistency@test.com"
    And the response field "age" should be number 35

    # Database: Update directly in database
    When I execute the update query "UPDATE users SET age = 40 WHERE email = 'consistency@test.com'"
    Then the update should affect 1 row(s)

    # API: Verify API reflects database change
    When I send a GET request to "/users/{id}"
    Then the response status code should be 200
    And the response field "age" should be number 40

  @ui @db @user-journey
  Scenario: User login with database validation
    # Database: Create test user
    Given I execute the update query:
      """
      INSERT INTO users (name, email, password, active, created_at)
      VALUES ('Login Test User', 'logintest@example.com', 'hashedpassword123', 1, NOW())
      """

    # Database: Verify user exists
    Then a record should exist in table "users" where "email = 'logintest@example.com'"

    # UI: Attempt login
    Given I navigate to the application URL
    And I am on the login page
    When I login with credentials "logintest@example.com" and "password123"
    # Note: In real scenario, login would succeed and we'd verify dashboard

    # Database: Log the login attempt (if application logs to database)
    # This would verify that the UI action triggered the expected database changes

  @ui @api @db @cleanup
  Scenario: Comprehensive data cleanup across all layers
    # Database: Insert test data
    Given I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Cleanup Test', 'cleanup@test.com', 25, 1)"

    # API: Verify data exists via API
    Given I set the API base URL
    And I set the authentication token
    And I set query parameter "email" to "cleanup@test.com"
    When I send a GET request to "/users/search"
    Then the response status code should be 200

    # Database: Delete via database
    When I execute the update query "DELETE FROM users WHERE email = 'cleanup@test.com'"
    Then the update should affect 1 row(s)

    # API: Verify deletion via API
    When I send a GET request to "/users/search"
    Then the response status code should be 200
    # Response should show no results for the deleted user

    # Database: Verify deletion
    Then no record should exist in table "users" where "email = 'cleanup@test.com'"

  @ui @api @db @performance
  Scenario: Performance validation across layers
    # Database: Create multiple test users
    Given I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Perf User 1', 'perf1@test.com', 25, 1)"
    And I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Perf User 2', 'perf2@test.com', 30, 1)"
    And I execute the update query "INSERT INTO users (name, email, age, active) VALUES ('Perf User 3', 'perf3@test.com', 35, 1)"

    # API: Test response time
    Given I set the API base URL
    And I set the authentication token
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response time should be less than 2000 milliseconds

    # Database: Test query performance
    When I execute the query "SELECT COUNT(*) as count FROM users WHERE email LIKE 'perf%@test.com'"
    Then the query should return 1 row(s)
    And the column "count" in first row should be number 3

    # Cleanup
    When I execute the update query "DELETE FROM users WHERE email LIKE 'perf%@test.com'"
    Then the update should affect 3 row(s)

  @ui @api @db @error-handling
  Scenario: Error handling across all layers
    # API: Try to create user with invalid data
    Given I set the API base URL
    And I set the authentication token
    And I set request body field "name" to ""
    And I set request body field "email" to "invalid-email"
    When I send a POST request to "/users"
    Then the response status code should be 400

    # Database: Verify no invalid data was inserted
    Then no record should exist in table "users" where "email = 'invalid-email'"

    # UI: Test error handling in login
    Given I navigate to the application URL
    And I am on the login page
    When I login with credentials "nonexistent" and "wrongpassword"
    Then I should see the error message "Invalid username or password"

    # Database: Verify no session was created for invalid login
    # This would check session or audit tables if they exist