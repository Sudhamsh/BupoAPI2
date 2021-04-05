package com.reit.services;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.reit.beans.LoiRequestBean;
import com.reit.beans.PropertyBean;

public class GenerateDocServiceTest {
	GenerateDocService generateDocService = new GenerateDocService();
	PropertyService propServ = new PropertyService();

	/**
	 * 
	 */
	@Test
	public void generateDoc_hp() {

		// Create a new property
		PropertyBean propertyBean = PropertyServiceTest.getDummyPropertyBean();

		ObjectId propId = propServ.createNewProperty(propertyBean);

		LoiRequestBean loiRequestBean = new LoiRequestBean();
		loiRequestBean.setOfferPrice("1,000,000");
		loiRequestBean.setPropId(propId);
		loiRequestBean.setSellerEmail("reit.dev2021@gmail.com");
		loiRequestBean.setBuyerEmail("sudhamsh.b@gmail.com");

		try {
			generateDocService.generateDoc("dev", "LOI", loiRequestBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
