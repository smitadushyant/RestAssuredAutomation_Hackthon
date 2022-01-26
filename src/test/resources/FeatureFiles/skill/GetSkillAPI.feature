@GET
Feature: Get Skill API
Verify different get scenarios for the SkillAPI 

@Smoke
Scenario: Get request to fetch all skills
When I perform GET operation for "/Skills"
Then API should return all the skills
Then I recieve a 200 valid status code
    
@Smoke
Scenario: Get request to fetch single skill
When I perform GET operation for "/Skills" with <skill_id>
   	|skill_id|
   	|20|
Then  API should return the details of skill id 20
Then I recieve a 200 valid status code
   	
#Negative Scenarios -- valid input
  
@Negative
Scenario: Get request to fecth the non existance of a SkillId
When  I perform GET operation for "/Skills" with <skill_id>
  	|skill_id|
   	|2032|
   	|0|
Then I recieve a 404 valid status code
  	
#Negative scenarios -- Invalid Input  
     
@Negative
Scenario Outline: Get request to fetch the skill for the Invalid skillId 
When  I perform GET operation for "/Skills" with <skill_id>
Then I recieve a 400 valid status code
   Examples: 
   | skill_id | 
   | "abc123" |
   | 2.2 |
   | "null" |
      
  
  