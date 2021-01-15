package com.reit.beans;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBean {
	private int errorCode;
	private String errorMsg;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}
