package com.bupo.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoDriver;
import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.model.UserAuto;
import com.bupo.enums.EducationLevel;
import com.bupo.services.AutoPolicyService;
import com.google.gson.Gson;

public class AutoPolicyServiceTest {

	@Test
	public void createAutoPolcyRequestHappyPath() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();

		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setEmail("a@a.c");

		// Add driver for anonymization test
		List<AutoDriver> drivers = new ArrayList<AutoDriver>();
		AutoDriver autoDriver = new AutoDriver(10, EducationLevel.DOCTORAL_DEGREE);
		autoDriver.setEmail("a@a.com");
		drivers.add(autoDriver);

		autoPolicyRequest.setDrivers(drivers);

		String code = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
		Assert.assertNotNull("Code is null on create auto policy request", code);

		UserAuto userAuto = autoPolicyService.getAutoPolicyDetails(code, null, true);
		System.out.println(new Gson().toJsonTree(userAuto));

	}

	private String createPolicy() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setEmail("a@a.c");

		// Add driver for anonymization test
		List<AutoDriver> drivers = new ArrayList<AutoDriver>();
		AutoDriver autoDriver = new AutoDriver(10, EducationLevel.DOCTORAL_DEGREE);
		autoDriver.setEmail("a@a.com");
		drivers.add(autoDriver);

		autoPolicyRequest.setDrivers(drivers);

		return autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
	}

	@Test
	public void getOpenAutoPolicy_hp() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();
		createPolicy();
		List<UserAuto> userAutoList = autoPolicyService.getOpenAutoPolicy();
		Assert.assertTrue("Open Policy creation failed. Search returned 0 restuls.", userAutoList.size() > 0);
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
