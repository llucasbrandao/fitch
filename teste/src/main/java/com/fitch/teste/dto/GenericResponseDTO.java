package com.fitch.teste.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * Esta classe foi criada para padronizar as respostas HTTP.
 * 
 * @author Lucas Brandão
 *
 * @param <T>
 */
public class GenericResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T message; // Para maior conveniência e flexibilidade, usamos o tipo genérico na mensagem
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
