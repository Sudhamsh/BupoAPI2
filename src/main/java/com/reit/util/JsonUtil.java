package com.reit.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonUtil {

	public static Response getJsonResponse(String url) {
		Client client = ClientBuilder.newClient();

		WebTarget webTarget = client.target(url);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();
		// System.out.println(response.readEntity(String.class));

		return response;
	}

	public static boolean isJson(String json) {
		if (json == null || "null".equals(json)) {
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

	// APIs like Census have label and values like a table, we need find the index
	// in the header and get the values
	public static JsonElement getValueByKey(JsonArray array, String fieldName) {
		Preconditions.checkNotNull(array, "Array argument is null");
		Preconditions.checkNotNull(fieldName, "FieldName argument is null");
		Preconditions.checkArgument(array.size() > 1, "Array doesn't have a minimum of two rows to get a value");

		JsonElement valueElement = null;
		JsonArray header = array.get(0).getAsJsonArray();
		for (int i = 0; i < header.size(); i++) {
			JsonElement jsonElement = header.get(i);
			if (jsonElement.getAsString().equals(fieldName)) {
				valueElement = array.get(1).getAsJsonArray().get(i);
			}
		}

		return valueElement;

	}

}
