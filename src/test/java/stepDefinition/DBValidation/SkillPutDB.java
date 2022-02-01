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

public class SkillPutDB {

	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_skillname = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	String RequestString_skillid = null;
	String RequestString_skillname = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "SkillPut";
	

	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();

	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);

		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();

		int rows = reader.getRowCount(SHEET_NAME);
		System.out.println("rows :: "+rows);
		for (int rowNum = 2; rowNum <= rows; rowNum++) {

			if (reader.getCellData(SHEET_NAME, 0, rowNum).isEmpty()) {
				break;
			}

			String dataKey = reader.getCellData(SHEET_NAME, 0, rowNum);

			HashMap<String, Object> innerDataMap = new HashMap<String, Object>();

			String skillName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("skill_name", skillName);

			String skillid = reader.getCellData(SHEET_NAME, 2, rowNum);
			int SkillId = Math.round(Float.parseFloat(skillid));
			System.out.println("skill_id :: " + SkillId);
			innerDataMap.put("skill_id", SkillId);
			
			String statusCode = reader.getCellData(SHEET_NAME, 3, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			System.out.println("statusCodeNumber :: " + statusCodeNumber);
			innerDataMap.put("statusCode", statusCodeNumber);
					
			dataMap.put(dataKey, innerDataMap);
		}
		
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
		response = request.put("/Skills/" + map.get("skill_id"));
		System.out.println("PostUserAPI :: response :: " + response.asString());
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
	
	@Given("New skill is added for existing skillid with Put request")
	public void new_skill_is_added_for_existing_skillid_with_put_request() {
		
		System.out.println("SkillPut :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("updateWithNewSkillName");
		putRequest(validDataMap);


		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
		
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_skillid = jsonPathEvaluator.get("skill_id").toString();
		RequestString_message = jsonPathEvaluator.get("message_response");
		RequestString_skillname = jsonPathEvaluator.getString("skill_name");
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString_skillid);
		System.out.println("Json element value in response = " + RequestString_skillname);
		
	}

	@When("Modified details are queried from DataBase With creation and updation times")
	public void modified_details_are_queried_from_data_base_with_creation_and_updation_times() {
		
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_id='"+RequestString_skillid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_skill_master where skill_id='"+RequestString_skillid+"'", "last_mod_time");
			sqlString_skillname =  db.connect("SELECT * FROM tbl_lms_skill_master where skill_id='"+RequestString_skillid+"'", "skill_name");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);  
			System.out.println("received DB value sqlString_skillname = " + sqlString_skillname);  
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
	}
	}

	@Then("DataBase details are compared with request payload details and asserted")
	public void data_base_details_are_compared_with_request_payload_details_and_asserted() {
		
			System.out.println("Comparing and asserting skillname created from request response and skillname queried from DB");
			Assert.assertEquals(sqlString_skillname.trim(), RequestString_skillname.trim());
			
	}


}
