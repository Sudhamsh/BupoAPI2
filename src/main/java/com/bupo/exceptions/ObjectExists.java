package com.bupo.exceptions;

public class ObjectExists extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectExists(String errorMessage) {
		super(errorMessage);
	}

}
