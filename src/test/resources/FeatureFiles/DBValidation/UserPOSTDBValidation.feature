
Feature: DB validation for User API Post request 

Scenario: Validate POST request of new user with DB
Given  New user is added with Post request
When New user is queried from DB  
Then Compare and Assert request sent and DB user details

