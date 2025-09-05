Feature: Employee API with Auth

  Scenario: Search employee with authentication
    Given I have valid credentials username "user1" and password "pass123"
    When I search for employee with id "45177077"
    Then the response status code should be 200
    And the employee first name should be "TEST"
