package com.bupo.api;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicyRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AutoPolicyApiTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(AutoPolicyAPI.class);
	}

	@Test
	public void saveAutoDetails_exception_path() {

		try {
			Response response = target("auto").request(MediaType.APPLICATION_JSON).post(null);
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 400 ", 400, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

	@Test
	public void saveAutoDetails_happy_path() {

		try {

			AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
			autoPolicyRequest.setEmail("apitest@a.com");
			Response response = target("auto").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(autoPolicyRequest, MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 201 ", 201, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

	@Test
	public void getAutoPolicyDetails_happy_path() {

		try {

			// Create
			String email = "apitest@a.com";
			String lastName = "lName";

			String zip = "40000";
			AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
			autoPolicyRequest.setEmail(email);
			autoPolicyRequest.setLastName(lastName);
			autoPolicyRequest.setZip(zip);
			Response response = target("auto").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(autoPolicyRequest, MediaType.APPLICATION_JSON));
			if (response == null) {
				Assert.fail("Auto Request creation failed.");
			}
			String responseStr = (String) response.readEntity(String.class);
			System.out.println("responseStr: " + responseStr);
			JsonObject responseObj = new Gson().fromJson(responseStr, JsonObject.class);

			// Get
			String code = responseObj.getAsJsonPrimitive("uniqueCode").getAsString();
			String uri = String.format("/auto/search");

			response = target(uri).queryParam("code", code).queryParam("lastName", lastName).queryParam("zip", zip)
					.request(MediaType.APPLICATION_JSON).get();
			System.out.println("Response" + response.readEntity(String.class));

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

	@Test
	public void getAutoPolicyDetails_CC() {

		try {

			// Create
			Random rand = new Random();
			String lastName = "LastNameCC";
			String zip = Integer.toString(rand.nextInt(99999));
			String code = Integer.toString(rand.nextInt(9999999));

			// Get

			String uri = String.format("/auto/search");

			Response response = target(uri).queryParam("code", code).queryParam("lastName", lastName)
					.queryParam("zip", zip).request(MediaType.APPLICATION_JSON).get();
			System.out.println("Response" + response.readEntity(String.class));

			assertEquals("Http Response should be 400 ", 400, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

}
