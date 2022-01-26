


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

public class DeleteSkillAPI {
	HashMap<String, Object> header, reqParams;
	int statusCode;
	String resBody;
	JsonPath jsonPathEvaluator;
	TestContextAPI testContext;
	JSONObject reqParamsJSON;

	public DeleteSkillAPI(TestContextAPI testContext) {
		this.testContext = testContext;

	}

	@Given("I have the skill with the body and endpoint as {string}")
	public void i_have_the_skill_with_the_body_and_endpoint_as(String path, io.cucumber.datatable.DataTable dataTable) {
		header = new HashMap<String, Object>();
		header.put("Content-Type", "application/json");
		reqParams = new HashMap<String, Object>();
		reqParams.put("skill_name", dataTable.row(1).get(0));

		reqParamsJSON = new JSONObject(reqParams);
		testContext.response = testContext.httpRequest.headers(header).body(reqParamsJSON.toJSONString()).post(path);
		String resBody = testContext.response.getBody().asPrettyString();
		System.out.println(resBody);
		jsonPathEvaluator = testContext.response.jsonPath();
		testContext.skill_id = jsonPathEvaluator.get("skill_id");
		System.out.println("The created skill id is :" + testContext.skill_id);
	}

	@When("I hit the delete Api request to delete the skill details with endpoint {string}")
	public void i_hit_the_delete_api_request_to_delete_the_skill_details_with_endpoint(String path) {
		String endPoint = path + "/" + testContext.skill_id;

		testContext.response = testContext.httpRequest.delete(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());

	}

	@Then("Perform the get request to the skill with the endpoint {string}")
	public void perform_the_get_request_to_the_skill_with_the_endpoint(String path) {
		String endPoint = path + "/" + testContext.skill_id;

		testContext.response = testContext.httpRequest.get(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());
	}

	

	
	//Negative scenario

	@When("I hit the delete Api request to update the skill details with endpoint  {string} and non existing <skill_id>")
	public void i_hit_the_delete_api_request_to_update_the_skill_details_with_endpoint_and_non_existing_skill_id(
			String path, io.cucumber.datatable.DataTable dataTable) {
		testContext.skill_id = Math.round(Integer.parseInt(dataTable.row(1).get(0)));
		String endPoint = path + "/" + testContext.skill_id;

		testContext.response = testContext.httpRequest.delete(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());
	}

	@When("I hit the delete Api request to update the skill details with endpoint {string} and alphanumeric id<skill_id>")
	public void i_hit_the_delete_api_request_to_update_the_skill_details_with_endpoint_and_alphanumeric_id_skill_id(
			String path, io.cucumber.datatable.DataTable dataTable) {
		
		String endPoint = path + "/" + dataTable.row(1).get(0);
		testContext.response = testContext.httpRequest.delete(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());
	}

	

	@When("I hit the delete Api request to update the skill details with endpoint  {string} and blank <skill_id>")
	public void i_hit_the_delete_api_request_to_update_the_skill_details_with_endpoint_and_blank_skill_id(String path,
			io.cucumber.datatable.DataTable dataTable) {
		
		String endPoint = path + "/" + dataTable.row(1).get(0);
		testContext.response = testContext.httpRequest.delete(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());

	}
	@When("I hit the delete Api request to update the skill details with endpoint  {string} and specialcharacter <skill_id>")
	public void i_hit_the_delete_api_request_to_update_the_skill_details_with_endpoint_and_specialcharacter_skill_id(String path, io.cucumber.datatable.DataTable dataTable) {
	    
		
		String endPoint = path + "/" + dataTable.row(1).get(0);
		testContext.response = testContext.httpRequest.delete(endPoint);
		System.out.println("The skill Details are:" + testContext.response.asPrettyString());

	}



}