Feature: DB validation for API Put request 

Scenario: Validate PUT request of existing user with valid username
Given  User details are modified with username for existing user with Put request
When Modified user with valid username details are queried from DB With creation and updation times
Then DB details are compared with updated username details and asserted

Scenario: Validate PUT request of existing user with alphanumeric name location and comments
Given  User details are modified with alphanumeric name location and comments for existing user with Put request
When Modified user details with alphanumeric name location and comments are queried from DB With creation and updation times
Then DB details are compared with alphanumeric name location and comments details and asserted

Scenario: Validate PUT request of existing user with timezone and linkedin
Given  User details are modified with timezone and linkedin for existing user with Put request
When Modified user details with timezone and linkedin are queried from DB With creation and updation times
Then DB details are compared with timezone and linkedin details and asserted


