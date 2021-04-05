package com.reit.county;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.reit.beans.CountyDataBean;
import com.reit.util.CommonUtils;
import com.reit.util.CountyUtil;
import com.reit.util.GsonUtils;

import lombok.Getter;
import lombok.Setter;

public class FLBroward implements CountyData {
	private static Gson gson = GsonUtils.getGson();
	private static LogManager logger = LogManager.getLogger(FLBroward.class);
	private static final String parcelLookupAPI = "https://web.bcpa.net/BcpaClient/search.aspx/GetData";
	private static final String propDetailsAPI = "https://web.bcpa.net/BcpaClient/search.aspx/getParcelInformation";
	private static boolean isTesting = false;

	@Override
	public Map<String, JsonElement> getParcelByAddress(List<String> addressList) {
		Preconditions.checkNotNull(addressList, "Address List is null");
		Preconditions.checkArgument(addressList.size() > 0, "Address List is empty");
		Map<String, JsonElement> addressParcelMap = new HashMap<>();

		for (String address : addressList) {

			JsonElement responseElement = null;
			if (isTesting) {
				responseElement = CommonUtils.readJsonData("data/api/samples/fl/broward/parcelLookup.json");
			} else {
				ParcelLookupPayload parcelPayload = new ParcelLookupPayload(address);
				Response response = CommonUtils.httpPost(parcelLookupAPI, gson.toJson(parcelPayload));
				String strResp = response.readEntity(String.class);
				responseElement = JsonParser.parseString(strResp);
			}

			System.out.println(gson.toJson(responseElement));

			addressParcelMap.put(address, responseElement);

			// break;
		}
		return addressParcelMap;
	}

	@Override
	public JsonElement getPropertyDetails(String parcelId) {
		JsonElement responseElement = null;

		if (isTesting) {
			responseElement = CommonUtils.readJsonData("data/api/samples/fl/broward/parcelDetails.json");

		} else {
			ParcelDetailsPayload detailsPayload = new ParcelDetailsPayload(parcelId);

			Response response = CommonUtils.httpPost(propDetailsAPI, gson.toJson(detailsPayload));
			String strResp = response.readEntity(String.class);
			System.out.println("strResp:" + gson.toJson(strResp));
			responseElement = JsonParser.parseString(strResp);
		}

		System.out.println("prop Details:" + gson.toJson(responseElement));

		return responseElement;
	}

	/**
	 * @param arg
	 */
	public static void main(String arg[]) {

		FLBroward flBroward = new FLBroward();

		// read properties file
		List<String> searchAddressList = CountyUtil
				.getCleanAddressFromFile("property/county/fl/broward/properties.json", true);

		// get parceid
		Map<String, JsonElement> addressParcelMap = flBroward.getParcelByAddress(searchAddressList);
		String folioPath = "$.d.resultListk__BackingField[0].folioNumber";

		for (Map.Entry<String, JsonElement> element : addressParcelMap.entrySet()) {
			System.out.println("address:" + element.getKey());

			try {
				// get zip
				DocumentContext jsonContext = JsonPath.parse(element.getValue().toString());
				String parcelId = jsonContext.read(folioPath);
				System.out.println("parcelId:" + parcelId);

				// Get property details
				JsonElement responseElement = flBroward.getPropertyDetails(parcelId);
				String latestSalePath = "$.d.parcelInfok__BackingField[0].stampAmount1";
				String county = "Broward";
				String category = "DollarGeneral";
				String zipPath = "$.d.parcelInfok__BackingField[0].situsZipCode";
				jsonContext = JsonPath.parse(responseElement.toString());
				String latestSalePrice = jsonContext.read(latestSalePath);

				System.out.println("latestSalePrice:" + latestSalePrice);

				CountyDataBean countyDataBean = new CountyDataBean();
				countyDataBean.setState("FL");
				countyDataBean.setCounty(county);
				countyDataBean.setParcelId(parcelId);
				countyDataBean.setZip(jsonContext.read(zipPath));
				countyDataBean.setData(responseElement);
				countyDataBean.setCategory(category);
				new FLMiamiDade().saveCountyData(countyDataBean);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// break;

		}

		// get parcel details
		// save parcel details

//		String parcelId = "504136010040";

	}

}

@Getter
@Setter
class ParcelLookupPayload {
	// {value: "1801 N Federal Hwy, Hollywood",cities: "",orderBy:
	// "NAME",pageNumber:"1",pageCount:"5000",arrayOfValues:"", selectedFromList:
	// "false",totalCount:"Y"}

	// address
	private String value;
	private String cities = "";
	private String orderBy = "NAME";
	private String pageNumber = "1";
	private String pageCount = "5";
	private String arrayOfValues = "";
	private String selectedFromList = "false";
	private String totalCount = "Y";

	public ParcelLookupPayload(String address) {
		this.value = address;
	}

}

//{folioNumber: "514210540010",taxyear: "2021",action: "CURRENT",use:""}
@Getter
@Setter
class ParcelDetailsPayload {
	private String folioNumber;
	private String taxyear = "2021";
	private String action = "CURRENT";
	private String use = "";

	public ParcelDetailsPayload(String folioNumber) {
		this.folioNumber = folioNumber;
	}

}
