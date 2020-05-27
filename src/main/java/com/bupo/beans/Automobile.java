package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Automobile {

	private String make;
	private String model;
	private int year;
	private int yearlyMileage;
	private int presentMileage;
	private String vin;
	private List<String> usage; // Commute,leisure,
	private List<Coverage> coverages;
	private String type;

	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

}
