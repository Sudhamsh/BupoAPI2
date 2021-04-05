package com.reit.county;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;

public interface CountyData {

	public Map<String, JsonElement> getParcelByAddress(List<String> addressList);

	public JsonElement getPropertyDetails(String parcelId);

}
