package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import com.bupo.enums.EducationLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoDriver extends Person {

	private int yearsLicensed;
	private EducationLevel educationLevel;
	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

}
