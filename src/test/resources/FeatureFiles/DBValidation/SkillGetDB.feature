Feature: DB validation for Skill_API Get request response

Scenario: Validate response of Skill_GET request with DB
Given  Expected value is queried from DataBase
When GET request is sent to get actual response code
Then Compare and Assert actual value and expected value