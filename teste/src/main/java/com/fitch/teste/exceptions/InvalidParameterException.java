package com.fitch.teste.exceptions;

public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidParameterException(String message) {
		super(message);
	}
	
	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
