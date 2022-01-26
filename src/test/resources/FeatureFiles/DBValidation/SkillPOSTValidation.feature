#Author : Pallavi Nair
Feature: DB validation for API Post request 

Scenario: Validate POST request of new skill with DB
Given  New skill is added with Post request
When New skill is queried from DB  
Then Compare and Assert request sent and DB skill details