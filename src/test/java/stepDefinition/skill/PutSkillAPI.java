package stepDefinition.skill;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;

import java.util.HashMap;


import org.json.simple.JSONObject;
import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;


public class PutSkillAPI {
	
	HashMap<String,Object> header,reqParams;
	int statusCode;
	String resBody;
	JsonPath jsonPathEvaluator;
	TestContextAPI testContext;
	JSONObject reqParamsJSON;
	
	public PutSkillAPI(TestContextAPI testContext) {
		this.testContext = testContext;
		
	}
	
	@Given("I have the new skill with the body and endpoint as {string}")
	public void i_have_the_new_skill_with_the_body_and_endpoint_as(String path, io.cucumber.datatable.DataTable dataTable) {
		header = new HashMap<String,Object>();
		header.put("Content-Type","application/json");
		reqParams = new HashMap<String , Object>();
		reqParams.put("skill_name",dataTable.row(1).get(0) );
		
		
		reqParamsJSON = new JSONObject(reqParams);
		testContext.response = testContext.httpRequest.headers(header).body(reqParamsJSON.toJSONString()).post(path);
		String resBody = testContext.response.getBody().asPrettyString();
		System.out.println(resBody);
		jsonPathEvaluator = testContext.response.jsonPath();
		testContext.skill_id = jsonPathEvaluator.get("skill_id");
		System.out.println("The created skill id is :" +testContext.skill_id );
	}

	@When("I hit the PUT Api request to update the skill details with endpoint {string}")
	public void i_hit_the_put_api_request_to_update_the_skill_details_with_endpoint(String path, io.cucumber.datatable.DataTable dataTable) {
	   
		String endPoint = path+"/"+testContext.skill_id;
		header = new HashMap<String,Object>();
		header.put("Content-Type","application/json");
		reqParams = new HashMap<String , Object>();
		reqParams.put("skill_name",dataTable.row(1).get(0) );
		reqParamsJSON = new JSONObject(reqParams);
		testContext.response = testContext.httpRequest.headers(header).body(reqParamsJSON.toJSONString()).put(endPoint);
		String resBody = testContext.response.getBody().asPrettyString();
		System.out.println(resBody);
		
	}

	@Then("Perform the get request to the skill with endpoint {string}")
	public void perform_the_get_request_to_the_skill_with_endpoint(String path) {
		String endPoint = path+"/"+testContext.skill_id;
		
		testContext.response = testContext.httpRequest.get(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());
			
	   
	}

	@Then("I Should see skill name as {string}")
	public void i_should_see_skill_name_as(String expectedSkillName) {
	jsonPathEvaluator = testContext.response.jsonPath();
	String updatedSkillFromRes = jsonPathEvaluator.get("skill_name");
	Assert.assertEquals(updatedSkillFromRes, expectedSkillName);
}

// steps for nagative tests	
	
	@Given("I set the header with the body")
	public void i_set_the_header_with_the_body(io.cucumber.datatable.DataTable dataTable) {
		header = new HashMap<String,Object>();
		header.put("Content-Type","application/json");
		reqParams = new HashMap<String , Object>();
		reqParams.put("skill_name",dataTable.row(1).get(0) );
		reqParamsJSON = new JSONObject(reqParams);
	}
	


	
	
	@When("I hit the PUT Api request to update the skill details with endpoint {string} and <skill_id>")
	public void i_hit_the_put_api_request_to_update_the_skill_details_with_endpoint_and_skill_id(String path, io.cucumber.datatable.DataTable dataTable) {
	   
		String Skill_id = dataTable.row(1).get(0);
		String endPoint = path+"/"+Skill_id;
		testContext.response = testContext.httpRequest.headers(header).body(reqParamsJSON.toJSONString()).put(endPoint);
		String resBody = testContext.response.getBody().asPrettyString();
		System.out.println(resBody);
		
	}

	
	
	
}
