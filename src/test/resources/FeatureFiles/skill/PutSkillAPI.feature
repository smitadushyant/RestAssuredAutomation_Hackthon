@PUT
Feature: Put Skill API
 Verify different Put scenarios for the SkillAPI 
@Smoke
Scenario: To update skill in LMS system using SkillAPI
Given I have the new skill with the body and endpoint as "/Skills"
	|skill_name|
	|allure |
When I hit the PUT Api request to update the skill details with endpoint "/Skills"
	|skill_name|
	|SAP HANAS|
Then I recieve a 201 valid status code
And API should return "message_response" in responsebody is "Successfully Updated !!"
And Perform the get request to the skill with endpoint "/Skills"
Then I Should see skill name as "SAP HCM" 

# Negative testing - Valid data 
@Negative
Scenario: Put request to validate erroe message when try to update the sill with existing skillname
Given I set the header with the body 
 |skill_name|
 |SAP HCM|   #Skill name already exists
When I hit the PUT Api request to update the skill details with endpoint "/Skills" and <skill_id>
	|skill_id|
	|3|
Then I recieve a 400 valid status code
And API should return "message" in responsebody is "Failed to update existing Skill details as Skill already exists !!"



#Negative Testing - Invalid data 
@Negative 
Scenario: Put request to validate error message for invalid test data with body empty
Given I set the header with the body 
	 |skill_name|
	 ||
When I hit the PUT Api request to update the skill details with endpoint "/Skills" and <skill_id>
		|skill_id|
		|3|
Then I recieve a 400 valid status code
And API should return "message" in responsebody is "{skill_name=Skill name is required !!}"
 
@Negative
Scenario: Put request to validate error message for invalid test data with spcial characters
Given I set the header with the body 
	 |skill_name|
	 |sa-**|
When I hit the PUT Api request to update the skill details with endpoint "/Skills" and <skill_id>
		|skill_id|
		|3|
Then I recieve a 400 valid status code
And API should return "message" in responsebody is "Failed to update existing Skill details as Skill Name contains special characters !!" 



