package stepDefinition.DBValidation;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.Assert;

import utils.DBRestConnect;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSenderOptions;
import io.restassured.specification.RequestSpecification;
public class UserPUTDBValidation {
	
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_userid = null;
	String sqlString_location = null;
	String sqlString_name = null;
	String sqlString_comments = null;
	String sqlString_timezone = null;
	String sqlString_linkedin = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	String RequestString1_updated = null;
	String RequestString2_updated = null;
	String RequestString3_updated = null;
	String RequestString4_updated = null;
	String RequestString5_updated = null;
	String userid = "U43";
	
	private RequestSpecification request;
	//private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}
	

	@Given("User details are modified with username for existing user with Put request")
	public void user_details_are_modified_with_username_for_existing_user_with_put_request() {
		setupRestAssured();
		
		//	HashMap<String, String> map = new HashMap<String, String>();
			JSONObject requestparams = new JSONObject();
			requestparams.put("comments", "putreq");
			requestparams.put("education_pg", " Information Technology");
			requestparams.put("education_ug", "Masters");
			requestparams.put("linkedin_url", "https://www.linkedin.com/in/JoBell/");
			requestparams.put("location", "Chicago");
			requestparams.put("name", "matt,todd");
			requestparams.put("phone_number", "5785984039");
			requestparams.put("time_zone", "PST");
			requestparams.put("visa_status", "H1B");
			request.header("Content-Type", "application/json");
			request.body(requestparams.toJSONString());
			System.out.println("PUT request payload is : " + requestparams.toJSONString());
			Response response = request.put("/Users/"+userid);
			
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, "201");
			
