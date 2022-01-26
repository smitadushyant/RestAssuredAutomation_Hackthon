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

public class UserDELETEDBValidation {

	DBRestConnect db = new DBRestConnect();
	String sqlString = null;
	String deleteRequestString = null;
	String getRequestString = null;
	String userid = "U41";
	private Response response;
	private RequestSpecification request;

	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}
	
	@Given("User is deleted with DELETE request")
	public void user_is_deleted_with_delete_request() {
		setupRestAssured();
		response = request.delete("/Users/"+userid);
		System.out.println("Server response for delete user = " + response.asString());

		int delete_statusCode = response.getStatusCode();
		System.out.println("statusCode ="+ delete_statusCode);
		Assert.assertEquals(delete_statusCode, 200);
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		deleteRequestString = jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + deleteRequestString);
		
		

	}

	@When("Query the DB with user id")
	public void query_the_db_with_user_id() {
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_user where user_id='"+userid +"'", "user_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("Request response message and DB response are asserted")
	public void request_response_message_and_db_response_are_asserted() {
		Assert.assertEquals(deleteRequestString.trim(),"The record has been deleted !!");
		   Assert.assertNull(sqlString,"User not found in DB");
	}
	
}
