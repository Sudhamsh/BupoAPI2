package com.bupo.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bupo.enums.HomeOwnership;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.reit.beans.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class AutoPolicy {

	private HomeOwnership homeOwnership;
	// policy details
	private String policyNumber;
	private Date policyStartDate;
	private Date policyEndDate;
	private int monthlyPremium;
	private int sixMonthPremium;
	// find provider
	// find insured
	// find insured address
	// find policy details
	// find vehicles details

	// provider details
	private String presentProviderName;
	private Address providerAddress;
	private String presentProviderPhone;
	private String presentProviderEmail;

	// policy holder details
	private Address insuredAddress;
	private List<Vehicle> automobileList = new ArrayList<Vehicle>();

	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

	// drivers
	private List<AutoDriver> autoDrivers = new ArrayList<AutoDriver>();

	private LeinHolder leinHolder;

	private String fileName; // TODO: doesn't fit in this class, returning this value to UI will be a
								// security risk #SECURITY

}
