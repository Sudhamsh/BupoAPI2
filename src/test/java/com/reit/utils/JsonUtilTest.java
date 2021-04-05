package com.reit.utils;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.reit.util.CommonUtils;
import com.reit.util.JsonUtil;

public class JsonUtilTest {
	Gson gson = new Gson();

	@SuppressWarnings("deprecation")
	@Test
	public void getValueByKeyTest() {

		try {
			JsonUtil.getValueByKey(null, null);
			Assert.fail("getValueByKey accepted null params");
		} catch (Exception e) {
			// TODO: handle exception
		}

		JsonArray array = JsonParser.parseString(CommonUtils.readAllBytes("census/medianIncomeResponse.json"))
				.getAsJsonArray();
		JsonElement valueElement = JsonUtil.getValueByKey(array, "B19013_001E");
		System.out.println("valueElement : " + valueElement.toString());
		Assert.assertEquals("160542", valueElement.getAsString());

	}

}
