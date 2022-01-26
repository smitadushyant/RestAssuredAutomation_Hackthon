package stepDefinition.skill;

import org.junit.Assert;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import stepDefinition.skill.TestContextAPI;
import utils.ExcelReader;

public class GetSkillAPI {
	
	int statusCode;
	String resBody;
	JsonPath jsonPathEvaluator;
	TestContextAPI testContext;
	private static final String FILE_NAME = "./src/test/resources/ExcelData/SkillAPIData.xlsx";
	private static final String SHEET_NAME = "get";
	
	public GetSkillAPI(TestContextAPI testContext) {
		this.testContext = testContext;
		
	}

	@When("I perform GET operation for {string}")
	public void i_perform_get_operation_for(String path) {
	    
		testContext.response = testContext.httpRequest.get(path);
	}

	@Then("API should return all the skills")
	public void api_should_return_all_the_skills() {
		 resBody = testContext.response.getBody().asPrettyString();
			System.out.println(resBody);
	}
	
	@When("I perform GET operation for {string} with <skill_id>")
	public void i_perform_get_operation_for_with_skill_id(String path, io.cucumber.datatable.DataTable dataTable)
	{
		//String Skill_id = dataTable.row(1).get(0);
		testContext.skill_id=Math.round(Integer.parseInt(dataTable.row(1).get(0)));
		String endPoint = path+"/"+testContext.skill_id;
		testContext.response = testContext.httpRequest.get(endPoint);
		String resBody = testContext.response.getBody().asPrettyString();
		System.out.println(resBody);
	}

	
	@Then("API should return the details of skill id {int}")
	public void api_should_return_the_details_of_skill_id(Integer int1) {
		resBody = testContext.response.getBody().asPrettyString();
		 System.out.println("The skill details are : " + resBody );
		 jsonPathEvaluator = testContext.response.jsonPath();
		 int skillIDfromResponse =  jsonPathEvaluator.get("skill_id");
		 Assert.assertEquals(skillIDfromResponse,int1.intValue() );
	}
	
	
	
	//Steps for negative scenarios
	
	@When("I perform GET operation for {string} with {string}")
	public void i_perform_get_operation_for_with(String path, String skillID) {
	//System.out.println(path);
	//System.out.println(skillID);
	testContext.response = testContext.httpRequest.get(path+"/"+ skillID);
	}
	
	
	
	@Then("API should return message in responsebody is {string}")
	public void api_should_return_message_in_responsebody_is(String Expected) {
	
		
		resBody = testContext.response.getBody().asPrettyString();
		 System.out.println("The skill details are : " + resBody );
		 jsonPathEvaluator = testContext.response.jsonPath();
		 String msgFromResponse =  jsonPathEvaluator.get("message").toString();
		 System.out.println(msgFromResponse);
		 
		 Assert.assertEquals(msgFromResponse,Expected);
	    
	}


	@When("I perform GET operation for {string} with {double}")
	public void i_perform_get_operation_for_with(String path, Double double1) {
		testContext.response = testContext.httpRequest.get(path+"/"+ double1);
	}

	
	
	
	
	
	
	   
	@When("I perform GET operation for {string} with aplphnumeric id")
	public void i_perform_get_operation_for_with_aplphnumeric_id(String string) {
		//String sheetName = "get";
		ExcelReader reader = new ExcelReader(FILE_NAME);
		String data = reader.getCellData(SHEET_NAME, 1, 4);
		System.out.println(data);
		System.out.println(string+"/"+data);
		testContext.response = testContext.httpRequest.get(string+"/"+ data);
	
	}	
	
}
