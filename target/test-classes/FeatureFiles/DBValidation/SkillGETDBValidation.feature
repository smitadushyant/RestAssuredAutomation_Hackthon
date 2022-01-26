#Author :Smita Chougale
Feature: DB validation for Skill_API Get request response

Scenario: Validate response of Skill_GET request with DB
Given  Expected value is queried from DataBase
##(here primary key user_id will get as actual value)

When GET request is sent to get actual response code
##(here  user_id from JSON response will get as expected  value)

Then Compare and Assert actual value and expected value