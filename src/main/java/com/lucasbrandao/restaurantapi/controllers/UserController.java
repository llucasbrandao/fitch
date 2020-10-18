package com.lucasbrandao.restaurantapi.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucasbrandao.restaurantapi.dto.GenericResponseDTO;
import com.lucasbrandao.restaurantapi.dto.UserDTO;
import com.lucasbrandao.restaurantapi.entities.UserEntity;
import com.lucasbrandao.restaurantapi.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api/v1/users")
class UserController {
	
	/**
	 * Ao utilizar os roles para autorizar acesso aos endpoints, o nome do role não ter o prefixo ROLE_, ao contrário do que é definido 
	 * no Spring Security. Aqui, usar apenas o nome do role. 
	 */
	@Autowired
	private final UserService userService;
	
	UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/new")
	@ApiOperation(value = "Create a new user", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> newUser(@Valid @RequestBody UserDTO payload) {
		return new ResponseEntity<>(new GenericResponseDTO<String>("User created successfully. ID: " + userService.saveUser(UserService.fromDTO(payload)), 
				HttpStatus.CREATED), HttpStatus.CREATED);
	}
	
	@GetMapping("/getByID")
	@ApiOperation(value = "Get user by ID", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<Optional<UserEntity>> getByID(@RequestParam("id") Long id) {
		return new ResponseEntity<>(userService.findUserByID(id), HttpStatus.OK);
	}
	
	@GetMapping("/getByEmail")
	@ApiOperation(value = "Get user by email", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<UserEntity> getByEmail(@RequestParam("email") String email) {
		return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@ApiOperation(value = "Remove an user", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<GenericResponseDTO<?>> deleteUser(@PathVariable("id") Long id) {
		
		return new ResponseEntity<>(new GenericResponseDTO<String>(Boolean.toString(userService.delete(id)), HttpStatus.OK), HttpStatus.OK);
	}
}
