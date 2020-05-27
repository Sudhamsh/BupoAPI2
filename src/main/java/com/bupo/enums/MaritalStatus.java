package com.bupo.enums;

public enum MaritalStatus {

	Married("Married"), Single("Single"), Widowed("Widowed"), Divorced("Divorced"), NA("N/A");

	private final String name;

	private MaritalStatus(String s) {
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
