package com.bupo.enums;

public enum InfoEntryType {

	UPLOAD("UPLOAD"), MANUAL("MANUAL");

	private final String name;

	private InfoEntryType(String s) {
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
