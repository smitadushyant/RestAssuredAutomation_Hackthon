
Feature: DataBase validation for API Put request 
Scenario: Validate SkillPUT request of existing user
Given  New skill is added for existing skillid with Put request
When Modified details are queried from DataBase With creation and updation times
Then DataBase details are compared with request payload details and asserted