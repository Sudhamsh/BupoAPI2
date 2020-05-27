package com.bupo.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {

	private String firstLine;
	private String secondLine;
	private String city;
	private String state;
	private String zip;

}
