
Feature: Testing LMS_UserSkillMap API PUT request

Scenario: Test UserSkillMap API PUT request
Given User is on PUT request with endpoint /url/UserSkills/userskillid
When User sends input with valid userskillid and valid JSON body from valid excel for UserSkillMap to update record
Then User recieves a 201 valid status code
And valid json response

When User sends input with non-existing userskillid to update record
Then User recieves request status code 404 Not found

When User sends input with invalid userskillid to update record
Then User recieves a bad request status code 400

When User sends input with valid userskillid and valid months of experience to update record
Then User recieves a 201 valid status code
And  valid json response
  
When User sends input with valid userskillid and invalid months of experience to update record
Then User recieves a bad request status code 400