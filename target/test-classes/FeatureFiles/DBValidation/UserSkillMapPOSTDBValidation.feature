Feature: DB validation for UserSkillMap Post request
Scenario: Validate post request with DB
Given  new user is mapped with skill with post request
When DB is queried for new mapped user
Then Response and DB query results are asserted