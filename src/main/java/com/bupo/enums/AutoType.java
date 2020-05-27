package com.bupo.enums;

public enum AutoType {

	Car("Car"), Van("Van"), RV("RV"), SUV("SUV"), ATV("ATV"), OTHER("OTHER");

	private final String name;

	private AutoType(String s) {
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
