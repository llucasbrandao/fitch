package com.fitch.teste.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class GenericResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T message;
	private HttpStatus status;
	
	public GenericResponseDTO(T message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
