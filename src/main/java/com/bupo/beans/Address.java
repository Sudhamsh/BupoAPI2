package com.bupo.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

	private String firstLine;
	private String aptNo;
	private String city;
	private String state;
	private String zip;

}
