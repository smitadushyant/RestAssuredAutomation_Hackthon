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

public class SkillPUTDBValidation {
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_skillid = null;
	String sqlString_skillname = null;
	String RequestString_skillid = null;
	String RequestString_skillname = null;
	String RequestString_message = null;
	String skillid = "8";
	
	private RequestSpecification request;
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}
	@SuppressWarnings("unchecked")
	@Given("User details are modified for existing skill with Put request")
	public void user_details_are_modified_for_existing_skill_with_put_request() {
setupRestAssured();
		
		JSONObject requestparams = new JSONObject();
		requestparams.put("skill_name", "TrelloPUT");
		
		request.header("Content-Type", "application/json");
		request.body(requestparams.toJSONString());
		System.out.println("PUT request payload is : " + requestparams.toJSONString());
		Response response = request.put("/Skills/"+skillid);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, "201");
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_skillid = jsonPathEvaluator.get("skill_id");
		RequestString_message = jsonPathEvaluator.get("message_response");
		
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString_skillid);
	}

	@When("Modified user details are queried from DataBase With creation and updation times")
	public void modified_user_details_are_queried_from_data_base_with_creation_and_updation_times() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "last_mod_time");
			sqlString_skillid = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "skill_id");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_skillid = " + sqlString_skillid);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
	}
	}

	@Then("DataBase details are compared with request payload details and asserted")
	public void data_base_details_are_compared_with_request_payload_details_and_asserted() {
		
			System.out.println("Comparing and asserting skill id created from request response and skill id queried from DB");
			Assert.assertEquals(sqlString_skillname.trim(), RequestString_skillname.trim());
			
	}

}