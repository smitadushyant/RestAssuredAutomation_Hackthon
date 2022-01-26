package stepDefinition.DBValidation;

import org.junit.Assert;

import utils.DBRestConnect;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class SkillGETDBValidation {
	DBRestConnect db = new DBRestConnect();
	String sqlString = null;
	String getRequestString = null;
	private Response response;
	private RequestSpecification request;
	
	private void setupRestAssured() {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");
	}

	
	@Given("Expected value is queried from DataBase")
	public void expected_value_is_queried_from_data_base() {
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_skill_master where skill_id='2'", "skill_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@When("GET request is sent to get actual response code")
	public void get_request_is_sent_to_get_actual_response_code() {
		setupRestAssured();
		response = request.get("/Skills/2");
		System.out.println("Server response for GET skill = " + response.asString());

		JsonPath jsonPathEvaluator = response.jsonPath();
		getRequestString = jsonPathEvaluator.get("skill_id");
		System.out.println("Json element value in response = " + getRequestString);

	}

	@Then("Compare and Assert actual value and expected value")
	public void compare_and_assert_actual_value_and_expected_value() {
		Assert.assertEquals(sqlString.trim(), getRequestString.trim());
	}

}
