package stepDefinition.user;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import utils.AuthenticationUtil;
import utils.ExcelReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetUsersAPI {
	
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserAPIDataExcel.xlsx";
	private static final String SHEET_NAME = "get";
	
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
			
			String userId = reader.getCellData(SHEET_NAME, 1, rowNum);
			System.out.println(userId);
			innerDataMap.put("Value", userId);
			
			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statuscode", statusCodeNumber );
			
			dataMap.put(dataKey, innerDataMap);
		}
		
		return dataMap;
	}

	/** Test for all UsersSkill **/
	@Given("user set GET method with endpoint\\/url\\/Users")
	public void user_set_get_method_with_endpoint_url_users() {
		//request = util.requestSpecification(request);
		request=util.requestSpecification(request);
	}

	@When("User sends request")
	public void user_sends_request() {
		response = request.get("/Users");
	}

	@Then("JSON schema is valid")
	public void json_schema_is_valid() {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
		
	}

	@Then("User receives status code {int}")
	public void user_receives_status_code(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
	}

	@Then("User should receive a list of all Users")
	public void user_should_receive_a_list_of_all_users() {
		System.out.println("list of all users");
		System.out.println("list of Users"+response.getBody().asPrettyString());
	}

	/** Test for valid id **/
	@Given("user set GET method with endpoint\\/url\\/Users\\/ID")
	public void user_set_get_method_with_endpoint_url_users_id() {
		data = populateDataFromExcel();
	}

	@When("User sends the request with specific userid")
	public void user_sends_the_request_with_specific_userid() {
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("validuserid");
		response = request.get("/Users/" + validDataMap.get("Value"));
		System.out.println("valid User"+validDataMap.get("Value"));

	}
	@Then("User should receive a details of specific user ID")
	public void user_should_receive_a_details_of_specific_user_id() {
		Assert.assertNotNull(response);
	}

//Test Invalid ids -- till before.....

	@When("User sends the request with invalid id")
	public void user_sends_the_request_with_invalid_id() {
		//String sheetName = "get";
		request = util.requestSpecification(request);

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 3);
		System.out.println("/Users" + data);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with aplphnumeric id")
	public void user_sends_the_request_with_aplphnumeric_id() {
		//String sheetName = "get";
		request = util.requestSpecification(request);

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 4);
		System.out.println("/Users/" + data);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with blank id")
	public void user_sends_the_request_with_blank_id() {
		//String sheetName = "get";
		request = util.requestSpecification(request);

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 5);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with decimal id")
	public void user_sends_the_request_with_decimal_id() {
		//String sheetName = "get";
		request = util.requestSpecification(request);

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 6);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with non existing id")
	public void user_sends_the_request_with_non_existing_id() {
		//String sheetName = "get";
		request = util.requestSpecification(request);

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 7);
		System.out.println("/Users/" + data);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with null id")
	public void user_sends_the_request_with_null_id()
	{
		request = util.requestSpecification(request);
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 8);
		System.out.println("/Users/" + data);
		response = request.get("/Users/" + data);
	}

	@When("User sends the request with zero id")
	public void user_sends_the_request_with_zero_id()
	{
		request = util.requestSpecification(request);
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 9);
		System.out.println("/Users/" + data);
		response = request.get("/Users/" + data);
	}
	
	@Then("JSON schema is valid for Users")
	public void json_schema_is_valid_for_Users()
	{
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
	}
	
	public static boolean isJSONValid(String serverresponse) 
	{
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

}
