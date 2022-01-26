package stepDefinition.skill;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import utils.ConfigReader;

public class Hooks {
			
TestContextAPI testContext;

	
	public Hooks(TestContextAPI testContext) {
		this.testContext = testContext;
		
	}
	
	@Before
	public void SetUp() {
		/*System.out.println("Username is :" + ConfigReader.getInstance().getUserName());
		System.out.println("Password is :" + ConfigReader.getInstance().getpassword());
		System.out.println("APP URL is :" + ConfigReader.getInstance().getBaseUrl());*/
		
		RestAssured.authentication = RestAssured.preemptive().basic(ConfigReader.getInstance().getUserName(), ConfigReader.getInstance().getpassword());
		RestAssured.baseURI = ConfigReader.getInstance().getBaseUrl();
		testContext.httpRequest = RestAssured.given();
	}
	
	@After
	public void CleanUp() {
		testContext.httpRequest = null;
		testContext.response=null;
		//testContext.skill_id = 0;
	}
}
