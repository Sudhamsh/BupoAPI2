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
public class User extends UserStub {
	private ObjectId id;
	private Settings settings;
	private String tenantObjId;
	private String displayName;

}
