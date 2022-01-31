package stepDefinition.DBValidation;

import java.util.HashMap;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.DBRestConnect;
import utils.ExcelReader;

public class UserSkillDeleteDB {
	
DBRestConnect db = new DBRestConnect();
	
	String sqlString_userskillid = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UserSkillMapDel";
	
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

			String userSkillId = reader.getCellData(SHEET_NAME, 1, rowNum);
			System.out.println(userSkillId);
			innerDataMap.put("UserSkillId", userSkillId);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statusCode", statusCodeNumber);

			dataMap.put(dataKey, innerDataMap);
		}
		
		return dataMap;
	}
	
	@Given("existing user skill id is deleted with delete request")
	public void existing_user_skill_id_is_deleted_with_delete_request() {
		data = populateDataFromExcel();
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("ValidUserSkills");
		response = request.delete("/UserSkills/" + validDataMap.get("UserSkillId"));
		System.out.println("UserSkillMap::delRequest :: response :: " +response.asString());
		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		RequestString_message = jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + RequestString_message);
		
	}

	@When("DB is queried deleted field")
	public void db_is_queried_deleted_field() {
		HashMap<String, Object> validDataMap = data.get("ValidUserSkills");
		try {
			sqlString_userskillid = db.connect("SELECT * FROM tbl_lms_userskill_map where user_Skill_id='"+validDataMap.get("UserSkillId") +"'", "user_skill_id");

			System.out.println("received SqlString = " + sqlString_userskillid);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("Delete Response and DB query results are asserted")
	public void response_and_db_query_results_are_asserted() {
	    
		 Assert.assertEquals(RequestString_message.trim(),"The record has been deleted !!");
		   Assert.assertNull(sqlString_userskillid);
	}

}
