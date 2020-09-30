package com.bupo.enums;

public enum PolicyStatus {

	Open("Open"), Gathering_Quotes("Gathering_Quotes"), Customer_Considering("Customer_Considering"),
	Customer_Rejected("Customer_Rejected"), Closed("Closed");

	private final String name;

	private PolicyStatus(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		// (otherName == null) check is not needed because name.equals(null) returns
		// false
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}

}
