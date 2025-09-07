Feature: Employee API with Auth

  Scenario: Search employee with authentication
    Given I have valid credentials username "user1" and password "pass123"
    When I search for employee with id "45177077"
    Then the response status code should be 200
    And the employee first name should be "TEST"


Scenario Outline: Search employee with different criteria
    Given I search for employee with using "<key>" with value "<value>"45177077"
    Then the response status code should be 200
    And the employee first name should be "<value>"
Examples:

|key|value|
|empid|45177077|
|crdnum|43789104|
|firstname|falaknaaz|
|lastname|inamdar|