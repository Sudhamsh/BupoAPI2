package com.bupo.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.services.UserService;
import com.bupo.services.UserServiceTest;
import com.reit.api.AuthenticationApi;

public class AuthenticationApiTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(AuthenticationApi.class);
	}

	@Test
	public void authenticateUser_happy_path() {

		try {
			// create user first

			UserServiceTest serviceTest = new UserServiceTest();
			User user = serviceTest.populateUserObject();
			UserService userService = new UserService();
			UserRequestBean userBean = new UserRequestBean();
			BeanUtils.copyProperties(userBean, user);
			userService.createUser(userBean);

			Response response = target("/auth").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(userBean, MediaType.APPLICATION_JSON));
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

}
