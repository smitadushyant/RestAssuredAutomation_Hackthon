Feature: DB validation for API Get request response

Scenario: Validate response of GET request with DB

Given Expected value is queried from DB
When GET request is sent to get actual response 
Then Compare and Assert actual and expected values