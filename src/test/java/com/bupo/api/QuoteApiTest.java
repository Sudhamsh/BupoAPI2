package com.bupo.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

public class QuoteApiTest extends JerseyTest {
	@Override
	protected Application configure() {
		return new ResourceConfig(QuoteAPI.class);
	}

	@Test
	public void getQuote_hp() {
		try {
			// Get
			String uri = String.format("/quote/code/NBW9Q-SU7LL-0MSCE-3X7U1");

			Response response = target(uri).request(MediaType.APPLICATION_JSON).get();
			System.out.println("Response" + response.readEntity(String.class));

			assertEquals("Http Response should be 200 ", 200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}

	}

}
