package stepDefinition.DBValidation;
import org.junit.Assert;
import utils.DBRestConnect;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserGETDBValidation {

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
	
	@Given("Expected value is queried from DB")
	public void expected_value_is_queried_from_db() {
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_user where user_id='U10'", "user_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@When("GET request is sent to get actual response")
	public void get_request_is_sent_to_get_actual_response() {
		setupRestAssured();
		response = request.get("/Users/U10");
		System.out.println("Server response for GET user = " + response.asString());

		JsonPath jsonPathEvaluator = response.jsonPath();
		getRequestString = jsonPathEvaluator.get("user_id");
		System.out.println("Json element value in response = " + getRequestString);

	}

	@Then("Compare and Assert actual and expected values")
	public void compare_and_assert_actual_and_expected_values() {
		Assert.assertEquals(sqlString.trim(), getRequestString.trim());
	}

}
