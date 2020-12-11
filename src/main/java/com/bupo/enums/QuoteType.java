package com.bupo.enums;

public enum QuoteType {

	Minimum("Minimum"), Recommended("Recommended"), Expensive("Expensive");

	private final String name;

	private QuoteType(String s) {
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
