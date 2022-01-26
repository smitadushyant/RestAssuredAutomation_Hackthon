package stepDefinition.userSkillMap;

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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.ExcelReader;

public class UserSkillMapPut {

	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserSkillMapDataExcel.xlsx";
	private static final String SHEET_NAME = "put";
	private static String recordName;
	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	
	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		
		ExcelReader reader = new ExcelReader(FILE_NAME);
		
		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String,Object>>();
		
		int rows = reader.getRowCount(SHEET_NAME);
		
		for(int rowNum = 2; rowNum <= rows; rowNum++) {
			
			if(reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}
			
			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);
			
			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();
			
			String UserSkillIdPutXcel = reader.getCellData(SHEET_NAME, 1, rowNum);
			
			if(!UserSkillIdPutXcel.equals(""))
			
			innerDataMap.put("user_skill_id",UserSkillIdPutXcel);
			
			
			String  SkillIdPutxcel=reader.getCellData(SHEET_NAME, 3, rowNum);
			long skillidPut=0L;
			if(!SkillIdPutxcel.equals(""))
			skillidPut=Math.round(Double.parseDouble(SkillIdPutxcel));
			System.out.println("Skillid"+skillidPut);
			innerDataMap.put("skill_id",(int)skillidPut);
			
			String UserIdSkillPut=reader.getCellData(SHEET_NAME,2, rowNum);
			if(!UserIdSkillPut.equals(""))
			innerDataMap.put("user_id", UserIdSkillPut);
			
			String NoOfExpSkillPutxcel = reader.getCellData(SHEET_NAME, 4, rowNum);
			int noofexpSkillPut=0;
			if(!NoOfExpSkillPutxcel.equals(""))
			noofexpSkillPut=Math.round(Float.parseFloat(NoOfExpSkillPutxcel));
			innerDataMap.put("months_of_exp",noofexpSkillPut);
			
			String statuscodeSkillPut = reader.getCellData(SHEET_NAME, 5 , rowNum);
			int statusCodeNumberSkillPut=0;
			if(!statuscodeSkillPut.equals(""))
			statusCodeNumberSkillPut = Math.round(Float.parseFloat(statuscodeSkillPut));
			innerDataMap.put("statuscode", statusCodeNumberSkillPut);
			
			dataMap.put(dataKey, innerDataMap);
		}
		return dataMap;
		}
	private void putRequest(HashMap<String, Object> map) 
	{
		//System.out.println("Map::::::::::::"+map);
		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON = null;
		try {
			expectedJSON = mapper.writeValueAsString(map);
			System.out.println("UserSkillMapPuttApi:putRequest :: map :: " + expectedJSON);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean isJsonString = isJSONValid(expectedJSON);
		
		request = util.requestSpecification(request);
			if(isJsonString) {
				request.header("Content-Type", "application/json");
				request.body(map);
			}
			
			response = request.put("/UserSkills/"+map.get("user_skill_id"));	
			System.out.println("UserSkillMapPutApi:putRequest :: response :: " +response.asString());
		
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

@Given("User is on PUT request with endpoint  \\/url\\/UserSkills\\/userskillid for UserSkillMap API for UserSkillMap API")
public void user_is_on_put_request_with_endpoint_url_user_skills_userskillid_for_user_skill_map_api_for_user_skill_map_api() {

	System.out.println("UserSkillMapPut :: inside Given");
	data = populateDataFromExcel();
}

@When("User sends input with valid userskillid and valid JSON body from valid excel for UserSkillMap to update record for UserSkillMap API")
public void user_sends_input_with_valid_userskillid_and_valid_json_body_from_valid_excel_for_user_skill_map_to_update_record_for_user_skill_map_api() {
	HashMap<String, Object> validDataMap = data.get("validId");
	putRequest(validDataMap);
}

@Then("User recieves a {int} valid status code for UserSkillMap API")
public void user_recieves_a_valid_status_code_for_user_skill_map_api(Integer int1) {
	HashMap<String, Object> validDataMap = data.get("validId");
	try {
		Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
	}catch(AssertionError ex) {
		ex.printStackTrace();
		System.out.println("Assertion Failed !!");
	}
	System.out.println("The actual response status code is : " + response.getStatusCode()+response.getBody().asPrettyString());

}

@Then("valid json response for UserSkillMap API")
public void valid_json_response_for_user_skill_map_api() {
	HashMap<String, Object> validDataMap = data.get("validId");
	try {
		Assert.assertEquals(validDataMap.get(("statuscode")), response.statusCode());
	}catch(AssertionError ex) {
		ex.printStackTrace();
		System.out.println("Assertion Failed !!");
	}
	System.out.println("The actual response status code is : " + response.getStatusCode());

}

@When("User sends input with non-existing userskillid to update record for UserSkillMap API")
public void user_sends_input_with_non_existing_userskillid_to_update_record_for_user_skill_map_api() {
	recordName ="nonExistingUserSkillId";
	HashMap<String, Object> nonexistingUSIdhashput = data.get("nonExistingUserSkillId");
	putRequest(nonexistingUSIdhashput);
	
}

@Then("User recieves request status code {int} Not found for UserSkillMap API")
public void user_recieves_request_status_code_not_found_for_user_skill_map_api(Integer int1) {
	HashMap<String, Object> validDataMap = data.get("nonExistingUserSkillId");
	try {
		Assert.assertEquals(validDataMap.get(("statuscode")), response.statusCode());
	}catch(AssertionError ex) {
		ex.printStackTrace();
		System.out.println("Assertion Failed !!");
	}
	System.out.println("The actual response status code is : " + response.getStatusCode());

}


@When("User sends input with invalid userskillid to update record for UserSkillMap API")
public void user_sends_input_with_invalid_userskillid_to_update_record_for_user_skill_map_api() {
	recordName ="invalidMonth";
	HashMap<String, Object> invalidUSIdhashput = data.get("invalidMonth");
	putRequest(invalidUSIdhashput);
}

@Then("User recieves a bad request status code {int} for UserSkillMap API")
public void user_recieves_a_bad_request_status_code_for_user_skill_map_api(Integer int1) {
	HashMap<String, Object> badURL = data.get(recordName);
	try {
		Assert.assertEquals(badURL.get("statuscode"), response.statusCode());
	}catch(AssertionError ex) {
		ex.printStackTrace();
		System.out.println("Assertion Failed !!");
	}
	System.out.println("The actual response status code is : " + response.getStatusCode());
}


@When("User sends input with valid userskillid and valid months of experience to update record for UserSkillMap API")
public void user_sends_input_with_valid_userskillid_and_valid_months_of_experience_to_update_record_for_user_skill_map_api() {
	recordName ="validMonthandSkill";
	HashMap<String, Object> validMSIdhashput = data.get("validMonthandSkill");
	putRequest(validMSIdhashput);
	
}

@When("User sends input with valid userskillid and invalid months of experience to update record for UserSkillMap API")
public void user_sends_input_with_valid_userskillid_and_invalid_months_of_experience_to_update_record_for_user_skill_map_api() {
	recordName ="invalidMonth";
	HashMap<String, Object> invalidMonthIdhashput = data.get("invalidMonth");
	putRequest(invalidMonthIdhashput);
	
}
}