			JsonPath jsonPathEvaluator = response.jsonPath();
			RequestString_userid = jsonPathEvaluator.get("user_id");
			RequestString_message = jsonPathEvaluator.get("message_response");
			RequestString1_updated = jsonPathEvaluator.get("name");
			
			
			System.out.println("Json element value in response = " + RequestString1_updated);
			System.out.println("Json element value in response = " + RequestString_message);
			System.out.println("Json element value in response = " + RequestString_userid);
			
		}

	

	@When("Modified user with valid username details are queried from DB With creation and updation times")
	public void modified_user_with_valid_username_details_are_queried_from_db_with_creation_and_updation_times() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "last_mod_time");
			sqlString_name = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_location");
		
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_name);
		
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("DB details are compared with updated username details and asserted")
	public void db_details_are_compared_with_updated_username_details_and_asserted() {
		Assert.assertEquals(sqlString_name.trim(), RequestString1_updated.trim());
	}

	@Given("User details are modified with alphanumeric name location and comments for existing user with Put request")
	public void user_details_are_modified_with_alphanumeric_name_location_and_comments_for_existing_user_with_put_request() {
		setupRestAssured();
		
		//	HashMap<String, String> map = new HashMap<String, String>();
			JSONObject requestparams = new JSONObject();
			requestparams.put("comments", "updatedcomments");
			requestparams.put("education_pg", " Information Technology");
			requestparams.put("education_ug", "Masters");
			requestparams.put("linkedin_url", "https://www.linkedin.com/in/JoBell/");
			requestparams.put("location", "Chicago");
			requestparams.put("name", "matt5466,todd");
			requestparams.put("phone_number", "5785984039");
			requestparams.put("time_zone", "PST");
			requestparams.put("visa_status", "H1B");
			request.header("Content-Type", "application/json");
			request.body(requestparams.toJSONString());
			System.out.println("PUT request payload is : " + requestparams.toJSONString());
			Response response = request.put("/Users/"+userid);
			
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, "201");
			
			JsonPath jsonPathEvaluator = response.jsonPath();
			RequestString_userid = jsonPathEvaluator.get("user_id");
			RequestString_message = jsonPathEvaluator.get("message_response");
			RequestString1_updated = jsonPathEvaluator.get("name");
			RequestString2_updated = jsonPathEvaluator.get("location");
			RequestString3_updated = jsonPathEvaluator.get("comments");
			System.out.println("Json element value in response = " + RequestString1_updated);
			System.out.println("Json element value in response = " + RequestString2_updated);
			System.out.println("Json element value in response = " + RequestString3_updated);
			System.out.println("Json element value in response = " + RequestString_message);
			System.out.println("Json element value in response = " + RequestString_userid);
			
		}
	

	@When("Modified user details with alphanumeric name location and comments are queried from DB With creation and updation times")
	public void modified_user_details_with_alphanumeric_name_location_and_comments_are_queried_from_db_with_creation_and_updation_times() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "last_mod_time");
			sqlString_name = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_name");
			sqlString_location = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_location");
			sqlString_comments = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_comments");
		
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_name);
			System.out.println("received DB value sqlString_userid = " + sqlString_location);
			System.out.println("received DB value sqlString_userid = " + sqlString_comments);
		
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("DB details are compared with alphanumeric name location and comments details and asserted")
	public void db_details_are_compared_with_alphanumeric_name_location_and_comments_details_and_asserted() {
		Assert.assertEquals(sqlString_name.trim(), RequestString1_updated.trim());
		Assert.assertEquals(sqlString_location.trim(), RequestString2_updated.trim());
		Assert.assertEquals(sqlString_comments.trim(), RequestString3_updated.trim());
	}

	@Given("User details are modified with timezone and linkedin for existing user with Put request")
	public void user_details_are_modified_with_timezone_and_linkedin_for_existing_user_with_put_request() {
setupRestAssured();
		
		//	HashMap<String, String> map = new HashMap<String, String>();
			JSONObject requestparams = new JSONObject();
			requestparams.put("comments", "updatedcomments");
			requestparams.put("education_pg", " Information Technology");
			requestparams.put("education_ug", "Masters");
			requestparams.put("linkedin_url", "https://www.linkedin.com/in/matttodd/");
			requestparams.put("location", "Chicago");
			requestparams.put("name", "matt5466,todd");
			requestparams.put("phone_number", "5785984039");
			requestparams.put("time_zone", "CST");
			requestparams.put("visa_status", "H1B");
			request.header("Content-Type", "application/json");
			request.body(requestparams.toJSONString());
			System.out.println("PUT request payload is : " + requestparams.toJSONString());
			Response response = request.put("/Users/"+userid);
			
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, "201");
			
			JsonPath jsonPathEvaluator = response.jsonPath();
			RequestString_userid = jsonPathEvaluator.get("user_id");
			RequestString_message = jsonPathEvaluator.get("message_response");
			RequestString1_updated = jsonPathEvaluator.get("time_zone");
			RequestString2_updated = jsonPathEvaluator.get("linkedin_url");
			
			System.out.println("Json element value in response = " + RequestString1_updated);
			System.out.println("Json element value in response = " + RequestString2_updated);
			
			System.out.println("Json element value in response = " + RequestString_message);
			System.out.println("Json element value in response = " + RequestString_userid);
			
		}
	

	@When("Modified user details with timezone and linkedin are queried from DB With creation and updation times")
	public void modified_user_details_with_timezone_and_linkedin_are_queried_from_db_with_creation_and_updation_times() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "last_mod_time");
			sqlString_timezone = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_time_zone");
			sqlString_linkedin = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "user_linkedin_url");
			
		
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_timezone = " + sqlString_timezone);
			System.out.println("received DB value sqlString_linkedin = " + sqlString_linkedin);
			
		
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("DB details are compared with timezone and linkedin details and asserted")
	public void db_details_are_compared_with_timezone_and_linkedin_details_and_asserted() {
		Assert.assertEquals(sqlString_timezone.trim(), RequestString1_updated.trim());
		Assert.assertEquals(sqlString_linkedin.trim(), RequestString2_updated.trim());
	}

}
