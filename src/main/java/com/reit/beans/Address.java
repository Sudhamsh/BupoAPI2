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
public class Address {
	private String fullAddress;

	@CsvBindByName(column = "Address")
	private String firstLine;
	private String aptNo;

	@CsvBindByName(column = "City")
	private String city;

	private String county;

	@CsvBindByName(column = "State")
	private String state;

	@CsvBindByName(column = "Zip")
	private String zip;

}
