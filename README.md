# RestAssuredAutomation_Hackthon
REST Assured API test automation of LMS preproduction  USER, Skill and Skill MAP API's

# LMS RESTAPI
LMS API REST Assured Automation Testing
# Prerequisites/Development Environment

# Following software should be installed in the system.
Java 8 & above
Maven 	
Postgres 14 
Eclipse IDE
# Application setup- To run LMS API service
Step 1: Run and setup Postgre
Step 2: Check Postgres DB(assuming Postgres DB setup is in place) connectivity with username=postgres, password=admin
Step 3: If your your DB not setup please follow LMSDB_RestorationGuide.docx and restore LMS_DB.tar.
Step 4: Run LMS API Jar file (LMS_API-0.0.1-SNAPSHOT.jar) 
Step 5: Download LMS_API-0.0.1-SNAPSHOT.jar
Step 6:  Run command prompt from source folder of jar file
C:/< path for jar folder> java -jar LMS_API-0.0.1-SNAPSHOT.jar

Step 7: To confirm LMS API service is up and running  open the POSTMAN/Swagger and test the application.
POSTMAN : https://springboot-lms-userskill.herokuapp.com/Users
Swagger: https://springboot-lms-userskill.herokuapp.com/swagger-ui.html
# To Run the REST assured Automation from Eclipse:

 Step 1: Follow steps 1- 7 as mentioned in the above section.
Step 2 : Download code from Git, import into eclipse as Maven project and build the project so automatically install all needed dependencies.
Step 2A: Run the TestRunner file for Positive Scenarios (smoke cases) 
Step 2B: Run the TestRunner file for Negative Scenarios
Step 3 : Find reports under Reports folder in project

# Assumptions

We have two sequences 
user_seq for USER_ID of TBL_LMS_USER
Format – U{XX}, initialized with 01

user_skill_seq for USER_SKILL_ID of TBL_LMS_USERSKILL_MAP
Format – US{XX}, initialized with 01

***** Above these two customized sequences are using spring boot SequenceStyleGenerator.
***** It is advisable that the related tables should be empty, and the sequences values should be generated from 01. If some records are already present in the table, then execution will throw primary key violation constraint exception.
So in order to resolve the issue sequence need to be reset to the (max+1) value for that primary key column.
Below sqls need to be executed multiple times until respective sequences equal to the max value of that respective primary key column.
select nextval ('user_seq')
For example, let us assume that USER table has already 5 records and max value of USER_ID is ‘U05’. So, the above sql needs to be executed manually 5 times. So next time if your run the application spring boot will insert ‘U06’ value to USER_ID.

select nextval (' user_skill_seq ')
Same logic for this sequence also.

# Database used
Postgres database version 14
Tables used:
TBL_LMS_USER

TBL_LMS_SKILL_MASTER

TBL_LMS_USERSKILL_MAP



# Modules & Endpoints

Modify Excel Data according to mentioned endpoint data body specifications. 
User API : 
GET – Fetch all user data from TBL_LMS_USER table.
/Users 

GET – Fetch user data by USER_ID from TBL_LMS_USER table.
/Users/{id}

POST – Insert a new User detail into TBL_LMS_USER table.
/Users 
Request Body
{
	"name":"Baisali,Sadhukhan",
	"phone_number":9123467545,
	"location":"Pittsburgh",
	"time_zone":"EST",
	"linkedin_url":"www.linkedin.com/in/BaisaliSadhukhan",
	"education_ug":"UG",
	"education_pg":"PG",
	"visa_status":"H4",
	"comments":"Through Post"
}
Response Body
{
    	"user_id": {Auto-Generated},
   	 "name":"Baisali,Sadhukhan",
	"phone_number":9123467545,
	"location":"Pittsburgh",
	"time_zone":"EST",
	"linkedin_url":"www.linkedin.com/in/BaisaliSadhukhan",
	"education_ug":"UG",
	"education_pg":"PG",
	"visa_status":"H4",
	"comments":"Through Post"
	"message_response": "Successfully Created !!"
}

