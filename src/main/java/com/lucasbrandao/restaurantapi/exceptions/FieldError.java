package com.lucasbrandao.restaurantapi.exceptions;

import java.io.Serializable;

/*
 * Classe auxiliar para gerar um erro do tipo key => value.
 */
public class FieldError implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String field;
	private String message;
	
	public FieldError(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
