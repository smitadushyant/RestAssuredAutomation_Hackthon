
Feature: DB validation for API Get request response

Scenario: Validate response of GET request with DB
Given  Expected value is queried from DB
##(here primary key user_id will get as actual value)

When GET request is sent to get actual response 
##(here  user_id from JSON response will get as expected  value)

Then Compare and Assert actual and expected values