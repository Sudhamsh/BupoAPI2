package com.bupo.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.reit.api.PropertyAPI;

public class PropertyApiTest extends JerseyTest {
	@Override
	protected Application configure() {
		return new ResourceConfig(PropertyAPI.class);
	}

	@Test
	public void getProperties_happy_path() {

		try {

			Response response = target("/reit/property").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(null, MediaType.APPLICATION_JSON));
			if (response == null) {
				Assert.fail("Auto Request creation failed.");
			}
			String responseStr = (String) response.readEntity(String.class);
			System.out.println("responseStr: " + responseStr);

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

	@Test
	public void addNotes() {
		try {
			Response response = target("reit/property/propertyId/5ff941b77ad1820c4689aff5/notes/abcd")
					.request(MediaType.APPLICATION_JSON).put(Entity.entity("{}", MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}

	@Test
	public void getNotes() {
		try {
			Response response = target("reit/property/propertyId/5ff941b77ad1820c4689aff5/notes")
					.request(MediaType.APPLICATION_JSON).get();
			String responseStr = (String) response.readEntity(String.class);
			System.out.println("responseStr: " + responseStr);

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}

}
