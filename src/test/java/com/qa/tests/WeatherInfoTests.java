package com.qa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.util.TestBase;
import com.qa.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherInfoTests extends TestBase {
	
	@BeforeMethod
	public void setUP()
	{
		TestBase.init();	
	}

	@DataProvider
	public Object[][] getData() {
		Object[][] Testdata = TestUtil.getDataFromSheet(TestUtil.WeathersheetName);
		return Testdata;
	}

	@Test(dataProvider = "getData")
	public void getWeatherDetailsWithCorrectCityNameTest(String city, String HTTPMethod, String temperature,
			String humidity, String weatherdescription, String windSpeed, String winddirectiondegree) {
		// 1.Define Base URL.
		// http://restapi.demoqa.com/utilities/weather/city

		RestAssured.baseURI = prop.getProperty("serviceURL");

		// 2.define HTTP request--make an request so that we can send it server
		RequestSpecification httpRequest = RestAssured.given();

		// 3.make/execute request
		Response response = httpRequest.request(Method.GET + city);

		// 4.Get response body--after sending req we get response
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is:" + responseBody);

		// validate city name or validate the key or value
		Assert.assertEquals(responseBody.contains(city), true);

		// 5.get response status and validate it
		int statusCode = response.getStatusCode();
		System.out.println("The status code is:  " + statusCode);

		Assert.assertEquals(statusCode, TestUtil.RESPONSE_CODE_200);
		System.out.println("status line is " + response.getStatusLine());

		// 6. get the headers
		Headers headers = response.getHeaders();
		System.out.println(headers);

		// specific header
		String ContentType = response.getHeader("Content-Type");
		System.out.println("the value of content-type is " + ContentType);

		String ContentLength = response.getHeader("Content-Length");
		System.out.println("the value of content-type is " + ContentLength);

		// get the keyvalue/node by using Json Path

		JsonPath jsonPathValue = response.jsonPath();
		String cityval = jsonPathValue.get("City");
		System.out.println("The value of the city" + cityval);

		Assert.assertEquals(cityval, city);

		String temp = jsonPathValue.get("Temperature");
		System.out.println("The value of the Temperature" + temp);
		Assert.assertEquals(temp, temperature);

		// String WeatherDescription = jsonPathValue.get(WeatherDescription);
		// System.out.println("The value of the WeatherDescription" +
		// weatherdescription);
		// Assert.assertEquals(WeatherDescription,weatherdescription );
		//
		//
		//
		// String WindSpeed = jsonPathValue.get(windSpeed);
		// System.out.println("The value of the WindSpeed" + windSpeed);
		// Assert.assertEquals(WindSpeed, windSpeed);
		//
		// String WindDirectionDegree = jsonPathValue.get(winddirectiondegree);
		// System.out.println("The value of the WindDirectionDegree" +
		// WindDirectionDegree);

	}
}
