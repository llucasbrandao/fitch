package com.lucasbrandao.restaurantapi.controllers;

import java.security.InvalidParameterException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucasbrandao.restaurantapi.dto.GenericResponseDTO;
import com.lucasbrandao.restaurantapi.dto.IngredientsDTO;
import com.lucasbrandao.restaurantapi.entities.IngredientsEntity;
import com.lucasbrandao.restaurantapi.services.IngredientsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientsController {
	
	@Autowired
	private final IngredientsService ingredientsService;
	
	public IngredientsController(IngredientsService ingredientsService) {
		this.ingredientsService = ingredientsService;
	}
	
	@PostMapping("/new")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Create a new ingredient", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> newIngredient(@Valid @RequestBody IngredientsDTO ingredientsDTO) {
		return new ResponseEntity<>(new GenericResponseDTO<String>("Ingredient added successfully. ID: " + ingredientsService.newIngredient(IngredientsService.fromDTO(ingredientsDTO)), 
				HttpStatus.CREATED), HttpStatus.CREATED);
	}
	
	@GetMapping("/getAll")
	@ApiOperation(value = "Lists all available ingredients", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> getAll() {
		return new ResponseEntity<>(new GenericResponseDTO<List<IngredientsEntity>>(ingredientsService.findAll(), 
				HttpStatus.OK), HttpStatus.OK);
	}
	
	@GetMapping("/getByID")
	@ApiOperation(value = "Get ingredient by ID", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> getByID(@RequestParam Long id) {
		if (id == null)
			throw new InvalidParameterException("Parameter 'id' is required");
		
		return new ResponseEntity<>(new GenericResponseDTO<IngredientsEntity>(ingredientsService.findByID(id).get(), 
				HttpStatus.OK), HttpStatus.OK);
	}
	
	@PatchMapping("/edit/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Edit an ingredients", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> editIngredient(@PathVariable Long id, @RequestParam Double price) {
		if (price == null)
			throw new InvalidParameterException("Parameter 'price' is required");
		
		ingredientsService.updatePrice(id, price);
		
		return new ResponseEntity<>(new GenericResponseDTO<String>("Ingredient price edited successfully", 
				HttpStatus.CREATED), HttpStatus.OK);
	}
	
	@PatchMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Delete an ingredient", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> deleteIngredient(@PathVariable Long id) {
		if (id == null)
			throw new InvalidParameterException("Parameter 'id' is required");
		
		ingredientsService.delete(id);
		
		return new ResponseEntity<>(new GenericResponseDTO<String>("Ingredient removed successfully", 
				HttpStatus.CREATED), HttpStatus.OK);
	}
}
