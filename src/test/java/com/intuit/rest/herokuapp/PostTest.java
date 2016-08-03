package com.intuit.rest.herokuapp;

import static com.intuit.rest.herokuapp.common.Constants.CREATE_DATE;
import static com.intuit.rest.herokuapp.common.Constants.EMAIL;
import static com.intuit.rest.herokuapp.common.Constants.FIRST_NAME;
import static com.intuit.rest.herokuapp.common.Constants.LAST_NAME;
import static com.intuit.rest.herokuapp.common.Constants.PHONE_NUMBER;
import static com.intuit.rest.herokuapp.common.Constants._ID;
//import com.jayway.restassured.response.ResponseBody;
//import static io.restassured.path.xml.XmlPath.from;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertEquals;

import java.util.List;

//import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
//import static com.jayway.restassured.RestAssured.*;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.RestAssured.get;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.intuit.rest.herokuapp.common.BaseTest;
import com.intuit.rest.herokuapp.common.HerokuappUtil;

import io.restassured.response.Response;

public class PostTest extends BaseTest{
	String baseURL = "https://shielded-river-86069.herokuapp.com";
	String path = "/contacts";
	public static String contact_id;
	public static String jsonAsString;
	public static Response response;
	@Test
	public void testPostContact(){
		SoftAssert softly = new SoftAssert();
		// post a new contact resource
		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
		response = processPostRequest(postReqJsonFile);
		// verify that POST call was succesfull
		softly.assertEquals(response.statusCode(), 201,"Error in POST operation");
		softly.assertEquals(response.contentType() , "application/json; charset=utf-8", "Wrong Content Type");
		// validate response body
		String responseBody = response.asString();
		softly.assertEquals(from(responseBody).get(FIRST_NAME), "testFirstName" , "testPostContact : Incorrect first name in Post response");
		softly.assertEquals(from(responseBody).get(LAST_NAME), "testLastName", "testPostContact : Incorrect last name in Post response");
		softly.assertEquals(from(responseBody).get(EMAIL), "test@intuit.com", "testPostContact :Incorrect email in Post response");
		softly.assertEquals(from(responseBody).get(PHONE_NUMBER), "1234567890", "testPostContact :Incorrect phone number in Post response");
		String contactId = from(responseBody).get(_ID); //Fetch contactId from response
		softly.assertTrue(HerokuappUtil.isNotEmpty(contactId), "testPostContact : Contact Id in Post response is null");
//		assertTrue(from(responseBody).get(CREATE_DATE).toString().contains(""));
		//TO-DO
		System.out.println("Create Date - " + from(responseBody).get(CREATE_DATE));
	
		// verify ContactId is added via get() call
		response = processGetRequest("");
		List<String> ids = response.jsonPath().getList("_id");
		softly.assertTrue(ids.contains(contactId) , "testPostContact : ContactId does not exist!");
		softly.assertAll();
	}
	
