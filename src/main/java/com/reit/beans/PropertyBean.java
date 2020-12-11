package com.reit.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyBean {
	private Address address;
	private int numberOfTenants = 1;
	private TenantDetails tenantDetails;

	@CsvBindByName(column = "Property Name")
	private String propertyName;
	private String propertyStatus;
	private String type;
	private String leaseTerm;
	private String remainingTerm;
	private String units;

	private int noi;
	private int cap;
	private int sqFt;
	private int askingPrice;
	private int pricePerSqft;
	private int rentPerSqft;
	private boolean opportunityZone;

}
