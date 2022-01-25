Feature: Validate API user

Scenario: Basic auth
    Given the users endpoint exists
    When I send a in-valid userId and valid password as credentials
    Then response status code should be 401 unAuthotized
    
#Scenario: Basic auth
    #Given the users endpoint exists
    When I send a valid userId and in-valid password as credentials
    Then response status code should be 401 unAuthotized

#Scenario: Basic auth
    #Given the users endpoint exists
    When I send a valid login credentials
    Then response status code should be 200
