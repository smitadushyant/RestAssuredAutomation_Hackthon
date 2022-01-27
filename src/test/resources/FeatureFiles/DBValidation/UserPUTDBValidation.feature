Feature: DB validation for API Put request 

Scenario: Validate PUT request of existing user with valid username
Given  User details are modified with username for existing user with Put request
When Modified user with valid username details are queried from DB With creation and updation times
Then DB details are compared with updated username details and asserted

