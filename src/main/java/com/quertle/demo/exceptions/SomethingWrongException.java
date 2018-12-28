package com.quertle.demo.exceptions;

public class SomethingWrongException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1121637869723355794L;

	public SomethingWrongException(String message, String errorMessage) {
		super(message);
	}
}
