package com.bupo.exceptions;

public class EntityNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
