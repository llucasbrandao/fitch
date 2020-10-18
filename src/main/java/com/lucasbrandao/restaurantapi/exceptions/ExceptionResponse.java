package com.lucasbrandao.restaurantapi.exceptions;

import java.io.Serializable;
import java.util.Date;

import org.springframework.http.HttpStatus;

public class ExceptionResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date timestamp;
	private String message;
	private String details;
	private HttpStatus httpStatus;
	
	public <T> ExceptionResponse(Date timestamp, T message, String details, HttpStatus httpStatus) {
		super();
		this.timestamp = timestamp;
		this.message = (String) message;
		this.details = details;
	}
	
	public ExceptionResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	
	public ExceptionResponse(Date timestamp, String message, String details, HttpStatus httpStatus) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.httpStatus = httpStatus;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
