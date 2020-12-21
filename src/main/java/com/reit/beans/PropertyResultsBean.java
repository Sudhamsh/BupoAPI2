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
	private float remainingTerm;
	private float cap;
	private float weightedScore;
	private float askingPrice;

}
