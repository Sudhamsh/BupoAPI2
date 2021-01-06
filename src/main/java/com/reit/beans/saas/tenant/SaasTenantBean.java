package com.reit.beans.saas.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaasTenantBean {
	private String _id;
	private String tenantName;
	private User orgPrimaryContact;

}
