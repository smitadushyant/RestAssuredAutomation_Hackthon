
package stepDefinition.userSkillMap;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AuthenticationUtil;
import utils.ExcelReader;

public class UserskillmapGet {
	
	private Response response;
	private RequestSpecification request;
	private AuthenticationUtil util = new AuthenticationUtil();
	
	private static final String FILE_NAME = "./src/test/resources/ExcelData/UserSkillMapDataExcel.xlsx";
	private static final String SHEET_NAME = "delete";
	
	private HashMap<String, HashMap<String, Object>> data = new HashMap<String, HashMap<String, Object>>();
	
	private HashMap<String, HashMap<String, Object>> populateDataFromExcel() {
		ExcelReader reader = new ExcelReader(FILE_NAME);

		HashMap<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();

		int rows = reader.getRowCount(SHEET_NAME);
		//implement code
		
		return dataMap;
	}

	
	public static boolean isJSONValid(String serverresponse) {
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
	
}
