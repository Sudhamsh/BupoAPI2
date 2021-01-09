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

import com.reit.api.SettingsApi;

public class SettingsApiTest extends JerseyTest {

	@Override
	protected Application configure() {

		ResourceConfig resourceConfig = new ResourceConfig(SettingsApi.class);
		return resourceConfig;
	}

	@Test
	public void addFav_hp() {

		try {
			Response response = target("reit/settings/propertyId/1234").request(MediaType.APPLICATION_JSON)
					.put(Entity.entity("{}", MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}

}
