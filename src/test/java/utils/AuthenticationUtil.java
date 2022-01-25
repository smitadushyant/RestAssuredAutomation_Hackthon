package utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class AuthenticationUtil {
	
	public RequestSpecification requestSpecification(RequestSpecification request) {
		RestAssured.port = 8080;
		request = RestAssured.given();
		request.auth().preemptive().basic("APIPROCESSING", "2xx@Success");	
		
		return request;
	}
}
