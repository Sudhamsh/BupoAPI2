package com.bupo.beans;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestBean {
	private String displayName;
	private String password;
	private JsonObject settings;
	private String email;
	private int shares;

}
