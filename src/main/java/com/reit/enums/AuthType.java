package com.reit.enums;

public enum AuthType {

	APP("App"), GOOGLE("Google");

	private final String name;

	private AuthType(String s) {
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
