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

public class PostUserAPI {
	
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserAPIDataExcel.xlsx";
	private static final String SHEET_NAME = "post";
	private static String recordName;

	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	
	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		
		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String,Object>>();
		
		int rows = reader.getRowCount(SHEET_NAME);
		//System.out.println("rows :: "+rows);
		for(int rowNum = 2; rowNum <= rows; rowNum++) {
			
			if(reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}
			
			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);
			
			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();
			
			String userName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("name", userName);
			
			String phone_number = reader.getCellData(SHEET_NAME, 2, rowNum);
			long phoneNumber = 0L;
			if(!phone_number.equals(""))
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
			
			String statusCode = reader.getCellData(SHEET_NAME, 10, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statuscode", statusCodeNumber);
			
			dataMap.put(dataKey, innerDataMap);
		}
		/*
		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON;
		try {
			expectedJSON = mapper.writeValueAsString(dataMap);
			System.out.println("PostUserAPI :: expectedJSON :: " + expectedJSON);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		*/
		
		return dataMap;
	}
	
	private void postRequest(HashMap<String, Object> map) {
		
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
			if(isJsonString) {
				request.header("Content-Type", "application/json");
				request.body(map);
			}
			response = request.post("/Users");	
			System.out.println("PostUserAPI :: response :: " +response.asString());
		
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
	
	@Given("User is on Post request")
	public void user_is_on_post_request() {
		System.out.println("UserPost :: inside Given");
		data = populateDataFromExcel();
	}

	@When("User sends input with valid JSON body from valid excel")
	public void user_sends_input_with_valid_json_body_from_valid_excel() {
		HashMap<String, Object> validDataMap = data.get("validuserdata");
		postRequest(validDataMap);		
	}

	@Then("User recieves a 201 valid status code")
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

	@Then("{string} in responsebody is {string}")
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

	@When("User sends input with blank Linkedin_url")
	public void user_sends_input_with_blank_linkedin_url() {
		recordName ="blank_linkedin_url";
		HashMap<String, Object> blankUrl = data.get("blank_linkedin_url");
		postRequest(blankUrl);
	}
	
	@When("User sends input with blank education_ug,education_pg field and comments")
	public void user_sends_input_with_blank_education_ug_education_pg_field_and_comments() {
		HashMap<String, Object> blankFlds = data.get("blank_allowable_record");
		recordName ="blank_allowable_record";
		postRequest(blankFlds);
	}

	@Then("User recieves a valid status code 201")
	public void user_recieves_a_valid_status_code() {
		HashMap<String, Object> blankUrl = data.get("blank_linkedin_url");
		try {
			Assert.assertEquals(blankUrl.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	@When("User sends input with alphanumeric name")
	public void user_sends_input_with_alphanumeric_name() {
		recordName ="alphaNumeric_Name";
		HashMap<String, Object> alphanumericFlds = data.get("alphaNumeric_Name");
		postRequest(alphanumericFlds);
	}
	
	@When("User sends input with invalid blank field from excel")
	public void user_sends_input_with_invalid_blank_field_from_excel() {
		recordName ="all_blank";
		HashMap<String, Object> allBlankFlds = data.get("all_blank");
		postRequest(allBlankFlds);
	}

	@When("User sends input with only first name from excel")
	public void user_sends_input_with_only_first_name_from_excel() {
		recordName ="only_firstName";
		HashMap<String, Object> onlyFirstName = data.get("only_firstName");
		postRequest(onlyFirstName);
	}

	@When("User sends input with only last name from excel")
	public void user_sends_input_with_only_last_name_from_excel() {
		recordName ="only_lastName";
		HashMap<String, Object> onlyLastName = data.get("only_lastName");
		postRequest(onlyLastName);
	}

	@When("User sends input with invalid linkedin_url from excel")
	public void user_sends_input_with_invalid_linkedin_url_from_excel() {
		recordName ="invalid_linked_url";
		HashMap<String, Object> invalidLinkdinurl = data.get("invalid_linked_url");
		postRequest(invalidLinkdinurl);
	}

	@When("User sends input with invalid timezone from excel")
	public void user_sends_input_with_invalid_timezone_from_excel() {
		recordName ="invalid_timeZone";
		HashMap<String, Object> invalidTimeZone = data.get("invalid_timeZone");
		postRequest(invalidTimeZone);
	}

	@When("User sends input with invalid visa_status from excel")
	public void user_sends_input_with_invalid_visa_status_from_excel() {
		recordName ="invalid_visaStatus";
		HashMap<String, Object> invalidVisaStatus = data.get("invalid_visaStatus");
		postRequest(invalidVisaStatus);
	}

	@When("User sends input with invalid phone_number from execel")
	public void user_sends_input_with_invalid_phone_number_from_execel() {
		recordName ="invalid_phoneNumber";
		HashMap<String, Object> invalidPhoneNo = data.get("invalid_phoneNumber");
		postRequest(invalidPhoneNo);
	}
	
	@Then("User receive 400 bad request error response")
	public void user_receive_bad_request_error_response() {
		HashMap<String, Object> badURL = data.get(recordName);
		try {
			Assert.assertEquals(badURL.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
	
}