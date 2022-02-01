package stepDefinition.DBValidation;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
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
import utils.DBRestConnect;
import utils.ExcelReader;

public class SkillPostDB {

	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_skillname = null;
	String RequestString_skillname = null;
	String RequestString_message = null;
	DBRestConnect db = new DBRestConnect();
	private AuthenticationUtil util = new AuthenticationUtil();
	private Response response;
	private RequestSpecification request;
	private static final String FILE_NAME = "./src/test/resources/Exceldata/DBValidationData.xlsx";
	private static final String SHEET_NAME = "SkillPost";

	
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
			

			String validSkillData = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("skill_name", validSkillData);
			
			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			System.out.println("statusCodeNumber :: " + statusCodeNumber);
			innerDataMap.put("statusCode", statusCodeNumber);
			
			dataMap.put(dataKey, innerDataMap);
		}

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
		response = request.post("/Skills");	
		System.out.println("PostUserAPI :: response :: " +response.asString());

	}

	private static boolean isJSONValid(String serverresponse) {
		try {
			new JSONObject();
		} catch (JSONException ex) {
			try {
				new JSONArray(serverresponse);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
	
	
	@Given("New skill is added with Post request")
	public void new_skill_is_added_with_post_request() {
		
		System.out.println("SkillPost :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validSkillData");
		postRequest(validDataMap);	

		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_skillname = jsonPathEvaluator.get("skill_name");
		RequestString_message= jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_skillname);
		System.out.println("Json element value in response = " + RequestString_message);
		
		
	}

	@When("New skill is queried from DB")
	public void new_skill_is_queried_from_db() {
		HashMap<String, Object> validDataMap = data.get("validSkillData");
		if(response.statusCode() == 201) {
		try {
			
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='"+validDataMap.get("skillName")+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='"+validDataMap.get("skillName")+"'", "last_mod_time");
			sqlString_skillname = db.connect("SELECT * FROM tbl_lms_skill_master where skill_name='"+validDataMap.get("skillName")+"'", "skill_name");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_skillId = " + sqlString_skillname);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}else {
		System.out.println("Skill name not added");
	}
	
	}

	@Then("Compare and Assert request sent and DB skill details")
	public void compare_and_assert_request_sent_and_db_skill_details() {
		
		Assert.assertEquals(sqlString_skillname.trim(), RequestString_skillname.trim());
	   
	}

	
	
	
}
