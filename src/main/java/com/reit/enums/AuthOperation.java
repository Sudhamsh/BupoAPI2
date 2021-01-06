package com.reit.enums;

public enum AuthOperation {

	Create("create"), Read("read"), Update("update"), Delete("delete");

	private final String name;

	private AuthOperation(String s) {
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
