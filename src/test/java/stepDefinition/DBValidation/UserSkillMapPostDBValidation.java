package stepDefinition.DBValidation;

import org.json.simple.JSONObject;
import org.junit.Assert;

import utils.DBRestConnect;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillMapPostDBValidation {
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_userskillid = null;
	String RequestString_userskillid = null;
	String RequestString_message = null;
	String userid = "U09";
	private Response response;
	

	private RequestSpecification request;
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}

	
	@SuppressWarnings("unchecked")
	@Given("new user is mapped with skill with post request")
	public void new_user_is_mapped_with_skill_with_post_request() {
		setupRestAssured();

		JSONObject requestparams = new JSONObject();
		requestparams.put("months_of_exp", "34");
		requestparams.put("skill_id", " 2");
		requestparams.put("user_id", userid);
		
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Post request payload is : " + requestparams.toString());
		Response response = request.post("/UserSkills");

		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		//ResponseBody body = response.getBody();-


		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userskillid = jsonPathEvaluator.get("user_skill_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_userskillid);
		System.out.println("Json element value in response = " + RequestString_message);


		
	}

	@When("DB is queried for new mapped user")
	public void DB_is_queried_for_new_mapped_user() {
	    
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='"+userid+"'", "last_mod_time");
			sqlString_userskillid = db.connect("SELECT * FROM tbl_lms_user where user_first_name='"+userid+"'", "user_skill_id");
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userskillid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Then("Response and DB query results are asserted")
	public void Response_and_DB_query_results_are_asserted() {
	    
		Assert.assertEquals(sqlString_userskillid.trim(), RequestString_userskillid.trim());
	}

}
