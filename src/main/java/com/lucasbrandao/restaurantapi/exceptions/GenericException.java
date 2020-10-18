package com.lucasbrandao.restaurantapi.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class GenericException extends ExceptionResponse {
	
	private static final long serialVersionUID = 1L;
	
	public <T> GenericException(Date timestamp, T message, String details, HttpStatus httpStatus) {
		super(timestamp, message, details, httpStatus);
	}
}
