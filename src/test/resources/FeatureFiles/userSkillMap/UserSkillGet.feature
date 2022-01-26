Feature: USER SKILL API when User logged in as APIPROCESSING with Basic Auth

#For G1
	Scenario: To get all the Users with their Skill details
	Given User is on GET Method with Endpoint as /UserSkills
	When User sends request for UserSkillMap API
	Then User receives 200 OK status code for UserSkillMap API
	And JSON schema is valid for UserSkillMap API for all record

#For G2

Scenario Outline: To fetch the data with userSkillID
Given User is on GET Method with Endpoint as /UserSkills/id
When User sends request with Valid userSkillId
Then JSON schema is valid for UserSkillMap API
And User receives 200 OK status code for UserSkillMap API


# For G3

#Scenario Outline: To get user record for non-existing userSkillID 
 #Given user set GET method with endpoint/url/Userskills/ID
  When User sends the request with non-existing userSkillID for UserSkillMap API
  Then User receives status code as 404 for UserSkillMap API
  
 #For G5
#Scenario Outline: To get user record for Invalid userID 
  #Given user set GET method with endpoint/url/Userskills/ID
  When User sends the request with Invalid userSkillID for UserSkillMap API
  Then User receives status code as 404 for UserSkillMap API
  
  #For G4
#Scenario Outline: To get user record for blank userID 
  #Given user set GET method with endpoint/url/Userskills/ID
  When User sends the request with blank userSkillID for UserSkillMap API
  Then User receives status code 404 for UserSkillMap API
  
  #For G6
#Scenario Outline: To get user record for decimal userID 
  #Given user set GET method with endpoint/url/Userskills/ID
  When User sends the request with decimal userSkillID for UserSkillMap API
  Then User receives status code as 404 for UserSkillMap API
 
  #For G21
	Scenario: Retrieve all users and all skills
	Given User sets GET request with a valid endpoint as /UserSkillsMap
	When User sends a GET HTTP request for UserSkillMap all
	Then User receives 200 OK status code for UserSkillMap all
	And JSON schema is valid for UserSkillMap all

  #For G22
  Scenario Outline: Retreive particular user and skills
  Given user set GET method with endpoint/url/UserskillsMap/userId
  When User sends the request with userId for UserSkillMap 
  Then JSON schema is valid for UserSkillMap API getting by userId
  And User receives status code 200 for UserSkillMap API getting by userId
  
  #For G23
  #Scenario Outline: Retreive particular user and skills
  #Given user set GET method with endpoint/url/UserskillsMap/userId
  When User sends the request with a non-existing userId for UserSkillMap 
  Then User receives status code 404 for UserSkillMap by non-existing userId
  
  #For G24
  	Scenario Outline: Retrieve users of particular skill id
   Given user set GET method with endpoint/url/UserskillsMap/skillId
   When User sends the request with skillId for UserSkillMap 
   Then JSON schema is valid for UserSkillMap API getting by skillId
  And User receives status code 200 for UserSkillMap API getting by skillId
  
  #For G25
  #Scenario Outline: Retrieve users of particular skill id
   #Given user set GET method with endpoint/url/UserskillsMap/skillId
  When User sends the request with a non-existing skillId for UserSkillMap 
   Then User receives status code 404 for UserSkillMap API by non-existing skillId
  
  