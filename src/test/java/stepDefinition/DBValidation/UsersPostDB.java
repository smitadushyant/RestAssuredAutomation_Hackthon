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

public class UsersPostDB {


	String sqlString_creationTime = null;
	String sqlString_modTime = null;
	String sqlString_userid = null;
	String[] name = null;
	String RequestString_userid = null;
	String RequestString_message = null;
	String RequestString_name = null;
	DBRestConnect db = new DBRestConnect();
	private AuthenticationUtil util = new AuthenticationUtil();
	private Response response;
	private RequestSpecification request;
	private static final String FILE_NAME = "./src/test/resources/ExcelData/DBValidationData.xlsx";
	private static final String SHEET_NAME = "UsersPost";


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

			String userName = reader.getCellData(SHEET_NAME, 1, rowNum);
			innerDataMap.put("name", userName);

			String phone_number = reader.getCellData(SHEET_NAME, 2, rowNum);
			long phoneNumber = 0L;
			if(!phone_number.equals(""))
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

			String statusCode = reader.getCellData(SHEET_NAME, 10, rowNum);
			int statusCodeNumber = Math.round(Float.parseFloat(statusCode));
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
		response = request.post("/Users");	
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

	@Given("New user is added with Post request")
	public void new_user_is_added_with_post_request() {

		System.out.println("UserPost :: inside Given");
		data = populateDataFromExcel();
		HashMap<String, Object> validDataMap = data.get("validUserData");
		postRequest(validDataMap);	

		try {
			Assert.assertEquals(validDataMap.get("statusCode"), response.statusCode());
			System.out.println("The actual response status code is : " + response.getStatusCode());

			JsonPath jsonPathEvaluator = response.jsonPath();
			RequestString_userid = jsonPathEvaluator.get("user_id");
			RequestString_message= jsonPathEvaluator.get("message_response");
			RequestString_name= jsonPathEvaluator.get("name");
			name = RequestString_name.split(",");
			System.out.println("Json element value in response = " + RequestString_userid);
			System.out.println("Json element value in response = " + RequestString_message);
			System.out.println("Json element value in response = " + RequestString_name);
		}catch(AssertionError ex) {
			ex.printStackTrace();
			System.out.println("Assertion Failed !!");
		}
		
	}

	@When("New user is queried from DB")
	public void new_user_is_queried_from_db() {
		if(response.statusCode() == 201) {
		try {
			sqlString_creationTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='"+name[0]+"'", "creation_time");
			sqlString_modTime = db.connect("SELECT * FROM tbl_lms_user where user_first_name='"+name[0]+"'", "last_mod_time");
			sqlString_userid = db.connect("SELECT * FROM tbl_lms_user where user_first_name='"+name[0]+"'", "user_id");

			System.out.println("received DB value sqlString_creationTime = " + sqlString_creationTime);
			System.out.println("received DB value sqlString_modTime = " + sqlString_modTime);
			System.out.println("received DB value sqlString_userid = " + sqlString_userid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else {
			System.out.println("User not posted");
		}
	}

	@Then("Compare and Assert request sent and DB user details")
	public void compare_and_assert_request_sent_and_db_user_details() {
		
		Assert.assertEquals(sqlString_userid.trim(), RequestString_userid.trim());
	
	}
}



