package com.reit.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import com.reit.beans.Address;
import com.reit.beans.NotesBean;
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
			SearchFilter<String> filter = new SearchFilter("status", "New", FilterOperator.EQUALS);
			filters.add(filter);

			SearchFilter<Double> remainingFilter = new SearchFilter("remainingTerm", 5, FilterOperator.GREATER_THAN);
			filters.add(remainingFilter);

			List<PropertyResultsBean> properties = propertyService.findMatchingProperties(filters);
			System.out.println("properties size : " + properties.get(0).getId());
			Assert.assertTrue("Couldn't find properties with cap: " + cap, properties.size() > 0);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception on find properties");
		}

	}

	@Test
	public void addNotes() {

		// Create prop
		PropertyBean propertyBean = getDummyPropertyBean();
		ObjectId objId = propertyService.createNewProperty(propertyBean);

		// add notes

		try {
			propertyService.addNotes(objId, new NotesBean("a@a.com", "Notes test1"));

			PropertyBean propertyBean2 = propertyService.getPopertyById(objId);
			Assert.assertTrue("Prop Notes size is not 1", propertyBean2.getPropNotes().size() == 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Save notes failed with an exception." + e.getMessage());
		}
	}

	public static PropertyBean getDummyPropertyBean() {
		PropertyBean propertyBean = new PropertyBean();
		Address address = new Address();
		TenantDetails tenantDetails = new TenantDetails();
		address.setFullAddress(System.currentTimeMillis() + " 3929 Vineyard Dr,Dunkirk,NY,14048");

		propertyBean.setTenantDetails(tenantDetails);
		propertyBean.setAddress(address);

		return propertyBean;

	}

	@Test
	public void addTags() {

		// Create prop
		PropertyBean propertyBean = getDummyPropertyBean();
		propertyBean.setTags(new HashSet<>(Arrays.asList("Tag1", "Tag2")));
		ObjectId objId = propertyService.createNewProperty(propertyBean);
		System.out.println(objId.toHexString());
		// Find and validate
		propertyBean = propertyService.getPopertyById(objId);
		Assert.assertSame("Tags size is not 2 :)", 2, propertyBean.getTags().size());

	}

}
