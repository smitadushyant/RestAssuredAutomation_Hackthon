Feature: Testing LMS_User API PUT request

Scenario: Check for PUT method
Given User is on the PUT Request  
When User updates the existing userID with all inputs from excel 
Then User receives an 201 status code and updates the existing record
And "message_response" in response_body of Put request is "Successfully Updated !!"
 
 
#Scenario: Update record with invalid alphanumeric inputs
#Given  User is on the PUT Request
When User sends input with alphanumeric username 
Then User receives 400 bad request status response for update

#Scenario: Update record with invalid phone number inputs
    #Given User is on the PUT Request
    When User sends input with invalid phone number  
    Then User receives 400 bad request status response for update
   
    
#Scenario: Update record with invalid Visa status inputs
    #Given User is on the PUT Request
    When User sends input with invalid visa status 
    Then User receives 400 bad request status response for update
    

#Scenario: Update record with valid with user inputs
    #Given User is on the PUT Request 
    When User sends input with alphanumeric location and comments inputs 
    Then User receives 201 as valid status code and updates the existing record