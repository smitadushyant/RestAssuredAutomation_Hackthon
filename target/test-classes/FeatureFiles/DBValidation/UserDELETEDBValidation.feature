Feature: DB validation for API Delete request 

Scenario: Validate DELETE request of existing user
Given  User is deleted with DELETE request
When Query the DB with user id
Then Request response message and DB response are asserted