package com.bupo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.Address;
import com.bupo.beans.AutoDriver;
import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.model.UserAuto;
import com.bupo.enums.EducationLevel;
import com.google.gson.Gson;

public class AutoPolicyServiceTest {

	@Test
	public void createAutoPolcyRequest_HP() {

		String code = createPolicy();
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

		String code = createPolicy();

		AutoPolicyService autoPolicyService = new AutoPolicyService();

		// Get the newly created object
		Document userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);
		System.out.println(userAuto.toString());
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
			Document userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);
			Assert.assertNull("Found a auto request for a random entry, weird...", userAuto);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
		} catch (Exception e) {
			Assert.fail("Got generic expection, was expecting EntityNotFoundException ");
		}

	}

	private String email = "a@a.c";
	private String zip = "4000";
	private String lastName = "lName";

	@Test
	public void createAutoPolcyRequestHappyPath() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();

		String code = createPolicy();
		Assert.assertNotNull("Code is null on create auto policy request", code);

		Document userAuto = autoPolicyService.getAutoPolicyDetails(code, null, true);
		System.out.println(new Gson().toJsonTree(userAuto));

	}

	private String createPolicy() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setEmail(email);
		autoPolicyRequest.setLastName(lastName);

		// Add driver for anonymization test
		List<AutoDriver> drivers = new ArrayList<AutoDriver>();
		AutoDriver autoDriver = new AutoDriver(10, EducationLevel.DOCTORAL_DEGREE);
		autoDriver.setEmail(email);
		drivers.add(autoDriver);

		Address address = new Address();
		address.setZip(zip);
		autoPolicyRequest.setAddress(address);

		autoPolicyRequest.setDrivers(drivers);

		return autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
	}

	@Test
	public void getOpenAutoPolicy_hp() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();
		try {
			createPolicy();
			List<UserAuto> userAutoList = autoPolicyService.getOpenAutoPolicy();
			System.out.println(userAutoList.size());
			System.out.println(new Gson().toJson(userAutoList));
			Assert.assertTrue("Open Policy creation failed. Search returned 0 restuls.", userAutoList.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void createAutoPolcyRequestHappyFail() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();

		try {
			autoPolicyService.createAutoPolcyRequest(null);
			Assert.fail("Create Worked on null ;) ");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
