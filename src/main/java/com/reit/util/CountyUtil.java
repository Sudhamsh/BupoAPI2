package com.reit.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.gson.JsonElement;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class CountyUtil {

	/**
	 * County Search doesn't like state and zip
	 * 
	 * @param address
	 */
	public static String stripStateAndZip(String address, boolean includeCity) {
		String addForSearch = null;

		List<String> addParts = Splitter.on(',').splitToList(address);

		if (addParts.size() < 2) {

			return addForSearch;
		}
		if (includeCity) {
			addForSearch = addParts.get(0) + "," + addParts.get(1);
		} else {
			addForSearch = addParts.get(0);
		}

		return addForSearch;
	}

	public static List<String> getCleanAddressFromFile(String filePath, boolean includeCity) {
		// read properties file
		JsonElement propsElement = CommonUtils.readJsonData(filePath);

		DocumentContext propsContext = JsonPath.parse(propsElement.toString());
		String allPropsPath = "$.dollarGeneral[*]";
		List<String> propsList = propsContext.read(allPropsPath);

		List<String> searchAddressList = new ArrayList<>();

		// clean address for search
		for (String propAddress : propsList) {
			searchAddressList.add(stripStateAndZip(propAddress, includeCity));
		}

		return searchAddressList;
	}

}
