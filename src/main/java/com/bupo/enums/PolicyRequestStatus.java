package com.bupo.enums;

public enum PolicyRequestStatus {

	NEW("New"), CLOSED("CLOSED"), QUOTES_IN_PROCESS("Quotes In Process");

	private final String name;

	private PolicyRequestStatus(String s) {
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
