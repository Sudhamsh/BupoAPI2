package com.bupo.enums;

public enum EducationLevel {

	HIGH_SCHOOL("High School"), ASSOCIATE_DEGREE("Associate Degree"), BACHELORS_DEGREE("Bachelor's Degree"),
	MASTERS_DEGREE("Master's Degree"), DOCTORAL_DEGREE("Doctoral degree"), UNKNOWN("UNKNOWN");

	private final String name;

	private EducationLevel(String s) {
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
