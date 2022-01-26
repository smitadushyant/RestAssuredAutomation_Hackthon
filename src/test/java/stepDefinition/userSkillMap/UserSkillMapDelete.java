package stepDefinition.userSkillMap;
import java.util.HashMap;

import org.junit.Assert;

import utils.AuthenticationUtil;
import utils.ExcelReader;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillMapDelete {
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserSkillMapDataExcel.xlsx";
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
			innerDataMap.put("user_skill_id", userId);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
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
	
	@Given("Set Delete request endpoint to \\/UserSkills\\/id")
	public void set_delete_request_endpoint_to_user_skills_id() {
		// Read data from excel
		data = populateDataFromExcel();
		
	}
	

	@When("User sends request with valid userSkillId for UserSkillMap Delete API")
	public void user_sends_request_with_valid_user_skill_id_for_user_skill_map_delete_api() {
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("ValidUserSkills/id");
		response = request.delete("/UserSkills/" + validDataMap.get("ValidUserSkills/id"));
		  
	}

	@Then("User should receive a valid response code 200 for UserSkillMap Delete API")
	public void user_should_receive_a_valid_response_code_for_user_skill_map_delete_api() {
		HashMap<String, Object> validDataMap = data.get("ValidUserSkills/id");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
		
		
	
	@When("User sends request with invalid userSkillId for UserSkillMap Delete API")
	public void user_sends_request_with_invalid_user_skill_id_for_user_skill_map_delete_api() {
		request = util.requestSpecification(request);
		HashMap<String, Object> inValidDataMap = data.get("InvalidUserSkills/id");

		response = request.delete("/UserSkills/" + inValidDataMap.get("InvalidUserSkills/id"));
	}
		
		
	@When("User sends request with non-existing userSkillId for UserSkillMap Delete API")
	public void user_sends_request_with_non_existing_user_skill_id_for_user_skill_map_delete_api() {
		request = util.requestSpecification(request);
		HashMap<String, Object> nonExistDataMap = data.get("Non_ExistingUserSkills/id");

		response = request.delete("/UserSkills/" + nonExistDataMap.get("Non_ExistingUserSkills/id"));
	
		
			
	}
	
	@When("User sends request with decimal userSkillId for UserSkillMap Delete API")
	public void user_sends_request_with_decimal_user_skill_id_for_user_skill_map_delete_api() {
		request = util.requestSpecification(request);
		HashMap<String, Object> decimalDataMap = data.get("DecimalUserSkill/id");

		response = request.delete("/UserSkills/" + decimalDataMap.get("DecimalUserSkill/id"));
	
	}

	@Then("User should receive a response code {int} for UserSkillMap Delete API")
	public void user_should_receive_a_response_code_for_user_skill_map_delete_api(Integer code) {
		//HashMap<String, Object> validDataMap = data.get("validId");
		try {
			Assert.assertEquals(code.intValue(), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
		
	

	@When("User sends request with blank userSkillId for UserSkillMap Delete API")
	public void user_sends_request_with_blank_user_skill_id_for_user_skill_map_delete_api() {
		request = util.requestSpecification(request);
		//HashMap<String, Object> validDataMap = data.get("blankId");
		response = request.delete("/UserSkills/");
	}

	@Then("User should receive 405 Method Not Allowed status code for UserSkillMap Delete API")
	public void user_should_receive_method_not_allowed_status_code_for_user_skill_map_delete_api() {
		HashMap<String, Object> validDataMap = data.get("BlankUserSkills/id");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}

	

}
