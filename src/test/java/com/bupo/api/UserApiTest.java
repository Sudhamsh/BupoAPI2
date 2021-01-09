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

import com.bupo.beans.UserRequestBean;

public class UserApiTest extends JerseyTest {
	@Override
	protected Application configure() {

		ResourceConfig resourceConfig = new ResourceConfig(UserApi.class);
		return resourceConfig;
	}

	@Test
	public void createUser_hp() {

		try {

			UserRequestBean user = new UserRequestBean();
			user.setEmail(String.valueOf(System.currentTimeMillis()) + "@a.com");
			Response response = target("user").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(user, MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 201 ", 201, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}

	@Test
	public void createUser_cc() {

		try {
			Response response = target("user").request(MediaType.APPLICATION_JSON).post(null);
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 400 ", 400, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}
}
