package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import utils.ExcelReader;

import org.junit.Assert;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

public class BasicAuth {

	private String url = "http://localhost:8080/Users";
	private Response response = null;
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/BasicAuthExcel.xlsx";
	private static final String SHEET_NAME = "basicAuth";
	
	private HashMap<String, HashMap<String, Object>> data = new HashMap<String,HashMap<String,Object>>();
	
	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		
		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String,Object>>();
		
		int rows = reader.getRowCount(SHEET_NAME);
		System.out.println("rows :: "+rows);
		for(int rowNum = 2; rowNum <= rows; rowNum++) {
			
			if(reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}
			
			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);
			
			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();
			
			String userName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("username", userName);
			
			String passWord = reader.getCellData(SHEET_NAME, 2, rowNum);
			innerDataMap.put("password", passWord);
			
			String statusCode = reader.getCellData(SHEET_NAME, 3, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statuscode", statusCodeNumber);
			
			dataMap.put(dataKey, innerDataMap);
		}
		/*
		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON;
		try {
			expectedJSON = mapper.writeValueAsString(dataMap);
			System.out.println("BasicAuth :: expectedJSON :: " + expectedJSON);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		*/
		
		return dataMap;
	}
	
	private void setupResponse(HashMap<String, Object> map) {
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName(map.get("username").toString());
		authScheme.setPassword(map.get("password").toString());
		RestAssured.authentication = authScheme;
	}

	@Given("the users endpoint exists")
	public void preReq() {
		RestAssured.baseURI = url;
		data = populateDataFromExcel();
	}
	
	@When("I send a in-valid userId and valid password as credentials")
	public void i_send_a_in_valid_user_id_and_valid_password_as_credentials() {
		HashMap<String, Object> invalidDataMap = data.get("invalidUserId");

		setupResponse(invalidDataMap);
		response = given().header("Content-Type", "application/json").when().get(url);
	}
	
	@When("I send a valid userId and in-valid password as credentials")
	public void i_send_a_valid_user_id_and_in_valid_password_as_credentials() {
		HashMap<String, Object> invalidDataMap = data.get("invalidPassword");
		
		setupResponse(invalidDataMap);
		response = given().header("Content-Type", "application/json").when().get(url);
	}

	@Then("response status code should be 401 unAuthotized")
	public void response_status_code_should_be_un_authotized() {
		HashMap<String, Object> invalidDataMap = data.get("invalidUserId");
		Assert.assertEquals(invalidDataMap.get("statuscode"), response.getStatusCode());
		
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	@When("I send a valid login credentials")
	public void validUser() {
		HashMap<String, Object> validDataMap = data.get("validCredential");
		
		setupResponse(validDataMap);
		response = given().header("Content-Type", "application/json").when().get(url);
	}

	@Then("response status code should be 200")
	public void checkResponseStatusCode() {
		HashMap<String, Object> validDataMap = data.get("validCredential");
		Assert.assertEquals(validDataMap.get("statuscode"), response.getStatusCode());
		
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
	
	
}