package com.bupo.beans;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reit.beans.Settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private ObjectId objectId;
	private String displayName;
	private String password;
	private boolean isPasswordTemp;
	private String email;
	private int shares;
	private Settings settings;

}
