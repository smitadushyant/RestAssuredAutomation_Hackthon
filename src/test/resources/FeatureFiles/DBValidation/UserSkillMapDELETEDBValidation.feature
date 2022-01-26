Feature: DB validation for UserSkillMap del request
Scenario: Validate delete request with DB
Given  existing user skill id is deleted with delete request
When DB is queried deleted field 
Then Delete Response and DB query results are asserted