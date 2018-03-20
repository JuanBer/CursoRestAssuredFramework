package com.udemy.statuses;

import org.testng.annotations.Test;

import com.udemy.common.RestUtilities;
import com.udemy.constants.EndPoints;
import com.udemy.constants.Path;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;

public class TwitterWorkflowTest {
	RequestSpecification reqSpec;
	ResponseSpecification resSpec;
	String tweetID = "";

	@BeforeClass
	public void setup() {
		reqSpec = RestUtilities.getRequestSpecification();
		reqSpec.basePath(Path.STATUSES);
		resSpec = RestUtilities.getResponseSpecification();
	}

	@Test
	public void postTweet() {
		Response res = 
		given()
			.spec(RestUtilities.createQueryParam(reqSpec, "status","Automated tweet created using Rest Assured framework"))
		.when()
			.post(EndPoints.STATUSES_TWEET_POST)
		.then()
			.spec(resSpec).extract().response();
		JsonPath jsPath = RestUtilities.getJsonPath(res);
		tweetID = jsPath.get("id_str");
		System.out.println("Tweet ID: " + tweetID);

	}
	
	@Test(dependsOnMethods= {"postTweet"})
	public void readTweet() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_READ_SINGLE);
		Response res = RestUtilities.getResponse(RestUtilities.createQueryParam(reqSpec, "id", tweetID), "get");
		System.out.println("The tweet text is " +res.path("text"));
		
	}
	
	@Test(dependsOnMethods= {"readTweet"})
	public void deleteTweet() {
		given()
			.spec(RestUtilities.createPathParam(reqSpec, "id", tweetID))
		.when()
			.post(EndPoints.STATUSES_TWEET_DELETE)
		.then()
			.spec(resSpec);
	}
}
