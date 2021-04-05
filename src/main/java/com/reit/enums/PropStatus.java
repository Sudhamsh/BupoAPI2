package com.reit.enums;

public enum PropStatus {

	SHORTLISTED("SHORTLISTED"), LOI_SUBMITTED("LOI SUBMITTED"), LOI_ACCEPTED("LOI ACCEPTED"),
	PSA_RECEIVED("PSA RECEIVED"), PSA_SIGNED("PSA SIGNED"), INSPECTION_PERIOD("INSPECTION PERIOD"),
	INSPECTION_COMPLETED("INSPECTION COMPLETED"), FINANCE_APPROVED("FINANCE APPROVED"), CLOSED("CLOSED"),
	IGNORE("IGNORE");

	private final String name;

	private PropStatus(String s) {
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
