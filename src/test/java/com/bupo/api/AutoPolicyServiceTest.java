package com.bupo.api;

import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.services.AutoPolicyService;

public class AutoPolicyServiceTest {

	@Test
	public void createAutoPolcyRequestHappyPath() {
		AutoPolicyService autoPolicyService = new AutoPolicyService();

		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setEmail("a@a.c");
		autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);

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
