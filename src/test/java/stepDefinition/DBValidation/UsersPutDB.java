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

public class UsersPutDB {

	DBRestConnect db = new DBRestConnect();
	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String[] username = null;
	String sqlString_name = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	String RequestString1_updated = null;
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();

	private static final String FILE_NAME = "./src/test/resources/Exceldata/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UsersPut";
	

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

			String userName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("name", userName);

			String phone_number = reader.getCellData(SHEET_NAME, 2, rowNum);
			long phoneNumber = 0L;
			if (!phone_number.equals(""))
				phoneNumber = Math.round(Double.parseDouble(phone_number));
			innerDataMap.put("phone_number", phoneNumber);

			String location = reader.getCellData(SHEET_NAME, 3, rowNum);
			innerDataMap.put("location", location);

			String time_zone = reader.getCellData(SHEET_NAME, 4, rowNum);
			innerDataMap.put("time_zone", time_zone);

			String linkedin_url = reader.getCellData(SHEET_NAME, 5, rowNum);
			innerDataMap.put("linkedin_url", linkedin_url);

			String education_ug = reader.getCellData(SHEET_NAME, 6, rowNum);
			innerDataMap.put("education_ug", education_ug);

			String education_pg = reader.getCellData(SHEET_NAME, 7, rowNum);
			innerDataMap.put("education_pg", education_pg);

			String visa_status = reader.getCellData(SHEET_NAME, 8, rowNum);
			innerDataMap.put("visa_status", visa_status);

			String comments = reader.getCellData(SHEET_NAME, 9, rowNum);
			innerDataMap.put("comments", comments);

			String userId = reader.getCellData(SHEET_NAME, 10, rowNum);
			
			innerDataMap.put("userId", userId);
			
			String statusCode = reader.getCellData(SHEET_NAME, 11, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
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
		response = request.put("/Users/" + map.get("userId"));
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
	

	@Given("User details are modified with username for existing user with Put request")
	public void user_details_are_modified_with_username_for_existing_user_with_put_request() {
		
		System.out.println("UserPost :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validUserData");
		putRequest(validDataMap);	
		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		System.out.println("The actual response status code is : " + response.getStatusCode());
		

			JsonPath jsonPathEvaluator = response.jsonPath();
			RequestString_userid = jsonPathEvaluator.get("user_id");
			RequestString_message = jsonPathEvaluator.get("message_response");
			RequestString1_updated = jsonPathEvaluator.get("name");
			String[] username = RequestString1_updated.split(",");
			
			System.out.println("Json element value in response = " + username[0]);
			System.out.println("Json element value in response = " + username[1]);
			System.out.println("Json element value in response = " + RequestString_message);
			System.out.println("Json element value in response = " + RequestString_userid);
			
		}

	

	@When("Modified user with valid username details are queried from DB With creation and updation times")
	public void modified_user_with_valid_username_details_are_queried_from_db_with_creation_and_updation_times() {
		HashMap<String, Object> validDataMap = data.get("validUserData");
		
		
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+validDataMap.get("userId")+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_id='"+validDataMap.get("userId")+"'", "last_mod_time");
			sqlString_name = db.connect("SELECT * FROM tbl_lms_user where user_id='"+validDataMap.get("userId")+"'", "user_first_name");
		
		
			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_name);
		
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Then("DB details are compared with updated username details and asserted")
	public void db_details_are_compared_with_updated_username_details_and_asserted() {
		Assert.assertEquals(sqlString_name.trim(), username[0].trim());
	}


}

	



