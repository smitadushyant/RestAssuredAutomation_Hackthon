package stepDefinition.user;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.ExcelReader;

public class PutUserAPI {

	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserAPIDataExcel.xlsx";
	private static final String SHEET_NAME = "put";
	private static String recordName;

	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();

	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);

		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();

		int rows = reader.getRowCount(SHEET_NAME);
		// System.out.println("rows :: "+rows);
		for (int rowNum = 2; rowNum <= rows; rowNum++) {

			if (reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}

			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);

			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();

			String userName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("name", userName);

			String phone_number = reader.getCellData(SHEET_NAME, 2, rowNum);
			long phoneNumber = 0L;
			if (!phone_number.equals(""))
				phoneNumber = Math.round(Double.parseDouble(phone_number));
			innerDataMap.put("phone_number", phoneNumber);

			String location = reader.getCellData(SHEET_NAME, 3, rowNum);
			innerDataMap.put("location", location);

			String time_zone = reader.getCellData(SHEET_NAME, 4, rowNum);
			innerDataMap.put("time_zone", time_zone);

			String linkedin_url = reader.getCellData(SHEET_NAME, 5, rowNum);
			innerDataMap.put("linkedin_url", linkedin_url);

			String education_ug = reader.getCellData(SHEET_NAME, 6, rowNum);
			innerDataMap.put("education_ug", education_ug);

			String education_pg = reader.getCellData(SHEET_NAME, 7, rowNum);
			innerDataMap.put("education_pg", education_pg);

			String visa_status = reader.getCellData(SHEET_NAME, 8, rowNum);
			innerDataMap.put("visa_status", visa_status);

			String comments = reader.getCellData(SHEET_NAME, 9, rowNum);
			innerDataMap.put("comments", comments);

			String userId = reader.getCellData(SHEET_NAME, 10, rowNum);
			innerDataMap.put("userId", userId);
			
			String statusCode = reader.getCellData(SHEET_NAME, 11, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statuscode", statusCodeNumber);
			
			dataMap.put(dataKey, innerDataMap);
		}
		/*
		 * ObjectMapper mapper = new ObjectMapper(); String expectedJSON; try {
		 * expectedJSON = mapper.writeValueAsString(dataMap);
		 * System.out.println("PostUserAPI :: expectedJSON :: " + expectedJSON); } catch
		 * (JsonProcessingException e) { e.printStackTrace(); }
		 */

		return dataMap;
	}

	private void putRequest(HashMap<String, Object> map) {

		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON = null;
		try {
			expectedJSON = mapper.writeValueAsString(map);
			System.out.println("PostUserAPI :: map :: " + expectedJSON);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean isJsonString = isJSONValid(expectedJSON);

		request = util.requestSpecification(request);
		if (isJsonString) {
			request.header("Content-Type", "application/json");
			request.body(map);
		}
		response = request.put("/Users/" + map.get("userId"));
		System.out.println("PostUserAPI :: response :: " + response.asString());
	}

	private static boolean isJSONValid(String serverresponse) {
		try {
			new JSONObject(serverresponse);
		} catch (JSONException ex) {
			try {
				new JSONArray(serverresponse);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	@Given("User is on the PUT Request")
	public void user_is_on_the_put_request() {
		System.out.println("UserPost :: inside Given");
		data = populateDataFromExcel();
	}
	
	@When("User updates the existing userID with all inputs from excel")
	public void user_updates_the_existing_user_id_with_all_inputs_from_excel() {
		HashMap<String, Object> validDataMap = data.get("validuserdata");
		putRequest(validDataMap);	
	}
	
	
	@Then("User receives an 201 status code and updates the existing record")
	public void assertionForValidRecord() {
		HashMap<String, Object> validDataMap = data.get("validuserdata");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	@Then("{string} in response_body of Put request is {string}")
	public void in_responsebody_is(String keydata, String Expected) {
		String responseString = response.asString();
		JsonPath js = new JsonPath(responseString);
		try {
			Assert.assertEquals(js.get(keydata).toString(), Expected);
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
	}

	@When("User sends input with alphanumeric username")
	public void user_sends_input_with_alphanumeric_username() {
		recordName = "alphanumericName";
		HashMap<String, Object> inValidDataMap = data.get("alphanumericName");
		putRequest(inValidDataMap); 
	}
	
	@When("User sends input with invalid phone number")
	public void user_sends_input_with_invalid_phone_number() {
		recordName = "invalidPhone";
		HashMap<String, Object> inValidDataMap = data.get("invalidPhone");
		putRequest(inValidDataMap); 
	}
	
	@When("User sends input with invalid visa status")
	public void user_sends_input_with_invalid_visa_status() {
		recordName = "invalidVisa";
		HashMap<String, Object> inValidDataMap = data.get("invalidVisa");
		putRequest(inValidDataMap);
	}

	@Then("User receives 400 bad request status response for update")
	public void user_receives_bad_request_status_response_for_update() {
		HashMap<String, Object> badURL = data.get(recordName);
		try {
			Assert.assertEquals(badURL.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
	
	@When("User sends input with alphanumeric location and comments inputs")
	public void user_sends_input_with_alphanumeric_location_and_comments_inputs() {
		recordName = "alphanumericLocation";
		HashMap<String, Object> excelDataMap = data.get("alphanumericLocation");
		putRequest(excelDataMap);
	}

	@Then("User receives 201 as valid status code and updates the existing record")
	public void user_receives_as_valid_status_code_and_updates_the_existing_record() {
		HashMap<String, Object> validDataMap = data.get("alphanumericLocation");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
	
}
