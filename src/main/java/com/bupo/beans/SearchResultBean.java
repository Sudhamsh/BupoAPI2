package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultBean<T> {
	private String message;
	private String errorMessage;
	private int errorCode;
	private int count;
	private int pageNo = 1;
	private boolean hasMore = false;
	private List<T> results = new ArrayList<T>();
	private JsonObject searchParams = new JsonObject();

	public void setSearchParam(String name, String value) {
		searchParams.addProperty(name, value);

	}

	public void setSearchParam(String name, int value) {
		searchParams.addProperty(name, value);

	}

}
