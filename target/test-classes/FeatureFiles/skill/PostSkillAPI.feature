@POST
Feature: Post Skill API
Verify different Post scenarios for the SkillAPI 
  
@Smoke 
Scenario: To create skill in LMS system using SkillAPI
Given I  am  on Post request 
When I send input with valid JSON body from  excel 
Then I recieve a 201 valid status code
And API should return "message_response" in responsebody is "Successfully Created !!"

#Negative Testing - valid input

@Negative
Scenario: Try to create an existing skill in LMS system using SkillAPI
Given I  am  on Post request 
When I send input with valid JSON body for the existing Skill  
Then I recieve a 400 valid status code
And API should return "message" in responsebody is "Failed to create new Skill details as Skill already exists !!"

@Negative
Scenario: Try to create skill with empty skill_name in LMS system using SkillAPI
Given I  am  on Post request 
When I send input with valid JSON body with empty skill_name field 
Then I recieve a 400 valid status code
And API should return "message" in responsebody is "{skill_name=Skill name is required !!}"