
Feature: DB validation for API Post request 

Scenario: Validate POST request of new user with DB
Given  New user is added with Post request
When New user is queried from DB  
Then Compare and Assert request sent and DB user details

Scenario: Validate POST request with alphanumeric name and location with DB
Given  New user is added with Post request with alphanumeric name and location
When New user is queried with name and location fields
Then Compare and Assert name and location fields from DB and response

Scenario: Validate POST request with blank linkedin edu_pg and comments fields with DB
Given  New user is added with Post request blank linkedin edu_pg and comments fields
When New user is queried with linkedin edu_pg and comments fields
Then Compare and Assert linkedin edu_pg and comments fields from DB and response