package com.reit.beans;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyResultsBean {

	private ObjectId id;
	private String name;
	private Address address;
	private String status;
	private String externalLink;
	private float remainingTerm;
	private float cap;
	private double norCap;
	private double weightedScore;
	private float askingPrice;
	private float pricePerSqft;
	private float rentPerSqft;
	private double norPricePerSqft;
	private double norRentPerSqft;
	private double norRemainingTerm;
	private float noi;

	// zip data fields
	private int zip;
	private double population;
	private double norPopulation;
	private float income;
	private double norIncome;

	private StatusBean statusBean = new StatusBean();
	private List<Long> capsList = new ArrayList<>();

	private double predictedCap;

}
