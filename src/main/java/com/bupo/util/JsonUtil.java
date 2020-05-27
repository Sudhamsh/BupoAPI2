package com.bupo.util;

import com.google.gson.Gson;

public class JsonUtil {

	public static boolean isJson(String json) {
		if (json == null) {
			return false;
		}

		Gson gson = new Gson();
		try {
			gson.fromJson(json, Object.class);
			Object jsonObjType = gson.fromJson(json, Object.class).getClass();
			if (jsonObjType.equals(String.class)) {
				return false;
			}
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

}
