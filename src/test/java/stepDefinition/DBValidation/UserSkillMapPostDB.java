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

public class UserSkillMapPostDB {
	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_userskillid = null;
	String RequestString_userskillid = null;
	String RequestString_message = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UserSkillMapPost";


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

			String UserIdSkillMapPost=reader.getCellData(SHEET_NAME, 3, rowNum);
			if(!UserIdSkillMapPost.equals(""))
				innerDataMap.put("user_id", UserIdSkillMapPost);
			
			String  SkillIdSkillMapPost=reader.getCellData(SHEET_NAME, 2, rowNum);
			long skillidSkillMapPost=0L;
			if(!SkillIdSkillMapPost.equals(""))
				skillidSkillMapPost=Math.round(Double.parseDouble(SkillIdSkillMapPost));
			System.out.println("Skillid"+skillidSkillMapPost);
			innerDataMap.put("skill_id",(int)skillidSkillMapPost);


			String statusCode = reader.getCellData(SHEET_NAME, 4, rowNum);
			int statusCodeNumber = 0;
			if(!statusCode.equals(""))
			statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statusCode", statusCodeNumber);
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
	@Given("new user is mapped with skill with post request")
	public void new_user_is_mapped_with_skill_with_post_request() {

		System.out.println("UserSkillMapPOst :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validId");
		System.out.println("UserSkillMapPost :: validDataMap"+validDataMap);
		postRequest(validDataMap);		
		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());



		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_userskillid = jsonPathEvaluator.get("user_skill_id");
		RequestString_message= jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_userskillid);
		System.out.println("Json element value in response = " + RequestString_message);

	}

	@When("DB is queried for new mapped user")
	public void DB_is_queried_for_new_mapped_user() {

		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "last_mod_time");
			sqlString_userskillid = db.connect("SELECT * FROM tbl_lms_userskill_map where user_skill_id='"+RequestString_userskillid+"'", "user_skill_id");
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userskillid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Then("Response and DB query results are asserted")
	public void Response_and_DB_query_results_are_asserted() {

		Assert.assertEquals(sqlString_userskillid.trim(), RequestString_userskillid.trim());
	}

}


