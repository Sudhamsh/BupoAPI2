package com.reit.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.reit.beans.Address;
import com.reit.beans.PropertyBean;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;
import com.reit.beans.TenantDetails;
import com.reit.enums.FilterOperator;

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

	@Test
	public void findMatchingProperties_cc() {
		try {
			propertyService.findMatchingProperties(null);
			Assert.fail("Find properties accepted null param");
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			propertyService.findMatchingProperties(new ArrayList<SearchFilter>());
			Assert.fail("Find properties accepted  empty list");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void findMatchingProperties_hp() {

		try {
			float cap = 3;
			List<SearchFilter> filters = new ArrayList<SearchFilter>();
			SearchFilter<Double> filter = new SearchFilter("cap", 3, FilterOperator.GREATER_THAN);
			filters.add(filter);
			List<PropertyResultsBean> properties = propertyService.findMatchingProperties(filters);
			System.out.println("properties size : " + properties.size());
			Assert.assertTrue("Couldn't find properties with cap: " + cap, properties.size() > 0);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception on find properties");
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
