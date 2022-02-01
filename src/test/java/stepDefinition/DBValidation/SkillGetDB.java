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

public class SkillGetDB {

	
	DBRestConnect db = new DBRestConnect();
	private AuthenticationUtil util = new AuthenticationUtil();
	String sqlString = null;
	String getRequestString = null;
	private Response response;
	private RequestSpecification request;
	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "SkillGet";

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

			String validSkillId = reader.getCellData(SHEET_NAME, 1, rowNum);
			System.out.println(validSkillId);
			innerDataMap.put("value", validSkillId);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statusCode", statusCodeNumber );

			dataMap.put(dataKey, innerDataMap);
		}

		return dataMap;
	}
	
	@Given("Expected value is queried from DataBase")
	public void expected_value_is_queried_from_data_base() {
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validSkillId");
		int value = Math.round(Float.parseFloat((String) validDataMap.get("value")));
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_skill_master where skill_id="+value, "skill_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@When("GET request is sent to get actual response code")
	public void get_request_is_sent_to_get_actual_response_code() {
		data = populateDataFromExcel();
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("validSkillId");
		int value = Math.round(Float.parseFloat((String) validDataMap.get("value")));
		response = request.get("/Skills/" + value);
		System.out.println("valid Skill id : "+value);
		getRequestString = validDataMap.get("value").toString();
		Assert.assertNotNull(response);
		

		System.out.println("Server response for GET skill = " + response.asString());
		
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		getRequestString = jsonPathEvaluator.get("skill_id").toString();
		System.out.println("Json element value in response = " + getRequestString);

	}

	@Then("Compare and Assert actual value and expected value")
	public void compare_and_assert_actual_value_and_expected_value() {
		Assert.assertEquals(sqlString.trim(), getRequestString.trim());
	}


}
