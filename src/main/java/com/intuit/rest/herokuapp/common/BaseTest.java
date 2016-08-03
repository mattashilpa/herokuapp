package com.intuit.rest.herokuapp.common;

import static com.intuit.rest.herokuapp.common.Constants.BASE_URL;
import static io.restassured.RestAssured.given;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import com.intuit.rest.herokuapp.common.HerokuappUtil;

public class BaseTest {

	Response response;
	Map<String,String> headers = new HashMap<String,String>();
	public static Logger logger = Logger.getLogger(BaseTest.class);
	
	@BeforeClass
	public void setUpRA(){
		DOMConfigurator.configure("log4j.xml");
		String className = this.getClass().getName();
		logger.info("Running Test Class **** " + className + " ****");
//		logger.setLevel(Level.INFO);
        headers.put("Authentication","facetking");
        headers.put("Content-type","application/json");
        RestAssured.requestSpecification = new RequestSpecBuilder() 
                .addHeaders(headers)
                .build();
        
	}
	
	@AfterClass
	public void tearDown(){
		String className = this.getClass().getName();
		RestAssured.reset();
		logger.info("Execution Complete Test Class **** " + className + " ****");
			
	}
	
	@BeforeMethod
	protected void startTest(Method method) throws Exception {
	    String testName = method.getName(); 
	    logger.info("Executing test: " + testName + " ...");
	}
	
	@AfterMethod
	public void endTest(Method method) throws Exception {
		String testName = method.getName(); 
	    logger.info("Execution Completing test: " + testName + " ...");
		response = null;
	}
	
	public Response processPutRequest(String contactId, String fileName){
		String inputJson = HerokuappUtil.getJsonInputFile(fileName);
		response = given()
						.body(inputJson)
						.put(BASE_URL + contactId)
						.then()
						.extract()
						.response();
		return response;
	}
	
	public Response processPostRequest(String fileName){
		String inputJson = HerokuappUtil.getJsonInputFile(fileName);
		response = given()
						.body(inputJson)
						.post(BASE_URL)
						.then()
						.extract()
						.response();
		return response;
	}
	
	public Response processGetRequest(String contactId){
		response = given()
						.get(BASE_URL + contactId)
						.then()
						.extract()
						.response();
		return response;
	}
	
	public Response processDeleteRequest(String contactId){
		response = given()
						.delete(BASE_URL + contactId)
						.then()
						.extract()
						.response();
		return response;
	}
}
