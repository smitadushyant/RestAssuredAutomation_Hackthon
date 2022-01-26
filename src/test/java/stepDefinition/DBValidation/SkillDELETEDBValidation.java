package stepDefinition.DBValidation;

import org.junit.Assert;

import utils.DBRestConnect;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SkillDELETEDBValidation {
	DBRestConnect db = new DBRestConnect();
	String sqlString = null;
	String deleteRequestString = null;
	String getRequestString = null;
	String skill_id = "6";
	private Response response;
	private RequestSpecification request;
	
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}
	
	@Given("Skill is deleted with DELETE request")
	public void skill_is_deleted_with_delete_request() {
		setupRestAssured();
		response = request.delete("/Skills/"+skill_id);
		System.out.println("Server response for delete user = " + response.asString());

		int delete_statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ delete_statusCode);
		Assert.assertEquals(delete_statusCode, 200);
	}

	@When("Query the DataBase with user skillid")
	public void query_the_data_base_with_user_skillid() {
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_user where skill_id='"+skill_id+"'", "skill_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}

	@Then("Request response message and DataBase response are asserted")
	public void request_response_message_and_data_base_response_are_asserted() {
		 //System.out.println("User Deleted");
		   Assert.assertEquals(deleteRequestString.trim(),"The record has been deleted !!");
		   Assert.assertNull(sqlString);
		}
	}
