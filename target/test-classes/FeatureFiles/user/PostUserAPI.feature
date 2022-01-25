Feature: Testing LMS_User API POST request

Scenario: User creates new record in POST method 
Given User is on Post request 
When User sends input with valid JSON body from valid excel 
Then User recieves a 201 valid status code
And "message_response" in responsebody is "Successfully Created !!"

#Scenario: To create new user record with blank field
#Given User is on Post request 
When User sends input with blank Linkedin_url 
Then User recieves a valid status code 201

#Scenario: To create new user record with blank field
#Given User is on Post request 
When User sends input with blank education_ug,education_pg field and comments
Then User recieves a valid status code 201

#Scenario: To create  new user record with alphanumeric name and location
#Given User is on Post request
When User sends input with alphanumeric name 
Then User receive 400 bad request error response
     
#Scenario: To create new user record with any blank field
#Given User is on Post request 
When User sends input with invalid blank field from excel 
Then User receive 400 bad request error response

#Scenario: To create new user record with only first name
#Given User is on Post request 
When User sends input with only first name from excel 
Then User receive 400 bad request error response
 
#Scenario: To create new user record with only Last name
#Given User is on Post request 
When User sends input with only last name from excel 
Then User receive 400 bad request error response

#Scenario: To create record with invalid linkedin_url
#Given User is on Post request 
When User sends input with invalid linkedin_url from excel
Then User receive 400 bad request error response

#Scenario: To create record with invalid timezone
#Given User is on Post request 
When User sends input with invalid timezone from excel 
Then User receive 400 bad request error response
    
#Scenario: To create record with invalid visa_status
#Given User is on Post request 
When User sends input with invalid visa_status from excel 
Then User receive 400 bad request error response
    
#Scenario: To create record with invalid phone_number
#Given User is on Post request 
When User sends input with invalid phone_number from execel
Then User receive 400 bad request error response



