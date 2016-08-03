package com.intuit.rest.herokuapp;

//import com.jayway.restassured.response.ResponseBody;
//import static io.restassured.path.xml.XmlPath.from;
import static io.restassured.path.json.JsonPath.from;
import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;

//import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
//import static com.jayway.restassured.RestAssured.*;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.RestAssured.get;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.intuit.rest.herokuapp.common.BaseTest;
import com.intuit.rest.herokuapp.common.HerokuappUtil;

import io.restassured.response.Response;

public class PutTest extends BaseTest{
	public static String contact_id;
	public static String jsonAsString;
	public static Response response;
	
	
	@BeforeClass
	public void getContactIdFromPost(){
		// post a new contact resource before the beginning of tests
		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
		response = processPostRequest(postReqJsonFile);
		if(response.statusCode() != 201)
			Assert.fail("Error in POST operation." + response.asString());
		contact_id = "/" + from(response.asString()).get("_id"); // get contact_id from POST response
		logger.info("Get Contact Id from POST operation - " + contact_id);
	}
	
	@Test
	public void testSuccessfulPutRequest(){
		String putReqJsonFile = "requestPayload/TestPutRequest.json";
		if(HerokuappUtil.isNotEmpty(contact_id)){
			response = processPutRequest(contact_id,putReqJsonFile);
			SoftAssert softly = new SoftAssert();
			softly.assertEquals(response.statusCode(), 204 , "testSuccessfulPutRequest : Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
			softly.assertTrue(response.asString().isEmpty(),"testSuccessfulPutRequest : Unexpected response body.");
			softly.assertAll();
		}else{
			logger.info("Contact Id not generated in POST operation.");
			Assert.fail("_id not generated in POST operation.");
		}
	}
	
	
	
	@Test
	public void testPutContactIdMissingInURL(){
		String reqJsonFile = "requestPayload/TestPutRequest.json";
		Response response = processPutRequest("",reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 404 , "testPutContactIdMissingInURL : Expected Status Code = 404 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Cannot PUT /contacts", "testPutContactIdMissingInURL : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutInvalidContactIdInURL(){
		String reqJsonFile = "requestPayload/TestPutRequest.json";
		Response response = processPutRequest("/id1234",reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 500 , "testPutInvalidContactIdInURL : Expected Status Code = 500 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Internal Server Error", "testPutInvalidContactIdInURL : Unexpected error message.");
		softly.assertAll();
	}
	
	
	@Test(priority=1)
	public void testPutInvalidContactIdInURL2(){
		String reqJsonFile = "requestPayload/TestPutRequest.json";
		Response response = processPutRequest("/111b111e11e1111111e1111a",reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 500 , "testPutInvalidContactIdInURL2 : Expected Status Code = 500 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Internal Server Error", "testPutInvalidContactIdInURL2 : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutBadRequest(){
		String reqJsonFile = "requestPayload/TestBadRequest.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutBadRequest : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Bad Request", "testPutBadRequest : Unexpected error message.");
		softly.assertAll();
	}

	@Test
	public void testPutBlankRequest(){
		String reqJsonFile = "requestPayload/TestBlankRequest.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutBlankRequest : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPutBlankRequest : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutAdditionalRequestFields(){
		String reqJsonFile = "requestPayload/TestAdditionalRequestFields.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 400 , "testPutAdditionalRequestFields : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Please provide a valid input request.","testPutAdditionalRequestFields : Unexpected error message.");
		
	}
	
	@Test
	public void testPutFirstNameMissing(){
		String reqJsonFile = "requestPayload/TestFirstNameMissing.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutAdditionalRequestFields : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPutAdditionalRequestFields : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutFirstNameBlank(){
		String reqJsonFile = "requestPayload/TestFirstNameBlank.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutFirstNameBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPutFirstNameBlank : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutFirstNameNull(){
		String reqJsonFile = "requestPayload/TestFirstNameNull.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutFirstNameNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPutFirstNameNull : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutFirstNameSpace(){
		String reqJsonFile = "requestPayload/TestFirstNameSpace.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 400 , "testPutFirstNameSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Must provide a first name.","testPutFirstNameSpace : Unexpected error message.");

	}
	
	@Test
	public void testPutFirstNameNumeric(){
		String reqJsonFile = "requestPayload/TestFirstNameNumeric.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
//		SoftAssert softly = new SoftAssert();
//		softly.assertEquals(response.statusCode(), 400 , "testPutFirstNameNumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
//		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid first name.","testPutFirstNameNumeric : Unexpected error message.");
//		softly.assertAll();
		assertEquals(response.statusCode() ,400, "testPutFirstNameNumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Must provide a last name.", "testPutFirstNameNumeric : Unexpected response string");
	}
	
	@Test
	public void testPutLastNameMissing(){
		String reqJsonFile = "requestPayload/TestLastNameMissing.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutLastNameMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.","testPutLastNameMissing : Unexpected error message.");
		softly.assertAll();
	}
	
	
	@Test
	public void testPutLastNameBlank(){
		String reqJsonFile = "requestPayload/TestLastNameBlank.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutLastNameBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.","testPutLastNameBlank : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutLastNameNull(){
		String reqJsonFile = "requestPayload/TestLastNameNull.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutLastNameNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a last name.","testPutLastNameNull : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutLastNameSpace(){
		String reqJsonFile = "requestPayload/TestLastNameSpace.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 400 , "testPutLastNameSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Must provide a last name.","testPutLastNameSpace : Unexpected error message.");
		
	}
	
	@Test
	public void testPutLastNameNumeric(){
		String reqJsonFile = "requestPayload/TestLastNameNumeric.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 400 , "testPutLastNameNumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		assertEquals(from(response.asString()).get("error"),"Must provide a valid last name.","testPutLastNameNumeric : Unexpected error message.");
		
	}
	
	@Test
	public void testPutEmailMissing(){
		String reqJsonFile = "requestPayload/TestEmailMissing.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutEmailMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.","testPutEmailMissing : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutEmailBlank(){
		String reqJsonFile = "requestPayload/TestEmailBlank.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutEmailBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.","testPutEmailBlank : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutEmailNull(){
		String reqJsonFile = "requestPayload/TestEmailNull.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutEmailNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a email.","testPutEmailNull : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutInvalidEmail1(){
		String reqJsonFile = "requestPayload/TestInvalidEmail1.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutInvalidEmail1 : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid email.","testPutInvalidEmail1 : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutInvalidEmail2(){
		String reqJsonFile = "requestPayload/TestInvalidEmail2.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutInvalidEmail2 : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid email.","testPutInvalidEmail2 : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPostPhoneMissing(){
		String reqJsonFile = "requestPayload/TestPhoneMissing.json";
		Response response = processPostRequest(reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPostPhoneMissing : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPostPhoneMissing : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneNull(){
		String reqJsonFile = "requestPayload/TestPhoneNull.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneNull : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneNull : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneBlank(){
		String reqJsonFile = "requestPayload/TestPhoneBlank.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneBlank : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneBlank : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneSpace(){
		String reqJsonFile = "requestPayload/TestPhoneSpace.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneSpace : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneSpace : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneFormat1(){
		String reqJsonFile = "requestPayload/TestPhoneFormat1.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 204,"testPutPhoneFormat1 : Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPutPhoneFormat2(){
		String reqJsonFile = "requestPayload/TestPhoneFormat2.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 204,"testPutPhoneFormat2 : Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPutPhoneFormat3(){
		String reqJsonFile = "requestPayload/TestPhoneFormat3.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		assertEquals(response.statusCode(), 204,"testPutPhoneFormat3 : Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
	}
	
	@Test
	public void testPutPhoneLengthOverTen(){
		String reqJsonFile = "requestPayload/TestPhoneLengthOverTen.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneLengthOverTen : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneLengthOverTen : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneLengthUnderTen(){
		String reqJsonFile = "requestPayload/TestPhoneLengthUnderTen.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneLengthUnderTen : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneLengthUnderTen : Unexpected error message.");
		softly.assertAll();
	}
	
	@Test
	public void testPutPhoneAlphanumeric(){
		String reqJsonFile = "requestPayload/TestPhoneAlphanumeric.json";
		Response response = processPutRequest(contact_id,reqJsonFile);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 400 , "testPutPhoneAlphanumeric : Expected Status Code = 400 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(from(response.asString()).get("error"),"Must provide a valid phone number.","testPutPhoneAlphanumeric : Unexpected error message.");
		softly.assertAll();
	}

} 