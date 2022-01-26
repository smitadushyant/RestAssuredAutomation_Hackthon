
Feature: Testing LMS_UserSkillMap API PUT request

Scenario: Test UserSkillMap API PUT request for UserSkillMap API
Given User is on PUT request with endpoint /url/UserSkills/userskillid for UserSkillMap API for UserSkillMap API
When User sends input with valid userskillid and valid JSON body from valid excel for UserSkillMap to update record for UserSkillMap API
Then User recieves a 201 valid status code for UserSkillMap API 
And valid json response for UserSkillMap API

When User sends input with non-existing userskillid to update record for UserSkillMap API
Then User recieves request status code 404 Not found for UserSkillMap API

When User sends input with invalid userskillid to update record for UserSkillMap API
Then User recieves a bad request status code 400 for UserSkillMap API

When User sends input with valid userskillid and valid months of experience to update record for UserSkillMap API
Then User recieves a 201 valid status code for UserSkillMap API
And  valid json response for UserSkillMap API
  
When User sends input with valid userskillid and invalid months of experience to update record for UserSkillMap API
Then User recieves a bad request status code 400 for UserSkillMap API