package com.reit.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyResultsBean {

	private String name;
	private String status;
	private String externalLink;
	private float remainingTerm;
	private float norRemainingTerm;
	private float cap;
	private float norCap;
	private double weightedScore;
	private float askingPrice;
	private float pricePerSqft;
	private float norPricePerSqft;
	private float rentPerSqft;
	private float norRentPerSqft;

	// zip data fields
	private int zip;
	private float population;
	private float norPopulation;
	private float income;
	private float norIncome;

}
