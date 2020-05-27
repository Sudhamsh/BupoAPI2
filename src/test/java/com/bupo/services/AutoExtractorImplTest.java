package com.bupo.services;

import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicy;
import com.bupo.dao.BaseDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.util.AutoUserTestUtil;

public class AutoExtractorImplTest {

	@Test
	public void persistAutoPolicyDetailsNegativeTest() {
		AutoExtractorImpl autoExtractorImpl = new AutoExtractorImpl();
		try {
			autoExtractorImpl.persistAutoPolicyDetails(null, "/abc", new AutoPolicy());
			Assert.fail("Policy details persisted without user email ");
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			autoExtractorImpl.persistAutoPolicyDetails("abc@g.com", null, new AutoPolicy());
			Assert.fail("Policy details persisted without file path ");
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			autoExtractorImpl.persistAutoPolicyDetails("abc@g.com", "/abc", null);
			Assert.fail("Policy details persisted without policy details ");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void persistAutoPolicyDetailsPositiveTest() {
		AutoExtractorImpl autoExtractorImpl = new AutoExtractorImpl();
		try {
			// create user
			BaseDao baseDao = new BaseDao();
			UserAuto userAuto = AutoUserTestUtil.createUser();
			baseDao.create(userAuto);

			autoExtractorImpl.persistAutoPolicyDetails(userAuto.getEmailId(), "/abc", new AutoPolicy());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception during persist policy");
		}

	}
}
