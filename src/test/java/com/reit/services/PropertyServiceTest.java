package com.reit.services;

import org.junit.Assert;
import org.junit.Test;

import com.reit.beans.Address;
import com.reit.beans.PropertyBean;
import com.reit.beans.TenantDetails;

public class PropertyServiceTest {

	PropertyService propertyService = new PropertyService();

	@Test
	public void createNewProperty_hp() {

		PropertyBean propertyBean = getDummyPropertyBean();

		propertyService.createNewProperty(propertyBean);
	}

	@Test
	public void createNewProperty_cc() {

		try {
			propertyService.createNewProperty(null);
			Assert.fail("Create property service accepted null param");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public PropertyBean getDummyPropertyBean() {
		PropertyBean propertyBean = new PropertyBean();
		Address address = new Address();
		TenantDetails tenantDetails = new TenantDetails();
		address.setFullAddress(System.currentTimeMillis() + " 3929 Vineyard Dr,Dunkirk,NY,14048");

		propertyBean.setTenantDetails(tenantDetails);
		propertyBean.setAddress(address);

		return propertyBean;

	}

}
