package stepDefinition.DBValidation;

import org.json.simple.JSONObject;
import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.DBRestConnect;

public class UserSkillMapPutDBValidation {

	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString = null;
	String RequestString_userskillid = null;
	String RequestString_message = null;
	String RequestString = null;
	String Id = "US08";
	private Response response;
	

	private RequestSpecification request;
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}

	
	@SuppressWarnings("unchecked")
	@Given("new months of exp is updated with put request for existing user skill id")
	public void new_months_of_exp_is_updated_with_put_request_for_existing_user_skill_id() {
		
		setupRestAssured();

		JSONObject requestparams = new JSONObject();
		requestparams.put("months_of_exp", "34");
		requestparams.put("skill_id", " 1");
		requestparams.put("user_id", "U08");
		
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Put request payload is : " + requestparams.toString());
		Response response = request.put("/UserSkills/"+Id);

		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		


		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userskillid = jsonPathEvaluator.get("user_skill_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		RequestString=jsonPathEvaluator.get("months_of_exp");
		System.out.println("Json element value in response = " + RequestString_userskillid);
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString);


	   
	}

	@When("DB is queried for updated field")
	public void db_is_queried_for_updated_field() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+Id+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+Id+"'", "last_mod_time");
			sqlString = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+Id+"'", "months_of_exp");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString= " + sqlString);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Then("Put Response and DB query results are asserted")
	public void put_response_and_db_query_results_are_asserted() {
		
		Assert.assertEquals(sqlString.trim(), RequestString.trim());
	   
	}
	
}
