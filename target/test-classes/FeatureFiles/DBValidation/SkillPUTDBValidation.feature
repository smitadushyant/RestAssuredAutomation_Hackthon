
Feature: DataBase validation for API Put request 
Scenario: Validate SkillPUT request of existing user
Given  User details are modified for existing skill with Put request
When Modified user details are queried from DataBase With creation and updation times
Then DataBase details are compared with request payload details and asserted