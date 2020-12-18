package com.reit.util;

import com.opencsv.bean.processor.StringProcessor;

public class NumberPreProcessor implements StringProcessor {
	String cleanValue;

	@Override
	public String processString(String value) {
		if (value != null && !value.trim().isEmpty()) {
			cleanValue = value.trim().replace("\"", "").replace(",", "").replace("$", "").replace("%", "");
			System.out.println("cleanValue ::" + cleanValue);
			return cleanValue;
		} else {
			return "0";
		}

	}

	@Override
	public void setParameterString(String value) {
		cleanValue = value;

	}

}