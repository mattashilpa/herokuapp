package com.intuit.rest.herokuapp;

import static io.restassured.path.json.JsonPath.from;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.intuit.rest.herokuapp.common.BaseTest;

import io.restassured.response.Response;

public class DeleteTest extends BaseTest {
	public static Response response;
	public static String contactId;
	
	@BeforeClass
	public void getContactIdFromPost(){
		// post a new contact resource before the beginning of tests
		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
		response = processPostRequest(postReqJsonFile);
		if(response.statusCode() != 201)
			Assert.fail("Error in POST operation." + response.asString());
		contactId = from(response.asString()).get("_id"); // get contact_id from POST response
	}
	@Test
	public void testDeleteById(){
//		//Post a new contact
//		String postReqJsonFile = "requestPayload/TestValidPostRequest.json";
//		Response responsePost = processPostRequest(postReqJsonFile);
//		SoftAssert softly = new SoftAssert();
//		softly.assertEquals(response.statusCode(), 201, "testDeleteById : Expected Status Code = 201 || Actual Status Code = " + response.statusCode());
//		contactId = from(responsePost.asString()).get(_ID); //Fetch contact_id from response
		SoftAssert softly = new SoftAssert();
		//Delete the contact
		response = processDeleteRequest("/" + contactId);
		softly.assertEquals(response.statusCode(), 204, "testDeleteById : Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.getHeader("Content-Length"),"0","testDeleteById : Unexpected Content-Length in response.");
//		assertEquals(response.body().toString(),"");
		
		//Verify the contact is deleted from system via GET call
		response = processGetRequest("/" +contactId);
		softly.assertEquals(response.statusCode(),200, "testDeleteById : Expected Status Code = 200 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString(),"Contact does not exist.","testDeleteById :Unexpected response in GET call on deleted contact.");
		softly.assertAll();
	}
	
	@Test
	public void testPerformDeleteMultipleTimes(){
		//Delete the contact
		response = processDeleteRequest("/" + contactId);
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 204, "testPerformDeleteMultipleTimes : First Delete Call - Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.getHeader("Content-Length"),"0","testPerformDeleteMultipleTimes : First Delete Call - Unexpected Content-Length in response.");

		
		//Delete the contact - perform DELETE second time on same Id
		response = processDeleteRequest("/" + contactId);
		softly.assertEquals(response.statusCode(), 204, "testPerformDeleteMultipleTimes : Second Delete Call - Expected Status Code = 204 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.getHeader("Content-Length"),"0","testPerformDeleteMultipleTimes : Second Delete Call - Unexpected Content-Length in response.");
		softly.assertAll();
	}
	
	@Test
	public void testDeleteWithNoId(){
		//Delete by invalid contactId
		response = processDeleteRequest("");
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 404, "testDeleteWithNoId : Expected Status Code = 404 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString().trim(),"Cannot DELETE /contacts","testDeleteWithNoId : Unexpected response body.");
		softly.assertAll();
	}
	
	@Test
	public void testDeleteByInvalidId(){
		//Delete by invalid contactId
		response = processDeleteRequest("/nonexistingID123");
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 500, "testDeleteByInvalidId : Expected Status Code = 500 || Actual Status Code = " + response.statusCode());
		softly.assertEquals(response.asString(),"Contact Not Found","testDeleteByInvalidId : Unexpected response body.");
		softly.assertAll();
	}
	
	@Test 
	public void testDeleteByInvalidId2(){
		//Delete by non existing contactId (in proper format)
		response = processDeleteRequest("/111b111e11e1111111e1111a");
		SoftAssert softly = new SoftAssert();
		softly.assertEquals(response.statusCode(), 500, "testDeleteByInvalidId2 : Expected Status Code = 500 || Actual Status Code = " + response.statusCode());
//		softly.assertEquals(response.getHeader("Content-Length"),"0","testDeleteByInvalidId2 : Unexpected Content-Length in response.");
		softly.assertEquals(response.asString(),"Contact Not Found","testDeleteByInvalidId2 : Unexpected response body.");
		softly.assertAll();
	}

}
