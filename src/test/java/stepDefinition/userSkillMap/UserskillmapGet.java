
package stepDefinition.userSkillMap;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.ExcelReader;

public class UserskillmapGet {
	
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserSkillMapDataExcel.xlsx";
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
			//System.out.println(userId);
			innerDataMap.put("Id", userId);
			
			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statuscode", statusCodeNumber);
			
			dataMap.put(dataKey, innerDataMap);
		}

		return dataMap;
		}
	public static boolean isJSONValid(String serverresponse) {
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
	//all User
	@Given("User is on GET Method with Endpoint as \\/UserSkills")
	public void user_is_on_get_method_with_endpoint_as_user_skills() {
		request = util.requestSpecification(request);
	    
	}

	@When("User sends request for UserSkillMap API")
	public void user_sends_request_for_user_skill_map_api() {
	   response=request.get("/UserSkills");
	}

	@Then("User receives {int} OK status code for UserSkillMap API")
	public void user_receives_ok_status_code_for_user_skill_map_api(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}

	@Then("JSON schema is valid for UserSkillMap API for all record")
	public void json_schema_is_valid_for_user_skill_map_api_for_all_record() {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
		System.out.println("list of all users"+response.asPrettyString());
	}
//valid Data
	@Given("User is on GET Method with Endpoint as \\/UserSkills\\/id")
	public void user_is_on_get_method_with_endpoint_as_user_skills_id() {
		data = populateDataFromExcel();
		request=util.requestSpecification(request);
		
	}

	@When("User sends request with Valid userSkillId")
	public void user_sends_request_with_valid_user_skill_id() {
		
		HashMap<String, Object> validDataMap = data.get("validId");
		System.out.println("ValidDataMap"+validDataMap);
		response = request.get("/UserSkills/" + validDataMap.get("Id"));
		
	}

	@Then("JSON schema is valid for UserSkillMap API")
	public void json_schema_is_valid_for_user_skill_map_api() {
		Assert.assertNotNull(response);
		
	}

	//@Then("User receives status code {int} ")
	//public void user_receives_status_code(Integer int1) {
	@Then("User receives status code {int} for UserSkillMap API")
	public void user_receives_status_code_for_user_skill_map_api(Integer int1) {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
	   
	}

//Invalid data
	@When("User sends the request with non-existing userSkillID for UserSkillMap API")
	public void user_sends_the_request_with_non_existing_user_skill_id_for_user_skill_map_api() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 3);
		response = request.get("/UserSkills/" + data);
		System.out.println("/UserSkills/" + data);
		
	}

	@When("User sends the request with Invalid userSkillID for UserSkillMap API")
	public void user_sends_the_request_with_invalid_user_skill_id_for_user_skill_map_api() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1,4);
		System.out.println("/UserSkills/" + data);
		response = request.get("/UserSkills/" + data);
	}

	@When("User sends the request with blank userSkillID for UserSkillMap API")
	public void user_sends_the_request_with_blank_user_skill_id_for_user_skill_map_api() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 5);
		System.out.println("/UserSkills/" + data);
		response = request.get("/UserSkills/" + data);
	}
	@When("User sends the request with decimal userSkillID for UserSkillMap API")
	public void user_sends_the_request_with_decimal_user_skill_id_for_user_skill_map_api() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 6);
		System.out.println("/UserSkills/" + data);
		response = request.get("/UserSkills/" + data);
	}
	
	//valid Skillmap
	@Given("User sets GET request with a valid endpoint as \\/UserSkillsMap")
	public void user_sets_get_request_with_a_valid_endpoint_as_user_skills_map() {
		request = util.requestSpecification(request);
	}

	@When("User sends a GET HTTP request for UserSkillMap all")
	public void user_sends_a_get_http_request_for_user_skill_map_all() {
	   response=request.get("/UserSkillsMap");
	}

	@Then("User receives {int} OK status code for UserSkillMap all")
	public void user_receives_ok_status_code_for_user_skill_map_all(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}

	@Then("JSON schema is valid for UserSkillMap all")
	public void json_schema_is_valid_for_user_skill_map_all() {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
		System.out.println("list of all users"+response.asPrettyString());
	}

	@Given("user set GET method with endpoint\\/url\\/UserskillsMap\\/userId")
	public void user_set_get_method_with_endpoint_url_userskills_map_user_id() {
		data = populateDataFromExcel();
		request=util.requestSpecification(request);
	}

	@When("User sends the request with userId for UserSkillMap")
	public void user_sends_the_request_with_user_id_for_user_skill_map() {

		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 7);
		System.out.println("/UserSkillsMap/" + data);
		response = request.get("/UserSkillsMap/" + data);
	}

	@Then("JSON schema is valid for UserSkillMap API getting by userId")
	public void json_schema_is_valid_for_user_skill_map_api_getting_by_user_id() {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
		System.out.println("list of all users"+response.asPrettyString());
	}

	@Then("User receives status code {int} for UserSkillMap API getting by userId")
	public void user_receives_status_code_for_user_skill_map_api_getting_by_user_id(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}

	@When("User sends the request with a non-existing userId for UserSkillMap")
	public void user_sends_the_request_with_a_non_existing_user_id_for_user_skill_map() {
	
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1,8);
		System.out.println("/UserSkillsMap/" + data);
		response = request.get("/UserSkillsMap/" + data);
	}

	@Then("User receives status code {int} for UserSkillMap by non-existing userId")
	public void user_receives_status_code_for_user_skill_map_by_non_existing_user_id(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode()+int1.intValue());
	}

	@Given("user set GET method with endpoint\\/url\\/UserskillsMap\\/skillId")
	public void user_set_get_method_with_endpoint_url_userskills_map_skill_id() {
		data = populateDataFromExcel();
		request=util.requestSpecification(request); 
	}

	@When("User sends the request with skillId for UserSkillMap")
	public void user_sends_the_request_with_skill_id_for_user_skill_map() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 9);
		int i= Math.round(Float.parseFloat(data));
		System.out.println("/UserSkillsMap/" + i);
		response = request.get("/UsersSkillsMap/" + String.valueOf(i));
	}	

	@Then("JSON schema is valid for UserSkillMap API getting by skillId")
	public void json_schema_is_valid_for_user_skill_map_api_getting_by_skill_id() {
		boolean isJsonString = isJSONValid(response.asString());
		Assert.assertTrue(isJsonString);
		System.out.println("list of all users"+response.asPrettyString());
	}

	@Then("User receives status code {int} for UserSkillMap API getting by skillId")
	public void user_receives_status_code_for_user_skill_map_api_getting_by_skill_id(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}

	@When("User sends the request with a non-existing skillId for UserSkillMap")
	public void user_sends_the_request_with_a_non_existing_skill_id_for_user_skill_map() {
		
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 10);
		int i= Math.round(Float.parseFloat(data));
		System.out.println("/UsersSkillsMap/" +String.valueOf(i));
		response = request.get("/UsersSkillsMap/" +String.valueOf(i));
	}
	

	@Then("User receives status code {int} for UserSkillMap API by non-existing skillId")
	public void user_receives_status_code_for_user_skill_map_api_by_non_existing_skill_id(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}
	
	@Then("User receives status code as {int} for UserSkillMap API")
	public void user_receives_status_code_as_for_user_skill_map_api(Integer int1) {
		Assert.assertEquals(response.statusCode(), int1.intValue());
		System.out.println("Status code"+response.statusCode());
	}

	
}
