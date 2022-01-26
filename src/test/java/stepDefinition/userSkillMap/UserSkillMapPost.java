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

public class UserSkillMapPost {
	
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserSkillMapDataExcel.xlsx";
	private static final String SHEET_NAME = "post";
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
			
			String NoOfExpSkillMapPost = reader.getCellData(SHEET_NAME, 1, rowNum);
			int noofexpSkillMapPost=0;
			if(!NoOfExpSkillMapPost.equals(""))
			noofexpSkillMapPost=Math.round(Float.parseFloat(NoOfExpSkillMapPost));
			innerDataMap.put("months_of_exp",noofexpSkillMapPost);
			
		
			String  SkillIdSkillMapPost=reader.getCellData(SHEET_NAME, 2, rowNum);
			long skillidSkillMapPost=0L;
			if(!SkillIdSkillMapPost.equals(""))
				skillidSkillMapPost=Math.round(Double.parseDouble(SkillIdSkillMapPost));
			System.out.println("Skillid"+skillidSkillMapPost);
			innerDataMap.put("skill_id",(int)skillidSkillMapPost);
			
			String UserIdSkillMapPost=reader.getCellData(SHEET_NAME, 3, rowNum);
			if(!UserIdSkillMapPost.equals(""))
			innerDataMap.put("user_id", UserIdSkillMapPost);
			
			
			String statuscodeSkillMapPost = reader.getCellData(SHEET_NAME, 4, rowNum);
			int statusCodeNumberSkillMapPost=0;
			if(!statuscodeSkillMapPost.equals(""))
			statusCodeNumberSkillMapPost = Math.round(Float.parseFloat(statuscodeSkillMapPost));
			innerDataMap.put("statuscode", statusCodeNumberSkillMapPost);
			dataMap.put(dataKey, innerDataMap);
		}
		return dataMap;
		}
	private void postRequest(HashMap<String, Object> map) 
	{
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON = null;
		try {
			expectedJSON = mapper.writeValueAsString(map);
			System.out.println("UserSkillMapPostApi:postRequest :: map :: " + expectedJSON);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean isJsonString = isJSONValid(expectedJSON);
		
		request = util.requestSpecification(request);
			if(isJsonString) {
				request.header("Content-Type", "application/json");
				request.body(map);
			}
			response = request.post("/UserSkills");	
			System.out.println("UserSkillMapPostApi:postRequest :: response :: " +response.asString());
		
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
	@Given("User is on Post request with endpoint \\/url\\/UserSkills for SkilllMap Post")
	public void user_is_on_post_request_with_endpoint_url_user_skills_for_skilll_map_post() {
		System.out.println("UserSkillMapPOst :: inside Given");
		data = populateDataFromExcel();
	}
//Valid Data
	@When("User sends input with valid JSON body from valid excel for SkilllMap Post")
	public void user_sends_input_with_valid_json_body_from_valid_excel_for_user_skill_map() {
		HashMap<String, Object> validDataMap = data.get("validId");
		System.out.println("UserSkillMapPost :: validDataMap"+validDataMap);
		postRequest(validDataMap);		
	
	}

	@Then("User recieves a {int} valid status code for SkilllMap Post")
	public void user_recieves_a_valid_status_code_for_skilll_map_post(Integer int1) {
		
		HashMap<String, Object> validDataMap = data.get("validId");
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
	}
	

	@Then("valid json response for SkilllMap Post")
	public void valid_json_response_for_skilll_map_post() {
		//HashMap<String, Object> validDataMap = data.get("validId");
		
		System.out.println("The actual response Body  is : "+response.getBody().asPrettyString());
	}

//Invalid data
	@When("User sends input with alphanumeric skillid for SkilllMap Post")
	public void user_sends_input_with_alphanumeric_skillid_for_skilll_map_post() {
		recordName ="nonExistingId";
		HashMap<String, Object> nonexistingIdhash = data.get("nonExistingId");
		postRequest(nonexistingIdhash);
	}

	@When("User sends input with null skillid for SkilllMap Post")
	public void user_sends_input_with_null_skillid_for_skilll_map_post() {
		recordName ="blankSkillId";
		HashMap<String, Object> blanksid = data.get("blankSkillId");
		postRequest(blanksid);
	}

	@When("User sends input with null userid for SkilllMap Post")
	public void user_sends_input_with_null_userid_for_skilll_map_post() {
		recordName ="blankUserId";
		HashMap<String, Object> blankUserIdhash = data.get("blankUserId");
		postRequest(blankUserIdhash);
	}

	@When("User sends input with alphanumeric months of experience for SkilllMap Post")
	public void user_sends_input_with_alphanumeric_months_of_experience_for_SkilllMap_Post() {
		recordName ="decimalUserId";
		HashMap<String, Object> decimalUserIdhash = data.get("decimalUserId");
		postRequest(decimalUserIdhash);
	}

	@When("User sends input with null months of experience for SkilllMap Post")
	public void user_sends_input_with_null_months_of_experience_for_skilll_map_post() {
		recordName ="blankNoofExp";
		HashMap<String, Object> blankmonthhash = data.get("blankNoofExp");
		postRequest(blankmonthhash);
	}

	@Then("User recieves a bad request status code {int} for SkilllMap Post")
	public void user_recieves_a_bad_request_status_code_for_skilll_map_post(Integer int1) {

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
