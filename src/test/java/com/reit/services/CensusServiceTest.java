package com.reit.services;

import org.junit.Assert;
import org.junit.Test;

public class CensusServiceTest {
	CensusService censusService = new CensusService();

	@Test
	public void getMedianIncome() {
		int zip = 94539;
		int income = censusService.getMedianIncome(zip);
		System.out.println("income :" + income);
		Assert.assertTrue(String.format("%d is less than 125K. Income came as %d", zip, income), income > 125000);
	}

}
