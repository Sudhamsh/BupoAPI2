package com.bupo.services;

import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.model.UserAuto;

public class AutoPolicyServiceTest {

	@Test
	public void createAutoPolcyRequest_HP() {
		// Create request
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setLastName("LastName");
		autoPolicyRequest.getAddress().setZip("40000");

		AutoPolicyService autoPolicyService = new AutoPolicyService();
		String code = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
		System.out.println(code.length() + "code :" + code);
		Assert.assertNotNull("Auto Policy Code return value is null", code);
		Assert.assertSame("Auto Policy Code length is Not 23", 23, code.length());

	}

	@Test
	public void createAutoPolcyRequest_CC() {

		AutoPolicyService autoPolicyService = new AutoPolicyService();

		try {
			autoPolicyService.createAutoPolcyRequest(null);
			Assert.fail(" Auto Policy Creation accepted null");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void getAutoPolicyDetails_HP() {

		// Create and then get it
		String lastName = "LastName";
		String zip = "40000";
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();

		autoPolicyRequest.setLastName(lastName);
		autoPolicyRequest.getAddress().setZip(zip);
		// TODO set more data

		AutoPolicyService autoPolicyService = new AutoPolicyService();
		String code = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);

		// Get the newly created object
		UserAuto userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);
		Assert.assertNotNull("Get failed for a newly created auto request, weird...", userAuto);

	}

	@Test
	public void getAutoPolicyDetails_CC() {

		Random rand = new Random();
		String lastName = "LastNameCC";
		String zip = Integer.toString(rand.nextInt(99999));
		String code = Integer.toString(rand.nextInt(9999999));

		// Get a random object
		AutoPolicyService autoPolicyService = new AutoPolicyService();
		try {
			UserAuto userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);
			Assert.assertNull("Found a auto request for a random entry, weird...", userAuto);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
		} catch (Exception e) {
			Assert.fail("Got generic expection, was expecting EntityNotFoundException ");
		}

	}

}
