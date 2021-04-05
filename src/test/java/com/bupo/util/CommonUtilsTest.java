package com.bupo.util;

import org.junit.Test;

import com.reit.util.CommonUtils;

public class CommonUtilsTest {

	@Test
	public void getJsonURLResonse() {
		System.out.println(CommonUtils.getJsonURLResonse(
				"https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeyear/make/tesla/modelyear/2000?format=json"));
	}

}
