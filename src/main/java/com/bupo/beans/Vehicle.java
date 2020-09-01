package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle {

	private String make;
	private String model;
	private int year;
	private int yearlyMileage;
	private int presentMileage;
	private String vin;
	private List<String> usage = new ArrayList<String>(); // Commute,leisure,
	private List<Coverage> coverages = new ArrayList<Coverage>();
	private String type;

	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

}
