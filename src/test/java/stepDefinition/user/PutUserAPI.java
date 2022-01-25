/*

package stepDefinition.user;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.UserDto;
import resources.Utils;

public class PutUserAPI extends Utils{

	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;


	private  static  final  String  BASE_URL  =  "http://localhost:8080";
	private static ObjectMapper mapper = new ObjectMapper();
	private final static String FILE_PATH = "src\\test\\java\\resources\\testinput.xlsx";
	

@Given("User is on the PUT Request")
public void user_is_on_the_put_request() throws IOException {

	RestAssured.baseURI=BASE_URL;
	res=RestAssured.given().spec(requestSpecification());
}

@When("User updates the existing userID with all inputs from excel")
public void user_updates_the_existing_user_id_with_all_inputs_from_excel() throws JsonProcessingException {
	UserDto requestUserDto=populate_userDtoFromExcel(1,"Put");
	sendRequest(requestUserDto);
}

@Then("User receives an {int} status code and updates the existing record")
public void user_receives_an_status_code_and_updates_the_existing_record(Integer int1) {
	assertEquals(response.getStatusCode(),int1.intValue());
}

@Then("{string} in response_body is {string}")
public void in_response_body_is(String keydata, String Expected) {
	String resps=  response.asString();
	JsonPath js=new JsonPath(resps);
	assertEquals(js.get(keydata).toString(),Expected);
	}

@When("User sends input with alphanumeric username")
public void user_sends_input_with_alphanumeric_username() throws JsonProcessingException {
	UserDto requestUserDto=populate_userDtoFromExcel(2,"Put");
	sendRequest(requestUserDto);
}

@Then("User receives {int} bad request status response")
public void user_receives_bad_request_status_response(Integer int1) {
	assertEquals(response.getStatusCode(),int1.intValue());
}

@When("User sends input with invalid phone number")
public void user_sends_input_with_invalid_phone_number() throws JsonProcessingException {
	UserDto requestUserDto=populate_userDtoFromExcel(3,"Put");
	sendRequest(requestUserDto);
}

@Then("receives {int} bad request status response")
public void receives_bad_request_status_response(Integer int1) {
	assertEquals(response.getStatusCode(),int1.intValue());
}

@When("User sends input with invalid visa status")
public void user_sends_input_with_invalid_visa_status() throws JsonProcessingException {
	UserDto requestUserDto=populate_userDtoFromExcel(4,"Put");
	sendRequest(requestUserDto);
}

@When("User sends input with alphanumeric location and comments inputs")
public void user_sends_input_with_alphanumeric_location_and_comments_inputs() throws JsonProcessingException {
	UserDto requestUserDto=populate_userDtoFromExcel(5,"Put");
	sendRequest(requestUserDto);
	}




	private UserDto populate_userDtoFromExcel(int row,String sheetname)  {

		resources.UserDto requestUserDto = new resources.UserDto();

		resources.ExcelUtils utils = new resources.ExcelUtils(FILE_PATH, sheetname);
		requestUserDto.setName(utils.getCellDataString(row, 0));

		String stringToConvert = String.valueOf(utils.getCellDataNumeric(row, 1));
		Long convertedLong = Long.parseLong(stringToConvert);

		requestUserDto.setPhone_number(convertedLong);

		requestUserDto.setLocation(utils.getCellDataString(row,2));

		requestUserDto.setTime_zone(utils.getCellDataString(row, 3));

		requestUserDto.setLinkedin_url(utils.getCellDataString(row, 4));

		requestUserDto.setEducation_ug(utils.getCellDataString(row, 5));

		requestUserDto.setEducation_pg(utils.getCellDataString(row,6));
		requestUserDto.setVisa_status(utils.getCellDataString(row,7));
		requestUserDto.setComments(utils.getCellDataString(row, 8));

		requestUserDto.setUser_id(utils.getCellDataString(row, 9));

		return requestUserDto;


	}
	private void sendRequest(UserDto reqDto) throws JsonProcessingException {

		String requestJSON = mapper.writeValueAsString(reqDto);	
		response  =  res.body(requestJSON).put("http://localhost:8080/Users/"+reqDto.getUser_id());	
		System.out.println(">>>> "+response.asString());

	}
}

*/










