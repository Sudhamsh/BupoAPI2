package com.bupo.services;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.reit.beans.ZipBean;
import com.reit.services.ZipService;

public class ZipServiceTest {
	ZipService zipservice = new ZipService();

	@Test
	public void getZipRecord_cc() {

		try {
			int zip = 00000;
			ZipBean zipBean = zipservice.findZipRecord(zip);
			Assert.assertNull("Found zip record for zip : " + zip, zipBean);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void getZipRecord_hp() {

		try {
			int zip = 94539;
			// Create then search
			ZipBean zipBean = new ZipBean();
			zipBean.setZip(zip);
			zipservice.createZipRecord(zipBean);

			zipBean = zipservice.findZipRecord(zip);
			System.out.println(new Gson().toJson(zipBean));
			Assert.assertNotNull("Zip Service failed to find the record for zip : " + zip, zipBean);

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

}
