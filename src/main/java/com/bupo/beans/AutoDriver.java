package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AutoDriver extends Person {

	private int yearsLicensed;
	// discounts
	private List<Discount> discounts = new ArrayList<Discount>();

}
