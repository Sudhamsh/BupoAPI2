package com.reit.beans;

import com.google.gson.JsonElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountyDataBean {
	private String state;
	private String county;
	private String parcelId;
	private String category;
	private String zip;
	private JsonElement data;

}
