package com.reit.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeBean {

	@CsvBindByName(column = "NAME")
	private int zip;

	@CsvBindByName(column = "GEO_ID")
	private String geoId;

	// Estimate!!Median household income in the past 12 months
	@CsvBindByName(column = "B19013_001E")
	private int medianIncome;

	// Margin of Error!!Median household income in the past 12 months
	@CsvBindByName(column = "B19013_001M")
	private int marginOfError;

	// Annotation of Estimate!!Median household income in the past 12 months (
	@CsvBindByName(column = "B19013_001EA")
	private String annotationEstimate;

	// Annotation of Margin of Error!!Median household income in the past 12 months
	@CsvBindByName(column = "B19013_001MA")
	private String annotationMarginEstimate;

	private int year;

}
