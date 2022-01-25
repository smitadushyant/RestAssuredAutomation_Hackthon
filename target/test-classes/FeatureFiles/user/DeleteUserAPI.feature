
#Author: Smita Chougale
#Background: Test UsersAPI DELETE Methods 

Feature: DELETE USER API when User logged in as APIPROCESSING with Basic Auth

Scenario Outline: To delete record for existing userId
    Given Set DELETE request endpoint to \Users\userid
    When User sends request with valid userId
    Then User should receive a valid response code 200
    
    When User sends request with blank userId
    Then response with 405 Method Not Allowed status code

    When User sends request with non-existing userId
    Then response with error 404 Not found status code

    When User sends request with invalid userId
    Then response with error 404 Not found status code

