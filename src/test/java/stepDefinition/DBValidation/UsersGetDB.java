package stepDefinition.DBValidation;

import java.util.HashMap;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.DBRestConnect;
import utils.ExcelReader;

public class UsersGetDB {
	DBRestConnect db = new DBRestConnect();
	private AuthenticationUtil util = new AuthenticationUtil();
	String sqlString = null;
	String getRequestString = null;
	private Response response;
	private RequestSpecification request;
	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UsersGet";


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

			String userId = reader.getCellData(SHEET_NAME, 1, rowNum);
			System.out.println(userId);
			innerDataMap.put("value", userId);

			String statusCode = reader.getCellData(SHEET_NAME, 2, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
			innerDataMap.put("statusCode", statusCodeNumber );

			dataMap.put(dataKey, innerDataMap);
		}

		return dataMap;
	}

	@Given("Expected value is queried from DB")
	public void expected_value_is_queried_from_db() {
		
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validUserId");
		try {
			sqlString = db.connect("SELECT * FROM tbl_lms_user where user_id='"+validDataMap.get("value")+"'", "user_id");

			System.out.println("received SqlString = " + sqlString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@When("GET request is sent to get actual response")
	public void get_request_is_sent_to_get_actual_response() {
		
		data = populateDataFromExcel();
		request = util.requestSpecification(request);
		HashMap<String, Object> validDataMap = data.get("validUserId");
		response = request.get("/Users/" + validDataMap.get("value"));
		System.out.println("valid User: "+validDataMap.get("value"));
		getRequestString = validDataMap.get("value").toString();
		System.out.println("The response for userid request is :" +response.asPrettyString());
		Assert.assertNotNull(response);
	}

	@Then("Compare and Assert actual and expected values")
	public void compare_and_assert_actual_and_expected_values() {
		
			Assert.assertEquals(sqlString.trim(), getRequestString.trim());
	
	}
}
