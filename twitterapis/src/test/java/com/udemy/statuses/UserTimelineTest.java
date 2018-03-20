package com.udemy.statuses;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.udemy.common.RestUtilities;
import com.udemy.constants.EndPoints;
import com.udemy.constants.Path;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class UserTimelineTest {
 
  RequestSpecification reqSpec;
  ResponseSpecification respSpec;
	
  @BeforeClass
  public void setup() {
	  reqSpec = RestUtilities.getRequestSpecification();
	  reqSpec.queryParam("screen_name", "Juan Manuel Bertoni");
	  reqSpec.basePath(Path.STATUSES);
	  respSpec = RestUtilities.getResponseSpecification();
  }

  @Test(priority=1)
  public void readTweets() {
	 given()
	 	.spec(reqSpec)
	 .when()
	 	.get(EndPoints.STATUSES_USER_TIMELINE)
	 .then()
	 	.spec(respSpec)
	 	.rootPath("[2].user")
		.body("screen_name",equalTo("jotaM1982"))
		.body("name",equalTo("Juan Manuel Bertoni"))
		.rootPath("[2].retweeted_status.user")
		.body("name", equalTo("Julia Mengolini"))
		.rootPath("[19].retweeted_status.user")
		.body("name", equalTo("Exa"));
  }
  
  @Test(priority=2)
  public void readThreeLatestTweets() {
	 given()
	 	.spec(RestUtilities.createQueryParam(reqSpec, "count","3"))
	 .when()
	 	.get(EndPoints.STATUSES_USER_TIMELINE)
	 .then()
	 	.spec(respSpec)
	 	.rootPath("[2].user")
		.body("screen_name",equalTo("jotaM1982"))
		.body("name",equalTo("Juan Manuel Bertoni"))
		.rootPath("[2].retweeted_status.user")
		.body("name", equalTo("Julia Mengolini"));		
  }
  
  @Test(priority=2)
  public void readTweetsResponse() {
	  RestUtilities.setEndPoint(EndPoints.STATUSES_USER_TIMELINE);
	  Response res = RestUtilities.getResponse(reqSpec,"get");
	  }
}
