Feature: JSONPlaceholder API Testing
  As a developer
  I want to test the JSONPlaceholder API endpoints
  So that I can verify the API functionality

  Background:
    Given I have access to the JSONPlaceholder API

  @smoke @api
  Scenario: Get All Posts
    When I send a GET request to "/posts"
    Then the response status should be 200
    And the response should contain posts data

  @api
  Scenario: Get Specific Post
    When I send a GET request to "/posts/1"
    Then the response status should be 200
    And the response should contain post with ID 1
    And the post should have a title
    And the post should have a body

  @api
  Scenario: Create New Post
    When I send a POST request to "/posts" with the following data:
      | title       | body           | userId |
      | Test Post   | Test Body      | 1      |
    Then the response status should be 201
    And the response should contain the created post data

  @api
  Scenario: Update Existing Post
    When I send a PUT request to "/posts/1" with the following data:
      | title           | body             | userId |
      | Updated Post    | Updated Body     | 1      |
    Then the response status should be 200
    And the response should contain the updated post data

  @api
  Scenario: Delete Post
    When I send a DELETE request to "/posts/1"
    Then the response status should be 200

  @api
  Scenario: API Response Time Test
    When I send a GET request to "/posts"
    Then the response time should be less than 5 seconds

  @api
  Scenario: API Error Handling
    When I send a GET request to "/posts/999999"
    Then the response status should be 404

  @api
  Scenario: API with Custom Headers
    When I send a GET request to "/posts" with custom headers
    Then the response status should be 200
    And the response should have JSON content type

  @api
  Scenario Outline: API Testing with Different Post IDs
    When I send a GET request to "/posts/<post_id>"
    Then the response status should be <expected_status>
    And the response should contain post data

    Examples:
      | post_id | expected_status |
      | 1       | 200            |
      | 5       | 200            |
      | 10      | 200            |
      | 999999  | 404            |