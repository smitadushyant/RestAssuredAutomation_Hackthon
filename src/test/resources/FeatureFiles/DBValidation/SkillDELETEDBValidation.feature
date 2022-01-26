
Feature: DB validation for SillAPI Delete request 

Scenario: Validate DELETE request of existing skill
Given  Skill is deleted with DELETE request 
When Query the DataBase with user skillid
Then Request response message and DataBase response are asserted