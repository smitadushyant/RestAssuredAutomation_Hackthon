Feature: DB validation for UserSkillMap Put request
Scenario: Validate put request with DB
Given  new months of exp is updated with put request for existing user skill id
When DB is queried for updated field 
Then Put Response and DB query results are asserted