	@Test
	public void testPostBadRequest(){
		String reqJsonFile = "requestPayload/TestBadRequest.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostBadRequest : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Bad Request", "testPostBadRequest : Unexpected response string");
		softly.assertAll();
	}

	@Test
	public void testPostBlankRequest(){
		String reqJsonFile = "requestPayload/TestBlankRequest.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostBlankRequest : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPostBlankRequest : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPostAdditionalRequestFields(){
		String reqJsonFile = "requestPayload/TestAdditionalRequestFields.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostAdditionalRequestFields : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Please provide a valid input request.", "testPostAdditionalRequestFields : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostFirstNameMissing(){
		String reqJsonFile = "requestPayload/TestFirstNameMissing.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostFirstNameMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.", "testPostFirstNameMissing : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostFirstNameNull(){
		String reqJsonFile = "requestPayload/TestFirstNameNull.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostFirstNameNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.", "testPostFirstNameNull : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostFirstNameBlank(){
		String reqJsonFile = "requestPayload/TestFirstNameBlank.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostFirstNameBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.", "testPostFirstNameBlank : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostFirstNameSpace(){
		String reqJsonFile = "requestPayload/TestFirstNameSpace.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostFirstNameSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.", "testPostFirstNameSpace : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostFirstNameNumeric(){
		String reqJsonFile = "requestPayload/TestFirstNameNumeric.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostFirstNameNumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid first name.", "testPostFirstNameNumeric : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostLastNameMissing(){
		String reqJsonFile = "requestPayload/TestLastNameMissing.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostLastNameMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.", "testPostLastNameMissing : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostLastNameNull(){
		String reqJsonFile = "requestPayload/TestLastNameNull.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostLastNameNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.", "testPostLastNameNull : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostLastNameBlank(){
		String reqJsonFile = "requestPayload/TestLastNameBlank.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostLastNameBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.", "testPostLastNameBlank : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostLastNameSpace(){
		String reqJsonFile = "requestPayload/TestLastNameSpace.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostLastNameSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.", "testPostLastNameSpace : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostLastNameNumeric(){
		String reqJsonFile = "requestPayload/TestLastNameNumeric.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostLastNameNumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid last name.", "testPostLastNameNumeric : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostEmailMissing(){
		String reqJsonFile = "requestPayload/TestEmailMissing.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostEmailMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.", "testPostEmailMissing : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostEmailNull(){
		String reqJsonFile = "requestPayload/TestEmailNull.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostEmailNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.", "testPostEmailNull : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostEmailBlank(){
		String reqJsonFile = "requestPayload/TestEmailBlank.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostEmailBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.", "testPostEmailBlank : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostEmailSpace(){
		String reqJsonFile = "requestPayload/TestEmailSpace.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostEmailSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid email.", "testPostEmailSpace : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostInvalidEmail1(){
		String reqJsonFile = "requestPayload/TestInvalidEmail1.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostInvalidEmail1 : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid email.", "testPostInvalidEmail1 : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostInvalidEmail2(){
		String reqJsonFile = "requestPayload/TestInvalidEmail2.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostInvalidEmail2 : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid email.", "testPostInvalidEmail2 : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneMissing(){
		String reqJsonFile = "requestPayload/TestPhoneMissing.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneMissing : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneNull(){
		String reqJsonFile = "requestPayload/TestPhoneNull.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneNull : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneBlank(){
		String reqJsonFile = "requestPayload/TestPhoneBlank.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneBlank : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneSpace(){
		String reqJsonFile = "requestPayload/TestPhoneSpace.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneSpace : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneFormat1(){
		String reqJsonFile = "requestPayload/TestPhoneFormat1.json";
		Response response = processPostRequest(reqJsonFile);
		assertEquals(response.statusCode(), 201 , "testPostPhoneFormat1 : Expected Status Code = 201 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPostPhoneFormat2(){
		String reqJsonFile = "requestPayload/TestPhoneFormat2.json";
		Response response = processPostRequest(reqJsonFile);
		assertEquals(response.statusCode(), 201 , "testPostPhoneFormat2 : Expected Status Code = 201 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPostPhoneFormat3(){
		String reqJsonFile = "requestPayload/TestPhoneFormat3.json";
		Response response = processPostRequest(reqJsonFile);
		assertEquals(response.statusCode(), 201 , "testPostPhoneFormat3 : Expected Status Code = 201 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPostPhoneLengthOverTen(){
		String reqJsonFile = "requestPayload/TestPhoneLengthOverTen.json";
		Response response = processPostRequest(reqJsonFile);
		assertEquals(response.statusCode(), 400 , "testPostPhoneLengthOverTen : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneLengthOverTen : Unexpected response string.");
	}
	
	@Test
	public void testPostPhoneLengthUnderTen(){
		String reqJsonFile = "requestPayload/TestPhoneLengthUnderTen.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneLengthUnderTen : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneLengthUnderTen : Unexpected response string");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneAlphanumeric(){
		String reqJsonFile = "requestPayload/TestPhoneAlphanumeric.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode() ,400, "testPostPhoneAlphanumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.", "testPostPhoneAlphanumeric : Unexpected response string");
		softly.assertAll();
	}
	
//	@Test(priority=1)
//	public void testResponseType(){
//		String url = baseURL + path;
//		String responseType = given()
//			.header("Authentication","facetking")
//			.header("Content-type","application/json")
//			.post(url)
//			.header("Content-Type");
//		
//		assertEquals(responseType,"application/json; charset=utf-8");
//	}

} 