
Feature: Testing LMS_UserSkillMap API POST request

Scenario: To map new user and skill for SkilllMap Post 
Given User is on Post request with endpoint /url/UserSkills for SkilllMap Post
When User sends input with valid JSON body from valid excel for SkilllMap Post
Then User recieves a 201 valid status code for SkilllMap Post
And valid json response for SkilllMap Post

When User sends input with alphanumeric skillid for SkilllMap Post
#Then User recieves a bad request status code 400 for SkilllMap Post

When User sends input with null skillid for SkilllMap Post
#Then User recieves a bad request status code 400 for SkilllMap Post

When User sends input with null userid for SkilllMap Post
#Then User recieves a bad request status code 400 for SkilllMap Post
  
When User sends input with alphanumeric months of experience for SkilllMap Post
#Then User recieves a bad request status code  400 for SkilllMap Post

When User sends input with null months of experience for SkilllMap Post
Then User recieves a bad request status code 400 for SkilllMap Post