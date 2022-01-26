package stepDefinition.DBValidation;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.Assert;
import utils.DBRestConnect;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSenderOptions;
import io.restassured.specification.RequestSpecification;


public class UserPOSTDBValidation {
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_userid = null;
	String sqlString_name = null;
	String sqlString_location = null;
	String sqlString_linkedin = null;
	String sqlString_edupg = null;
	String sqlString_comments = null;
	String RequestString_userid= null;
	String RequestString_message = null;
	String RequestString_name = null;
	String RequestString_location = null;
	String RequestString_linkedin = null;
	String RequestString_edupg = null;
	String RequestString_comments = null;
	
	private RequestSpecification request;
	
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}


	@Given("New user is added with Post request")
	public void new_user_is_added_with_post_request() {
		setupRestAssured();

		JSONObject requestparams = new JSONObject();
		requestparams.put("comments", "postreq");
		requestparams.put("education_pg", " Information Technology");
		requestparams.put("education_ug", "Masters");
		requestparams.put("linkedin_url", "https://www.linkedin.com/in/John Bell/");
		requestparams.put("location", "NewJersy");
		requestparams.put("name", "John,Bell");
		requestparams.put("phone_number", "5485984039");
		requestparams.put("time_zone", "EST");
		requestparams.put("visa_status", "H1B");
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Post request payload is : " + requestparams.toString());
		Response response = request.post("/Users");

		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		//ResponseBody body = response.getBody();


		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userid = jsonPathEvaluator.get("user_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_userid);
		System.out.println("Json element value in response = " + RequestString_message);

	}

	@When("New user is queried from DB")
	public void new_user_is_queried_from_db() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='John'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='John'", "last_mod_time");
			sqlString_userid = db.connect("SELECT * FROM tbl_lms_user where user_first_name='John'", "user_id");
			//	sqlString_phonenum = db.connect("SELECT * FROM tbl_lms_user where user_first_name='John'", "last_mod_time");
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("Compare and Assert request sent and DB user details")
	public void compare_and_assert_request_sent_and_db_user_details() {
		
		Assert.assertEquals(sqlString_userid.trim(), RequestString_userid.trim());
	}

	@Given("New user is added with Post request with alphanumeric name and location")
	public void new_user_is_added_with_post_request_with_alphanumeric_name_and_location() {
		setupRestAssured();

		JSONObject requestparams = new JSONObject();
		requestparams.put("comments", "postreq2");
		requestparams.put("education_pg", " masters");
		requestparams.put("education_ug", "ECE");
		requestparams.put("linkedin_url", "https://www.linkedin.com/in/robertBell/");
		requestparams.put("location", "seattle");
		requestparams.put("name", "robert4565,Bell");
		requestparams.put("phone_number", "5485894139");
		requestparams.put("time_zone", "EST");
		requestparams.put("visa_status", "H1B");
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Post request payload is : " + requestparams.toString());
		Response response = request.post("/Users");

		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		//ResponseBody body = response.getBody();


		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userid = jsonPathEvaluator.get("user_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		RequestString_name= jsonPathEvaluator.get("name");
		RequestString_location= jsonPathEvaluator.get("location");
		System.out.println("Json element value in response = " + RequestString_userid);
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString_name);
		System.out.println("Json element value in response = " + RequestString_location);
		
		
	}

	@When("New user is queried with name and location fields")
	public void new_user_is_queried_with_name_and_location_fields() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+RequestString_userid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+RequestString_userid+"'", "last_mod_time");
			sqlString_userid = db.connect("SELECT * FROM tbl_lms_user where user_id='"+RequestString_userid+"'", "user_id");
			sqlString_name = db.connect("SELECT * FROM tbl_lms_user where user_id='"+RequestString_userid+"'", "user_first_name");
			sqlString_location = db.connect("SELECT * FROM tbl_lms_user where user_id='"+RequestString_userid+"'", "user_location");
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userid);
			System.out.println("received DB value sqlString_userid = " + sqlString_name);
			System.out.println("received DB value sqlString_userid = " + sqlString_location);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Then("Compare and Assert name and location fields from DB and response")
	public void compare_and_assert_name_and_location_fields_from_db_and_response() {
		Assert.assertEquals(sqlString_name.trim(), RequestString_name.trim());
		Assert.assertEquals(sqlString_location.trim(), RequestString_location.trim());
		
	}

	@Given("New user is added with Post request blank linkedin edu_pg and comments fields")
	public void new_user_is_added_with_post_request_blank_linkedin_edu_pg_and_comments_fields() {
		setupRestAssured();

		JSONObject requestparams = new JSONObject();
		requestparams.put("comments", " ");
		requestparams.put("education_pg", " ");
		requestparams.put("education_ug", "ECE");
		requestparams.put("linkedin_url", " ");
		requestparams.put("location", "new york");
		requestparams.put("name", "robert,scott");
		requestparams.put("phone_number", "5487894239");
		requestparams.put("time_zone", "EST");
		requestparams.put("visa_status", "H1B");
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Post request payload is : " + requestparams.toString());
		Response response = request.post("/Users");

		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		//ResponseBody body = response.getBody();


		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userid = jsonPathEvaluator.get("user_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		RequestString_name= jsonPathEvaluator.get("name");
		
		System.out.println("Json element value in response = " + RequestString_userid);
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString_name);
		
		
	}

	@When("New user is queried with linkedin edu_pg and comments fields")
	public void new_user_is_queried_with_linkedin_edu_pg_and_comments_fields() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "last_mod_time");
			sqlString_userid = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "user_id");
			sqlString_edupg = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "user_edu_pg");
			sqlString_comments = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "user_comments");
			sqlString_linkedin = db.connect("SELECT * FROM tbl_lms_user where user_first_name='robert'", "user_linkedin_url");
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userid);
			System.out.println("received DB value sqlString_userid = " + sqlString_edupg);
			System.out.println("received DB value sqlString_userid = " + sqlString_comments);
			System.out.println("received DB value sqlString_userid = " + sqlString_linkedin);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("Compare and Assert linkedin edu_pg and comments fields from DB and response")
	public void compare_and_assert_linkedin_edu_pg_and_comments_fields_from_db_and_response() {
		Assert.assertNull(sqlString_edupg);
		Assert.assertNull(sqlString_comments);
		Assert.assertNull(sqlString_linkedin);
	}

}
