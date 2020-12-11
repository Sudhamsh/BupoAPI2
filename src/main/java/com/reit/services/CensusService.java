package com.reit.services;

import javax.ws.rs.core.Response;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.reit.util.JsonUtil;

public class CensusService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static String REST_URI = "https://api.census.gov/data/2017/acs/acs5?get=NAME,group(B19013)&for=zip%20code%20tabulation%20area:<zip>";

	public int getMedianIncome(int zip) {
		int income = 0;

		String url = REST_URI.replace("<zip>", Integer.toString(zip));
		Response response = JsonUtil.getJsonResponse(url);
		String responseStr = response.readEntity(String.class);

		if (JsonUtil.isJson(responseStr)) {
			JsonArray responseArray = JsonParser.parseString(responseStr).getAsJsonArray();
			income = JsonUtil.getValueByKey(responseArray, "B19013_001E").getAsInt();
		}

		return income;

	}

	public int getPopulation(int zip) {
		int population = 0;

		return population;
	}

}
