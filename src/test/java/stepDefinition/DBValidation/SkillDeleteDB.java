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

public class SkillDeleteDB {

	DBRestConnect db = new DBRestConnect();
	String sqlString = null;
	String deleteRequestString = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "SkillDel";

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

			String validSkillId = reader.getCellData(SHEET_NAME, 1, rowNum);
			int validSkillid = Math.round(Float.parseFloat(validSkillId));
			System.out.println(validSkillid);
			innerDataMap.put("value", validSkillid);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
		
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			System.out.println("statusCodeNumber :: " + statusCodeNumber);
			innerDataMap.put("statusCode", statusCodeNumber);

			dataMap.put(dataKey, innerDataMap);
		}
		
		return dataMap;
	}
	
	@Given("Skill is deleted with DELETE request")
	public void skill_is_deleted_with_delete_request() {
		data = populateDataFromExcel();
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("validSkillId");
		
		response = request.delete("/Skills/" +validDataMap.get("value"));
		
		System.out.println("UserSkillMap::delRequest :: response :: " +response.asString());
		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		deleteRequestString = jsonPathEvaluator.get("message_response");
		System.out.println("Json element value in response = " + deleteRequestString);

	
	}

	@When("Query the DataBase with user skillid")
	public void query_the_data_base_with_user_skillid() {
		try {
			HashMap<String, Object> validDataMap = data.get("validSkillId");
		
			sqlString = db.connect("SELECT * FROM tbl_lms_user where skill_id='"+validDataMap.get("value")+"'", "skill_id");

			System.out.println("received SqlString = " + sqlString);
			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}

	@Then("Request response message and DataBase response are asserted")
	public void request_response_message_and_data_base_response_are_asserted() {
		 
		   Assert.assertEquals(deleteRequestString.trim(),"The record has been deleted !!");
		   Assert.assertNull(sqlString);
		}
}