PUT – Update an existing User detail in TBL_LMS_USER table.
/Users/{id}
Request URL - /Users/U25
Request Body
{
    "user_id": "U25",
    "name": "Mounika,Badola",
    "phone_number": 9854658345,
    "location": "Pittsburgh",
    "time_zone": "EST",
    "linkedin_url": "www.linkedin.com/in",
    "education_ug": "UG",
    "education_pg": "PG",
    "visa_status": "H4",
    "comments": "Through Post"
}
Response Body
{
    "user_id": "U25",
    "name": "Mounika,Badola",
    "phone_number": 9854658345,
    "location": "Pittsburgh",
    "time_zone": "EST",
    "linkedin_url": "https://www.linkedin.com/in/MounikaBadola/",
    "education_ug": "UG",
    "education_pg": "PG",
    "visa_status": "H4",
    "comments": "Through Post",
    "message_response": "Successfully Updated !!"
}

DELETE – Delete an existing User detail from TBL_LMS_USER table.
/Users/{id}
Request URL - /Users/U05
Response Body
{
    "user_ id": "U05",
    "message_response": "The record has been deleted !!"
}



Skill API
GET – Fetch all skill data from TBL_LMS_SKILL_MASTER table.
/Skills

GET – Fetch skill data by SKILL_ID from TBL_LMS_USER table.
/ Skills/{id}

POST – Insert a new Skill detail into TBL_LMS_USER table.
/Skills
Request Body
{
    "skill_name": "Springg"
}

Response Body
{
    "skill_id": 3,
    "skill_name": "Springg",
    "message_response": "Successfully Created !!"
}

PUT – Update an existing Skill detail in TBL_LMS_USER table.
/Skills/{id}
Request URL - /Skills/3
Request Body
{
    "skill_id": 3,
    "skill_name": "Spring"
}

Response Body
{
    "skill_id": 3,
    "skill_name": "Spring",
    "message_response": "Successfully Updated !!"
}

DELETE – Delete an existing Skill detail from TBL_LMS_USER table.
/Skills/{id}
Request URL - /Skills/3
Response Body
{
    "message_response": "The record has been deleted !!",
    "Skill_Id": "3"
}

UserSkillMap API
GET – Fetch all user-skill data from TBL_LMS_USERSKILL_MAP table.
/UserSkills

GET – Fetch user-skill data by USER_SKILL_ID from TBL_LMS_USER table.
/UserSkills/{id}

POST – Insert a new User-Skill mapping detail into TBL_LMS_USER table.
/UserSkills 
Request Body
{
  "user_id": "U02",
  "skill_id": 2,
  "months_of_exp": 12
}

Response Body
{
    "user_skill_id": "US55",
    "user_id": "U02",
    "skill_id": 2,
    "months_of_exp": 12,
    "message_response": "Successfully Created !!"
}


PUT – Update an existing User-Skill mapping detail in TBL_LMS_USER table.
/UserSkills/{id}
Request URL - /UserSkills/US55
Request Body
{
  "user_skill_id": "US55",
  "user_id": "U02",
  "skill_id": 2,
  "months_of_exp": 18
}

Response Body
{
    "user_skill_id": "US55",
    "user_id": "U02",
    "skill_id": 2,
    "months_of_exp": 18,
    "message_response": "Successfully Updated !!"
}

DELETE – Delete an existing User-Skill mapping detail from TBL_LMS_USER table.
/UserSkills/{id}
Request URL - /UserSkills/US55
Response Body
{
    "user_skill_id": "US55",
    "message_response": "The record has been deleted !!"
}


UserSkillMapGetAPI
GET – List all users with all skill details.
/UserSkillsMap 

GET – List user with the skill details by USER_ID.
/UserSkillsMap/{userId}
Request URL – 
Response Body
{
    "users": {
        "id": "U23",
        "firstName": "Baisali",
        "lastName": "Sadhukhan",
        "skillmap": [
            {
                "id": 30,
                "skill": "PostMan"
            },
            {
                "id": 28,
                "skill": "SpringBoot"
            },
            {
                "id": 29,
                "skill": "Java"
            }
        ]
    }
}


GET  – List all users details by SKILL_ID.
/UserSkillsMap/{skillId}
