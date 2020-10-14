package com.fitch.teste.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitch.teste.dto.GenericResponseDTO;
import com.fitch.teste.dto.OrdersDTO;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {
	
	@PostMapping("/new")
	public ResponseEntity<GenericResponseDTO> newOrder(@Valid @RequestBody OrdersDTO order) {
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
}
