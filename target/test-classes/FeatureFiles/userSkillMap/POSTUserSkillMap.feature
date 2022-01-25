
Feature: Testing LMS_UserSkillMap API POST request

Scenario: To map new user and skill  
Given User is on Post request with endpoint /url/UserSkills
When User sends input with valid JSON body from valid excel for UserSkillMap
Then User recieves a 201 valid status code
And valid json response

When User sends input with alphanumeric skillid 
Then User recieves a bad request status code 400

When User sends input with null skillid 
Then User recieves a bad request status code 400

When User sends input with null userid
Then User recieves a bad request status code 400
  
When User sends input with alphanumeric months of experience 
Then User recieves a bad request status code 400

When User sends input with null months of experience
Then User recieves a bad request status code 400