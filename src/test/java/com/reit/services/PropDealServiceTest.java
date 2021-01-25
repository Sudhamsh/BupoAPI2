package com.reit.services;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import com.reit.beans.PropDealBean;
import com.reit.beans.PropertyBean;

public class PropDealServiceTest {
	private PropDealService propDealSev = new PropDealService();
	private PropertyService propServ = new PropertyService();

	@Test
	public void createPropdeal_hp() {
		PropDealBean propDeal = new PropDealBean();

		// create property
		PropertyBean propertyBean = PropertyServiceTest.getDummyPropertyBean();
		ObjectId propId = propServ.createNewProperty(propertyBean);

		propDeal.setPropObjId(propId);
		propDeal.setTenantObjId(new SaasTenantService().getSaasTenant(new TokenService().getTenantName()).getId());

		ObjectId propDealId = propDealSev.createDeal(propDeal);
		Assert.assertNotNull("Prop ID create didn't return object id", propDealId);
	}
}
