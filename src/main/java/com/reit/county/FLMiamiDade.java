package com.reit.county;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
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
import com.reit.util.JsonUtil;

public class FLMiamiDade implements CountyData {
	private static boolean isTesting = false;
	private static Gson gson = GsonUtils.getGson();
	private LogManager logger = LogManager.getLogger(this.getClass());

	private static final String parcelLookupAPI = "https://www.miamidade.gov/Apps/PA/PApublicServiceProxy/PaServicesProxy.ashx?Operation=GetAddress&clientAppName=PropertySearch&from=1&myAddress=%s&myUnit=&to=200";

	private static final String propDetailsAPI = "https://www.miamidade.gov/Apps/PA/PApublicServiceProxy/PaServicesProxy.ashx?Operation=GetPropertySearchByFolio&clientAppName=PropertySearch&folioNumber=%s";

	public Map<String, JsonElement> getParcelByAddress(List<String> addressList) {
		Preconditions.checkNotNull(addressList, "Address List is null");
		Preconditions.checkArgument(addressList.size() > 0, "Address List is empty");

		Map<String, JsonElement> addressParcelMap = new HashMap<>();

		for (String address : addressList) {
			String url = String.format(parcelLookupAPI, formatAddress(address));
			JsonElement responseElement = null;
			if (isTesting) {
				responseElement = CommonUtils.readJsonData("data/api/samples/fl/miamidade/parcelLookup.json");
			} else {
				Response response = JsonUtil.getJsonResponse(url);
				String strResp = response.readEntity(String.class);
				responseElement = JsonParser.parseString(strResp);
			}

			System.out.println(gson.toJson(responseElement));

			addressParcelMap.put(address, responseElement);

			break;
		}

		return addressParcelMap;

	}

	public JsonElement getPropertyDetails(String parcelId) {
		JsonElement responseElement = null;

		if (isTesting) {
			responseElement = CommonUtils.readJsonData("data/api/samples/fl/miamidade/parcelDetails.json");

		} else {
			String url = String.format(propDetailsAPI, formatParcel(parcelId));
			Response response = JsonUtil.getJsonResponse(url);
			String strResp = response.readEntity(String.class);
			responseElement = JsonParser.parseString(strResp);
		}

		System.out.println("details:" + gson.toJson(responseElement));

		return responseElement;
	}

	public String formatParcel(String address) {
		String addressParam = address.trim().replaceAll("-", "");

		return addressParam;
	}

	public String formatAddress(String address) {
		String addressParam = address.trim().replace(' ', '+');

		return addressParam;
	}

	public static void main(String[] args) {
		FLMiamiDade flMiamiDade = new FLMiamiDade();

		// read properties file
		List<String> searchAddressList = CountyUtil
				.getCleanAddressFromFile("property/county/fl/miamidade/properties.json", false);

		// get parceid
		Map<String, JsonElement> addressParcelMap = flMiamiDade.getParcelByAddress(searchAddressList);
		String folioPath = "$.MinimumPropertyInfos[0].Strap";

		for (Map.Entry<String, JsonElement> element : addressParcelMap.entrySet()) {
			System.out.println("address:" + element.getKey());

			try {
				// get zip
				DocumentContext jsonContext = JsonPath.parse(element.getValue().toString());
				String parcelId = jsonContext.read(folioPath);
				System.out.println("parcelId:" + parcelId);

				String latestSalePath = "$.SalesInfos[0].SalePrice";
				String county = "MiamiDade";
				String category = "DollarGeneral";
				String zipPath = "$.SiteAddress[0].Zip";

				JsonElement responseElement = flMiamiDade.getPropertyDetails(parcelId);

				jsonContext = JsonPath.parse(responseElement.toString());
				Integer latestSalePrice = jsonContext.read(latestSalePath);

				System.out.println("latestSalePrice:" + latestSalePrice);

				CountyDataBean countyDataBean = new CountyDataBean();
				countyDataBean.setState("FL");
				countyDataBean.setCounty(county);
				countyDataBean.setParcelId(parcelId);
				countyDataBean.setZip(jsonContext.read(zipPath));
				countyDataBean.setData(responseElement);
				countyDataBean.setCategory(category);
				flMiamiDade.saveCountyData(countyDataBean);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public ObjectId saveCountyData(CountyDataBean countyDataBean) {

		Preconditions.checkNotNull(countyDataBean, "County Data is null");
		Preconditions.checkNotNull(countyDataBean.getData(), "Data in bean is null");
		Preconditions.checkNotNull(countyDataBean.getParcelId(), "Parcel Id is null");

		MongoDao mongoDao = new MongoDao();
		ObjectId objectId = new ObjectId();
		try {
			// TODO - Check if property exists before create.
			objectId = mongoDao.insert(MongoCollEnum.CountyData.toString(), gson.toJson(countyDataBean));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		return objectId;

	}

}
