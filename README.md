
### Introduction
Project herokuapp automation :  Automation test cases for **https://shielded-river-86069.herokuapp.com/contacts** service.
- - - -
### API Reference

Header: key=Authentication, value=facetking
Sample GET CURL request: curl https://shielded-river-86069.herokuapp.com/contacts/ -H "Authentication:facetking"
Sample POST CURL request: 

curl -XPOST -H 'Authentication: facetking' -H "Content-type: application/json" -d '{
    "firstName": "Steven",    
        "lastName" :"Univ",
    "email": "support@lab.com",
    "phoneNumber":"1234567890"
    }' 'https://shielded-river-86069.herokuapp.com/contacts'


###Installation

Clone Repo.
git clone https://github.com/mattashilpa/herokuapp.git

Running the project

    * Do "mvn clean install -U" in order to download all artifacts

###Tests

 * Verifying POST use cases.
    * verify successful POST use case
    * verify unsuccessful POST use case
    * validate request parameters
    * validate authentication
 
 * Verifying PUT use cases.
    * verify successful PUT use case
    * verify missing ID in URL
    * verify invalid ID in URL
    * verify nonexisting ID in URL
    * validate request parameters
    * validate authentication
    
 * Verifying DELETE use cases.
    * Verify DELETE by ID
    * validate authentication
    * verify missing ID in URL
    * verify invalid ID in URL
    * verify nonexisting ID in URL
 
 * Verify GET use cases.
    * Verify GET by ID
    * Verify GET general
    * validate authentication
    * verify missing ID in URL
    * verify invalid ID in URL
    * verify nonexisting ID in URL

###Running Test
* To run only Post Tests "mvn test -Dtest=PostTest"
* To run only Put Tests "mvn test -Dtest=PutTest"
* To run only Post Tests "mvn test -Dtest=GetTest"
* To run only Post Tests "mvn test -Dtest=DeleteTest"
* To run only Post Tests "mvn test -Dtest=GeneralTest"
* To run entire test suite "mvn test -DtestSuiteXML=TestNG.xml"



```java
Test File : PostTest.java

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
	
		// verify ContactId is added via get() call
		response = processGetRequest("");
		List<String> ids = response.jsonPath().getList("_id");
		softly.assertTrue(ids.contains(contactId) , "testPostContact : ContactId does not exist!");
		softly.assertAll();
	}
```
