package com.intuit.rest.herokuapp;

import static com.intuit.rest.herokuapp.common.Constants.BASE_URL;
import static io.restassured.RestAssured.given;
//import com.jayway.restassured.response.ResponseBody;
//import static io.restassured.path.xml.XmlPath.from;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

//import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
//import static com.jayway.restassured.RestAssured.*;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.RestAssured.get;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.intuit.rest.herokuapp.common.Constants;
import com.intuit.rest.herokuapp.common.HerokuappUtil;

import io.restassured.response.Response;

public class GeneralTest{
	public static String contact_id;
	public static String jsonAsString;
	public static Response response;
	
	
//	@BeforeClass
//	public void getContactIdFromPost(){
//		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
//		response = processPostRequest(postReqJsonFile);
//		if(response.statusCode() != 201)
//			Assert.fail("Error in POST operation." + response.asString());
//		String responseAsString = response.asString();
//		contact_id = "/" + from(responseAsString).get("_id");
//		System.out.println("contact_id - " + contact_id);
//	}
	
	@Test(priority=0)
	public void testMissingAuthenticationInPut(){
		String inputJson = HerokuappUtil.getJsonInputFile("requestPayload/TestPutRequest.json");
		Response response = given()
			.header("Content-type","application/json")
			.body(inputJson)
			.put(BASE_URL + "/111b111e11e1111111e1111a")
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testMissingAuthenticationInPut : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testMissingAuthenticationInPut : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test(priority=0)
	public void testInvalidAuthenticationInPut(){
		String inputJson = HerokuappUtil.getJsonInputFile("requestPayload/TestPutRequest.json");
		Response response = given()
			.header("Authentication","invalidAuthentication")
			.header("Content-type","application/json")
			.body(inputJson)
			.put(BASE_URL + "/111b111e11e1111111e1111a")
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testInvalidAuthenticationInPut : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testInvalidAuthenticationInPut : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test
	public void testMissingAuthenticationInPost(){
		String inputJson = HerokuappUtil.getJsonInputFile("requestPayload/TestValidPostRequest.json");
		Response response = given()
			.header("Content-type","application/json")
			.body(inputJson)
			.post(BASE_URL)
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testMissingAuthenticationInPost : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testMissingAuthenticationInPost : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test
	public void testInvalidAuthenticationInPost(){
		String inputJson = HerokuappUtil.getJsonInputFile("requestPayload/TestValidPostRequest.json");
		Response response = given()
			.header("Authentication","invalid")
			.header("Content-type","application/json")
			.body(inputJson)
			.post(BASE_URL)
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testInvalidAuthenticationInPost : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testInvalidAuthenticationInPost : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test
	public void testPostInvalidURL(){
		String inputJson = HerokuappUtil.getJsonInputFile("requestPayload/TestValidPostRequest.json");
		Response response = given()
			.header(Constants.AUTHENTICATION_KEY,Constants.AUTHENTICATION_VALUE)
			.header(Constants.CONTENT_TYPE,Constants.CONTENT_TYPE_VALUE)
			.body(inputJson)
			.post(BASE_URL + "/invalidURL")
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,404, "testPostInvalidURL : Expected Status Code = 404 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Cannot POST /contacts/invalidURL", "testPostInvalidURL : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testMissingAuthenticationInGet(){
		Response response = given()
			.header("Content-type","application/json")
			.get(BASE_URL)
			.then()
			.extract()
			.response();
		assertTrue(response.statusCode() == 401);
		String errorText = from(response.asString()).get("error");
		assertEquals(errorText , "Must provide a valid Authentication header");
	}
	
	@Test(priority=0)
	public void testInvalidAuthenticationInGet(){
		Response response = given()
			.header("Authentication","")
			.header("Content-type","application/json")
			.get(BASE_URL)
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testInvalidAuthenticationInGet : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testInvalidAuthenticationInGet : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test(priority=0)
	public void testMissingAuthenticationInDelete(){
		Response response = given()
			.header("Content-type","application/json")
			.delete(BASE_URL + "/111b111e11e1111111e1111a")
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testMissingAuthenticationInDelete : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testMissingAuthenticationInDelete : Unexpected error in response.");
		softly.assertAll();
	}
	
	@Test(priority=0)
	public void testInvalidAuthenticationInDelete(){
		Response response = given()
			.header("Authentication","")
			.header("Content-type","application/json")
			.delete(BASE_URL + "/111b111e11e1111111e1111a")
			.then()
			.extract()
			.response();
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 401, "testInvalidAuthenticationInDelete : Expected Status Code = 401 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error") , "Must provide a valid Authentication header","testInvalidAuthenticationInDelete : Unexpected error in response.");
		softly.assertAll();
	}
} 