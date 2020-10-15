package com.fitch.teste.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ValidationFieldError extends ExceptionResponse {
	
	private static final long serialVersionUID = 1L;
	private List<FieldError> list = new ArrayList<>();
	
	public ValidationFieldError(Date timestamp, String message, String details) {
		super(timestamp, message, details, HttpStatus.BAD_REQUEST);
	}

	public List<FieldError> getErrors() {
		return list;
	}

	public void addError(String field, String message) {
		this.list.add(new FieldError(field, message));
	}
}
