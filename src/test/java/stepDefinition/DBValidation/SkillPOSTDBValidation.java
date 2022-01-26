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

public class SkillPOSTDBValidation {
	
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_skillid = null;
	String RequestString1 = null;
	String RequestString2 = null;

	private RequestSpecification request;

	//private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Given("New skill is added with Post request")
	public void new_skill_is_added_with_post_request() {
		setupRestAssured();
		
		JSONObject requestparams = new JSONObject();
			requestparams.put("skill_name","Trello");
			
		request.header("Content-Type", "application/json");

		request.body(requestparams.toString());
		System.out.println("Post request payload is : " + requestparams.toString());
		Response response = request.post("/Skills");
		
		int statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ statusCode);
		Assert.assertEquals(statusCode, 201);
		//ResponseBody body = response.getBody();

		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString1 = jsonPathEvaluator.get("skill_id");
		RequestString2 = jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString1);
		System.out.println("Json element value in response = " + RequestString2);
		
		
		
	}

	@When("New Skill is queried from DB")
	public void new_skill_is_queried_from_db() {
		try {
			
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "last_mod_time");
			sqlString_skillid = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='Trello'", "skill_id");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_skillId = " + sqlString_skillid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	@Then("Compare and Assert request sent and DB skill details")
	public void compare_and_assert_request_sent_and_db_skill_details() {
		System.out.println("Comparing and asserting skill id created from request response and skill id queried from DB");
		Assert.assertEquals(sqlString_skillid.trim(), RequestString1.trim());
	   
	}

	
	
	
	

}
