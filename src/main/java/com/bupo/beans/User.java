package com.bupo.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private String displayName;
	private String password;
	private JsonObject settings;
	private boolean isPasswordTemp;
	private String email;
	private int shares;

}
