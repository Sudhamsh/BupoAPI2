package com.bupo.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GenerateAutoDataFile {

	// Manufacturers > Makes/year > Models/year
	// There is some complexity in the data as its an external source. Using APIs
	// with Ids instead of name to reduce String error.

	public static void main(String[] args) {

		// https://vpic.nhtsa.dot.gov/api
		// Get All manufacturers
		// https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json&page=1

		Map<Integer, String> manufactureIdAndNameMap = getAllManufacturers();

		// Get data for last 30 years
		int latestYear = Calendar.getInstance().get(Calendar.YEAR);
		Map<String, Collection<Integer>> makeIdsByYearMap = new HashMap<String, Collection<Integer>>();
		JsonObject makesByYearJsonObj = new JsonObject();
		Gson gson = new Gson();

		List<ModelsBean> modelsBeansList = new ArrayList<ModelsBean>();
		for (int i = (latestYear - 30); i <= latestYear; i++) {

			System.out.println("Staring ....." + i);
			// Get Makes for Manufacturer by Manufacturer Name and Year
			Set<String> makesNameSet = new HashSet<String>();

			// Store all makes (name, id) for the year to get models
			Map<String, Integer> allMakeNameAndIdByYear = new HashMap<String, Integer>();
			ModelsBean modelsBean = new ModelsBean();
			modelsBean.setYear(i);
			for (Map.Entry<Integer, String> entry : manufactureIdAndNameMap.entrySet()) {

				// makesByYearMap
				Integer manufId = entry.getKey();
				Map<String, Integer> makeNameAndId = getMakesByYear(manufId, i);
				makesNameSet.addAll(makeNameAndId.keySet());
				allMakeNameAndIdByYear.putAll(makeNameAndId);
			}

			modelsBean.setAllMakeNameAndId(allMakeNameAndIdByYear);
			modelsBeansList.add(modelsBean);
			makesByYearJsonObj.add(Integer.toString(i), gson.toJsonTree(makesNameSet));

		}

		try {
			CommonUtils.writeToFile("MakesByYear.json", makesByYearJsonObj.toString());
			CommonUtils.writeToFile("ModelsMakesYear.json", getModelByMakes(modelsBeansList).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(makesByYearJsonObj);

		System.out.println("-----------------DONE--------------------");
	}

	@Test
	public void getModelByMakesTest() {
		List<ModelsBean> modelsBeans = new ArrayList<ModelsBean>();

		ModelsBean modelsBean = new ModelsBean();
		modelsBean.setYear(2015);
		Map<String, Integer> allMakeNameAndId = new HashMap<String, Integer>();
		allMakeNameAndId.put("HONDA", 474);
		modelsBean.setAllMakeNameAndId(allMakeNameAndId);
		modelsBeans.add(modelsBean);

		getModelByMakes(modelsBeans);
	}

	static JsonObject getModelByMakes(List<ModelsBean> modelsBeans) {
		JsonObject modelsObj = new JsonObject();
		Gson gson = new Gson();
		for (ModelsBean modelsBean : modelsBeans) {
			JsonObject makeAndModels = new JsonObject();
			for (Map.Entry<String, Integer> entry : modelsBean.getAllMakeNameAndId().entrySet()) {
				// Get models by year and make

				makeAndModels.add(entry.getKey(),
						gson.toJsonTree(getModelsByMakeIdAndYear(modelsBean.getYear(), entry.getValue())));
			}

			modelsObj.add(Integer.toString(modelsBean.getYear()), makeAndModels);
		}

		System.out.println(modelsObj);

		return modelsObj;

	}

	@Test
	public void getModelsByMakeIdAndYearTest() {
		getModelsByMakeIdAndYear(2015, 474);
	}

	static Set<String> getModelsByMakeIdAndYear(int year, Integer makeId) {

		String urlTemplate = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMakeIdYear/makeId/<makeId>/modelyear/<year>?format=json";
		String url = urlTemplate.replaceFirst("<makeId>", makeId.toString()).replaceFirst("<year>",
				Integer.toString(year));
		System.out.println(url);
		Set<String> modelsSet = new HashSet<String>();

		JsonObject makesByYear = CommonUtils.getJsonURLResonse(url);
		JsonArray results = makesByYear.getAsJsonArray("Results");

		for (int j = 0; j < results.size(); j++) {
			JsonObject result = results.get(j).getAsJsonObject();
			modelsSet.add(result.getAsJsonPrimitive("Model_Name").getAsString());
		}
		System.out.println("modelsSet:" + modelsSet);

		return modelsSet;
	}

	@Test
	public void getMakesByYearTest() {
		getMakesByYear(988, 2014);
	}

	static Map<String, Integer> getMakesByYear(Integer manufactureId, int year) {

		String urlTemplate = "https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForManufacturerAndYear/<manufId>?year=<year>&format=json";
		String url = urlTemplate.replaceFirst("<manufId>", manufactureId.toString()).replaceFirst("<year>",
				Integer.toString(year));

		System.out.println("url:" + url);

		Map<String, Integer> makesMap = new HashMap<String, Integer>();

		JsonObject makesByYear = CommonUtils.getJsonURLResonse(url);
		JsonArray results = makesByYear.getAsJsonArray("Results");

		for (int j = 0; j < results.size(); j++) {
			JsonObject result = results.get(j).getAsJsonObject();
			makesMap.put(result.getAsJsonPrimitive("MakeName").getAsString(),
					result.getAsJsonPrimitive("MakeId").getAsInt());
		}

		return makesMap;
	}

	final static Set<String> interesedVehicleTypeSet = new HashSet<String>(Arrays.asList("Passenger Car", "Truck"));

	@Test
	public void getAllManufacturersTest() {
		getAllManufacturers();
	}

	public static Map<Integer, String> getAllManufacturers() {
		Map<Integer, String> manufactureIdAndNameMap = new HashMap<Integer, String>();

		for (int i = 1; true; i++) {
			JsonObject allManufac = CommonUtils.getJsonURLResonse(
					"https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json&page=" + i);
			JsonArray results = allManufac.getAsJsonArray("Results");

			if (results == null || results.size() == 0) {
				break;
			}
			for (int j = 0; j < results.size(); j++) {

				JsonObject result = results.get(j).getAsJsonObject();

				// add manufactures if the vehicle type made by them is in the white list
				JsonArray vehTypes = result.getAsJsonArray("VehicleTypes");
				System.out.println(result.get("Mfr_ID") + ":" + result.get("Mfr_CommonName"));
				if (vehTypes != null) {
					for (int k = 0; k < vehTypes.size(); k++) {
						JsonElement mfrCommName = result.has("Mfr_CommonName")
								&& !result.get("Mfr_CommonName").isJsonNull() ? result.get("Mfr_CommonName") : null;

						if (mfrCommName != null && interesedVehicleTypeSet
								.contains(vehTypes.get(k).getAsJsonObject().getAsJsonPrimitive("Name").getAsString())) {
							manufactureIdAndNameMap.put(result.getAsJsonPrimitive("Mfr_ID").getAsInt(),
									mfrCommName.getAsString());
							break;
						}
					}
				}

			}

		}

		System.out.println("manufactureSet :" + manufactureIdAndNameMap);

		return manufactureIdAndNameMap;

	}

}
