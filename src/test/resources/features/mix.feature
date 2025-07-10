Feature: Mixed scenario with API

  @api @ui
  Scenario: Validate health endpoint
    Given a GET request is sent to "/health"
    Then the API response code should be 200