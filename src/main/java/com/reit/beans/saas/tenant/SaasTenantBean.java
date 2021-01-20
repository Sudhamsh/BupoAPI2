package com.reit.beans.saas.tenant;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaasTenantBean {
	private ObjectId id;
	private String tenantName;
	private UserContact orgPrimaryContact;

}
