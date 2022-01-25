#Author: Smita Chougale
#Background: Test UsersAPI GET Methods 

Feature: Authorization is set to default:Inherit auth from Parent
 
Scenario:To get all the Users details
  Given user set GET method with endpoint/url/Users
  When User sends request 
  Then JSON schema is valid 
  And User receives status code 200
  And User should receive a list of all Users

Scenario Outline: To get user detail for specific User ID 
  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with specific userid
  Then JSON schema is valid for Users
  And User receives status code 200
  And User should receive a details of specific user ID 

Scenario Outline: To get user record for Invalid userID 
  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with invalid id
  Then User receives status code 404

Scenario Outline: To get user record for decimal userID 
  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with decimal id
  Then User receives status code 404

Scenario Outline: To get user record for alphanumeric userID 
  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with aplphnumeric id
  Then User receives status code 404
  
  Scenario Outline: To get user record for null userID 

  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with null id
  Then User receives status code 404
  
  Scenario Outline: To get user record for zero userID 

  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with zero id
  Then User receives status code 404
  
  Scenario Outline: To get user record for blank userID 

  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with blank id
  Then User receives status code 200
  
Scenario Outline: To get all the user detail for null User ID 

  Given user set GET method with endpoint/url/Users/ID
  When User sends the request with non existing id
  Then User receives status code 404


