package stepDefinition.DBValidation;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.DBRestConnect;

public class UserSkillMapDeleteDBValidation {
	
	DBRestConnect db = new DBRestConnect();
	
	String sqlString_userskillid = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	String id = "US09";
	private Response response;
	

	private RequestSpecification request;
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}

	
	@Given("existing user skill id is deleted with delete request")
	public void existing_user_skill_id_is_deleted_with_delete_request() {
		setupRestAssured();
		response = request.delete("/UserSkills/"+id);
		System.out.println("Server response for delete user = " + response.asString());

		int delete_statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ delete_statusCode);
		Assert.assertEquals(delete_statusCode, 200);
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_message = jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_message);
		
	}

	@When("DB is queried deleted field")
	public void db_is_queried_deleted_field() {
		try {
			sqlString_userskillid = db.connect("SELECT * FROM tbl_lms_userskill_map where user_Skill_id='"+id +"'", "user_skill_id");

			System.out.println("received SqlString = " + sqlString_userskillid);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("Delete Response and DB query results are asserted")
	public void response_and_db_query_results_are_asserted() {
	    
		 Assert.assertEquals(RequestString_message.trim(),"The record has been deleted !!");
		   Assert.assertNull(sqlString_userskillid);
	}

}
