@Delete
Feature: Delete Skill API
Verify different Delete scenarios for the SkillAPI

@Smoke
Scenario: To delete skill in LMS system using SkillAPI
Given I have the skill with the body and endpoint as "/Skills"
      | skill_name  |
      | Spring Boot |
When I hit the delete Api request to delete the skill details with endpoint "/Skills"
Then I recieve a 200 valid status code
And API should return "message_response" in responsebody is "The record has been deleted !!"
And Perform the get request to the skill with the endpoint "/Skills"
Then I recieve a 404 valid status code


#Negative Testing --valid Input
@Negative
Scenario: Delete request to validate error message when try to delete with non existing id
When I hit the delete Api request to update the skill details with endpoint  "/Skills" and non existing <skill_id>
      | skill_id |
      |      303 |
Then I recieve a 404 valid status code

#Negative Testing - Invalid Input

@Negative
Scenario: Delete request to validate error message when try to delete with blank id
When I hit the delete Api request to update the skill details with endpoint  "/Skills" and blank <skill_id>
      | skill_id |
      |          |
Then I recieve a 400 valid status code


@Negative
Scenario Outline: Delete request to validate error message when try to delete with alphanumeric id
When I hit the delete Api request to update the skill details with endpoint "/Skills" and alphanumeric id<skill_id>
      | skill_id |
      | 123sdf   |
Then I recieve a 400 valid status code


@Negative
Scenario: Delete request to validate error message when try to delete with specialcharacter
When I hit the delete Api request to update the skill details with endpoint  "/Skills" and specialcharacter <skill_id>
      | skill_id |
      |     3$     |
Then I recieve a 400 valid status code