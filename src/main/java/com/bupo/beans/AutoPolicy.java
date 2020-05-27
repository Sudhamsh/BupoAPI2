package com.bupo.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AutoPolicy {

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
	private List<Automobile> automobileList = new ArrayList<Automobile>();

	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

	// drivers
	private List<AutoDriver> autoDrivers = new ArrayList<AutoDriver>();

	private LeinHolder leinHolder;

}
