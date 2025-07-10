Feature: Login functionality

  @ui
  Scenario: Valid user can login
    Given user is on the login page
    When user logs in with username "test" and password "test123"
    Then home page should be displayed