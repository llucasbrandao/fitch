package com.lucasbrandao.restaurantapi.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(value = {Throwable.class})
	public final ResponseEntity<ExceptionResponse> handleGenericException(Throwable ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = {NotFoundException.class})
	public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = {UserAlreadyExistsException.class})
	public final ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.CONFLICT);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(JsonMappingException.class)
	public final ResponseEntity<ExceptionResponse> handleJsonMappingException(JsonMappingException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public final ResponseEntity<ExceptionResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		ValidationFieldError validationFieldError = new ValidationFieldError(new Date(), "Validation Errors", 
				request.getDescription(false));
		
		// Para melhor visualização do erro, adicionamos cada um com o par key => message
		for (FieldError x : ex.getBindingResult().getFieldErrors())
			validationFieldError.addError(x.getField(), x.getDefaultMessage());
		
		return new ResponseEntity<>(validationFieldError, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {HttpMessageNotReadableException.class})
	public final ResponseEntity<ExceptionResponse> handleArgumentNotValidException(HttpMessageNotReadableException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {AccessDeniedException.class})
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.FORBIDDEN);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = {AuthorizationException.class})
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(AuthorizationException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.UNAUTHORIZED);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = {InvalidParameterException.class})
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(InvalidParameterException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.METHOD_NOT_ALLOWED);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(value = {MissingServletRequestParameterException.class})
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(MissingServletRequestParameterException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false), HttpStatus.METHOD_NOT_ALLOWED);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
