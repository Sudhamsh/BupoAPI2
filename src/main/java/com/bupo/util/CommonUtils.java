package com.bupo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommonUtils {

	public static Response getURLResonse(String url) {
		Client client = ClientBuilder.newClient();

		// query params: ?q=Turku&cnt=10&mode=json&units=metric
		WebTarget target = client.target(url);

		Response response = target.request(MediaType.APPLICATION_JSON).get();

		return response;

	}

	public static JsonObject getJsonURLResonse(String url) {
		System.out.println(url);
		Response response = getURLResonse(url);
		JsonObject jsonObject = new JsonObject();
		Gson gson = new Gson();
		try {
			String responseStr = response.readEntity(String.class);
			jsonObject = JsonParser.parseString(responseStr).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return jsonObject;

	}

	public static void writeToFile(String fileName, String content) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(content);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

	public static String readAllBytes(String filePath) {
		StringBuffer content = new StringBuffer();

		try {

			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream(filePath);
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			for (String line; (line = reader.readLine()) != null;) {
				content.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return content.toString();
	}

}
