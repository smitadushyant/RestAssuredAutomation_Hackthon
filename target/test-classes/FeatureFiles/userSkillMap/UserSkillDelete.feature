Feature: USER SKILL API when User logged in as APIPROCESSING with Basic Auth
#For G16
Scenario Outline: To delete record for existing  userSkillId
    Given Set Delete request endpoint to /UserSkills/id
    When User sends request with valid userSkillId for UserSkillMap Delete API
    Then User should receive a valid response code 200 for UserSkillMap Delete API
    
#For G17
#Scenario Outline: To delete record for non-existing userSkillId
    #Given  Set Delete request endpoint to /UserSkills/id
    When User sends request with non-existing userSkillId for UserSkillMap Delete API
    Then User should receive a response code 404 for UserSkillMap Delete API
    
#for G18

#Scenario Outline: To delete record for blank userSkillId
    #Given  Set Delete request endpoint to /UserSkills/id
    When User sends request with blank userSkillId for UserSkillMap Delete API
   Then User should receive 405 Method Not Allowed status code for UserSkillMap Delete API
    
    
 #For G19
 #Scenario Outline: To delete record for invalid userSkillId
    #Given  Set Delete request endpoint to /UserSkills/id
     When User sends request with invalid userSkillId for UserSkillMap Delete API
    Then User should receive a response code 404 for UserSkillMap Delete API
 
 #For G20
#Scenario Outline: To delete record for decimal userSkillId
     #Given  Set Delete request endpoint to /UserSkills/id
      When User sends request with decimal userSkillId for UserSkillMap Delete API
     Then User should receive a response code 404 for UserSkillMap Delete API
 