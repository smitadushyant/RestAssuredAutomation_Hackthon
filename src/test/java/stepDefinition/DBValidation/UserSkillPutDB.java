package stepDefinition.DBValidation;

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
import utils.DBRestConnect;
import utils.ExcelReader;

public class UserSkillPutDB {
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString = null;
	String RequestString_userskillid = null;
	String RequestString_message = null;
	String RequestString = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UserSkillMapPut";

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
	
	@Given("new months of exp is updated with put request for existing user skill id")
	public void new_months_of_exp_is_updated_with_put_request_for_existing_user_skill_id() {
		
		System.out.println("UserSkillMapPut :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validId");
		putRequest(validDataMap);
		try {
			Assert.assertEquals(validDataMap.get("statuscode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode()+response.getBody().asPrettyString());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userskillid = jsonPathEvaluator.get("user_skill_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		RequestString=jsonPathEvaluator.get("months_of_exp").toString();
		System.out.println("Json element value in response = " + RequestString_userskillid);
		System.out.println("Json element value in response = " + RequestString_message);
		System.out.println("Json element value in response = " + RequestString);


	   
	}

	@When("DB is queried for updated field")
	public void db_is_queried_for_updated_field() {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "last_mod_time");
			sqlString = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "months_of_exp");
			
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString= " + sqlString);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Then("Put Response and DB query results are asserted")
	public void put_response_and_db_query_results_are_asserted() {
		
		Assert.assertEquals(sqlString.trim(), RequestString.trim());
	   
	}
	

}
