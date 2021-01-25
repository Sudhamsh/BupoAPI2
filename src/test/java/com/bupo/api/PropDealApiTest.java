package com.bupo.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.reit.api.PropDealApi;
import com.reit.beans.PropDealReq;
import com.reit.beans.PropertyBean;
import com.reit.services.PropertyService;
import com.reit.services.PropertyServiceTest;
import com.reit.services.SaasTenantService;
import com.reit.services.TokenService;
import com.reit.util.GsonUtils;

public class PropDealApiTest extends JerseyTest {

	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = GsonUtils.getGson();

	@Override
	protected Application configure() {

		ResourceConfig resourceConfig = new ResourceConfig(PropDealApi.class);
		return resourceConfig;
	}

	@Test
	public void createPropDeal_hp() {

		try {

			PropDealReq propDeal = new PropDealReq();

			// create property
			PropertyBean propertyBean = PropertyServiceTest.getDummyPropertyBean();
			ObjectId propId = new PropertyService().createNewProperty(propertyBean);

			propDeal.setPropObjId(propId);
			propDeal.setTenantObjId(new SaasTenantService().getSaasTenant(new TokenService().getTenantName()).getId());

			Response response = target("deal").request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(gson.toJson(propDeal), MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());

			assertEquals("Http Response should be 201 ", 201, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed with an exception");
		}
	}
}
