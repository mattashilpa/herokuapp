package com.intuit.rest.herokuapp;

import static com.intuit.rest.herokuapp.common.Constants.EMAIL;
import static com.intuit.rest.herokuapp.common.Constants.FIRST_NAME;
import static com.intuit.rest.herokuapp.common.Constants.LAST_NAME;
import static com.intuit.rest.herokuapp.common.Constants.PHONE_NUMBER;
import static com.intuit.rest.herokuapp.common.Constants._ID;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
//import static com.jayway.restassured.RestAssured.*;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.RestAssured.get;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.intuit.rest.herokuapp.common.BaseTest;

import io.restassured.response.Response;

public class GetTest extends BaseTest{
	String baseURL = "https://shielded-river-86069.herokuapp.com";
	String path = "/contacts";
	public static String jsonAsString;
	public static Response response;
	
	@Test
	public void testGetGeneral(){
		response = processGetRequest("");
		SoftAssert softly = new SoftAssert();
		//Verify Response
		softly.assertEquals(response.statusCode(), 200, "testGetGeneral : Expected Status Code = 200 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.contentType(),"application/json; charset=utf-8", "testGetGeneral : Unexpected Content Type in response.");
		ArrayList<Map<String,?>> contactsList = from(response.asString()).get("");
		softly.assertTrue(contactsList.size() > 0 , "testGetGeneral : Contact list is empty !");
		softly.assertAll();
	}
	
	@Test
	public void testValidateGetResponse(){
		response = processGetRequest("");
		//Verify Response
//		ArrayList<Map<String,?>> contactsList = from(response.asString()).get("");
//		assertTrue(contactsList.size() > 0);
//		for(){
//			
//		}
		
	    // put all contactIds into a list
	    List<String> ids = response.jsonPath().getList("_id");
	    for (String id : ids)
	    {
	        assertNotNull(id, "ERROR : Id cannot be null !");
	    }
	}
	
	@Test
	public void testGetById(){
		//Post a new contact resource
		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
		Response responsePost = processPostRequest(postReqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(responsePost.statusCode(), 201,"testGetById : Error in POST operation.");
		
		//Fetch contactId from response
		String contactId = from(responsePost.asString()).get(_ID);
		//Get resource by contactId
		response = processGetRequest("/" +contactId);
		softly.assertEquals(response.statusCode(), 200, "testGetById : Expected Status Code = 200 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.contentType(),"application/json; charset=utf-8","testGetById : Unexpected Content Type in response.");
		String responseBody = response.asString();
		softly.assertEquals(from(responseBody).get(FIRST_NAME), "testFirstName" , "testGetById : Incorrect first name in Get response");
		softly.assertEquals(from(responseBody).get(LAST_NAME), "testLastName", "testGetById : Incorrect last name in Get response");
		softly.assertEquals(from(responseBody).get(EMAIL), "test@intuit.com", "testGetById :Incorrect email in Get response");
		softly.assertEquals(from(responseBody).get(PHONE_NUMBER), "1234567890", "testGetById :Incorrect phone number in Get response");
		softly.assertEquals(from(responseBody).get(_ID),contactId,"testGetById :Incorrect contact ID in Get response."); 
		softly.assertAll();
	}
	
	@Test
	public void testGetByInvalidId(){
		response = processGetRequest("/nonexistingID123");
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 500, "testGetByInvalidId : Expected Status Code = 500 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Contact Not Found","testGetByInvalidId : Unexpected response.");
		softly.assertAll();
	}
	
	@Test
	public void testGetByInvalidId2(){ 
		//non existing id but in proper format
		response = processGetRequest("/111b111e11e1111111e1111a");
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 200, "testGetByInvalidId2 : Expected Status Code = 200 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString(),"Contact does not exist.","testGetByInvalidId2 : Unexpected response.");
		softly.assertAll();
	}
	
} 