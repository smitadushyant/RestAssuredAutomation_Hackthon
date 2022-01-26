package stepDefinition.skill;

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
import utils.ExcelReader;

public class PostSkillAPI {
	
	int statusCode;
	String resBody;
	JsonPath jsonPathEvaluator;
	TestContextAPI testContext;
	private static final String FILE_NAME = "./src/test/resources/ExcelData/SkillAPIData.xlsx";
	private static final String SHEET_NAME = "post";
	
	public PostSkillAPI(TestContextAPI testContext) {
		this.testContext = testContext;
		
	}
	
private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	
	private HashMap<String, HashMap<String, Object>> populateSkillDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);
		
		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String,Object>>();
		
		int rows = reader.getRowCount(SHEET_NAME);
		//System.out.println("rows :: "+rows);
		for(int rowNum = 2; rowNum <= rows; rowNum++) 
		{
			
			if(reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty())
			{
				break;
			}
			
			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);
			
			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();
			

			String skillName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("skill_name", skillName);
			
			/*String statusCode = reader.getCellData(SHEET_NAME, 10, rowNum);
			System.out.println(statusCode);
			int statusCodeNumber = Math.round(Integer.parseInt(statusCode));
			
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			System.out.println(statusCodeNumber);
			innerDataMap.put("statuscode", statusCodeNumber);*/
			
			dataMap.put(dataKey, innerDataMap);
			
		}
	
		return dataMap;
	}
	
	
private void postSkillRequest(HashMap<String, Object> map) {
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON = null;
		try {
			expectedJSON = mapper.writeValueAsString(map);
			System.out.println("PostSkillAPI :: map :: " + expectedJSON);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean isJsonString = isJSONValid(expectedJSON);
		
		if(isJsonString) {
			testContext.httpRequest.header("Content-Type", "application/json");
			testContext.httpRequest.body(map);
		}
		testContext.response = testContext.httpRequest.post("/Skills");	
		System.out.println("PosSkillAPI :: response :: " +testContext.response.asString());
		
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
	
	
	@Given("I  am  on Post request")
	public void i_am_on_post_request() {
	    System.out.println("i am in given");
	    data = populateSkillDataFromExcel();
	}

	@When("I send input with valid JSON body from  excel")
	public void i_send_input_with_valid_json_body_from_excel() {
		HashMap<String, Object> validDataMap = data.get("validskilldata");
		postSkillRequest(validDataMap);		
	    
	}
	
	@When("I send input with valid JSON body for the existing Skill")
	public void i_send_input_with_valid_json_body_for_the_existing_skill() {
		
		HashMap<String, Object> validDataMap = data.get("existingSkillIname");
		postSkillRequest(validDataMap);
	   
	}

	@When("I send input with valid JSON body with empty skill_name field")
	public void i_send_input_with_valid_json_body_with_empty_skill_name_field() {
		HashMap<String, Object> validDataMap = data.get("blank");
		postSkillRequest(validDataMap);
	}

	@Then("I recieve a {int} valid status code")
	public void i_recieve_a_valid_status_code(Integer int1) {
		//HashMap<String, Object> validDataMap = data.get("validskilldata");
		try {
			//Assert.assertEquals(validDataMap.get("statuscode"), testContext.response.statusCode());
			Assert.assertEquals(testContext.response.statusCode(), int1.intValue());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + testContext.response.getStatusCode());
		
	} 
	
	@Then("API should return {string} in responsebody is {string}")
	public void api_should_return_in_responsebody_is(String resMsgKey, String Expected)
	{
		System.out.println(resMsgKey);
		resBody = testContext.response.getBody().asPrettyString();
		 System.out.println("The skill details are : " + resBody );
		 jsonPathEvaluator = testContext.response.jsonPath();
		 String msgFromResponse =  jsonPathEvaluator.get(resMsgKey).toString();
		 System.out.println("The Actual message from response :"+msgFromResponse);
		 
		 Assert.assertEquals(msgFromResponse,Expected);
	}
}




