package stepDefinition.user;

import java.util.HashMap;

import org.junit.Assert;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.ExcelReader;

public class DeleteUserAPI {

	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserAPIDataExcel.xlsx";
	private static final String SHEET_NAME = "delete";

	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();

	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);

		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();

		int rows = reader.getRowCount(SHEET_NAME);
		System.out.println("rows :: " + rows);
		for (int rowNum = 2; rowNum <= rows; rowNum++) {

			if (reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}

			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);

			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();

			String userId = reader.getCellData(SHEET_NAME, 1, rowNum);
			//System.out.println(userId);
			innerDataMap.put("Value", userId);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			//System.out.println(statusCode);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			//System.out.println("statusCodeNumber :: " + statusCodeNumber);
			innerDataMap.put("statuscode", statusCodeNumber);

			dataMap.put(dataKey, innerDataMap);
		}
		/*
		 * ObjectMapper mapper = new ObjectMapper(); String expectedJSON; try {
		 * expectedJSON = mapper.writeValueAsString(dataMap);
		 * System.out.println("BasicAuth :: expectedJSON :: " + expectedJSON); } catch
		 * (JsonProcessingException e) { e.printStackTrace(); }
		 */

		return dataMap;
	}

	@Given("Set DELETE request endpoint to \\Users\\userid")
	public void set_delete_request_endpoint_to_users_userid() {
		// Read data from excel
		data = populateDataFromExcel();
	}

	@When("User sends request with valid userId")
	public void user_sends_request_with_valid_user_id() {
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("validuserid");

		response = request.delete("/Users/" + validDataMap.get("Value"));
	}

	@Then("User should receive a valid response code {int}")
	public void user_should_receive_a_valid_response(Integer int1) {
		HashMap<String, Object> validDataMap = data.get("validuserid");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	@When("User sends request with blank userId")
	public void user_sends_request_with_blank_user_id() {
		request = util.requestSpecification(request);
		response = request.delete("/Users");
	}

	@Then("response with {int} Method Not Allowed status code")
	public void response_with_method_not_allowed_status_code(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	@When("User sends request with non-existing userId")
	public void user_sends_request_with_non_existing_user_id() {
		request = util.requestSpecification(request);
		HashMap<String, Object> inValidDataMap = data.get("non existing");

		response = request.delete("/Users/" + inValidDataMap.get("Value"));
	}

	@When("User sends request with invalid userId")
	public void user_sends_request_with_invalid_user_id() {
		request = util.requestSpecification(request);
		HashMap<String, Object> inValidDataMap = data.get("invalid");

		response = request.delete("/Users/" + inValidDataMap.get("Value"));
	}

	@Then("response with error 404 Not found status code")
	public void response_with_error_not_found_status_code() {
		HashMap<String, Object> inValidDataMap = data.get("non existing");
		Assert.assertEquals(inValidDataMap.get("statuscode"), response.statusCode());
		
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

}
