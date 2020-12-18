package com.reit.util;

import com.opencsv.bean.processor.StringProcessor;

public class StringToBoolean implements StringProcessor {
	String cleanValue = "false";

	@Override
	public String processString(String value) {
		if (value != null && !value.trim().isEmpty()) {
			cleanValue = value.trim().replace("\"", "").replace(",", "").replace("$", "").replace("%", "");
			if (cleanValue.equalsIgnoreCase("Yes") || cleanValue.equalsIgnoreCase("True")) {
				cleanValue = "true";
			}
			return cleanValue;
		}

		return cleanValue;

	}

	@Override
	public void setParameterString(String value) {
		cleanValue = value;

	}

}
