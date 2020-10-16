package com.fitch.teste.controllers;

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

import com.fitch.teste.dto.GenericResponseDTO;
import com.fitch.teste.dto.UserDTO;
import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.services.UserService;

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
	public ResponseEntity<GenericResponseDTO<?>> newUser(@Valid @RequestBody UserDTO payload) {
		return new ResponseEntity<>(new GenericResponseDTO<String>("User created successfully. ID: " + userService.saveUser(UserService.fromDTO(payload)), 
				HttpStatus.CREATED), HttpStatus.CREATED);
	}
	
	@GetMapping("/getByID")
	public ResponseEntity<Optional<UserEntity>> getByID(@RequestParam("id") Long id) {
		return new ResponseEntity<>(userService.findUserByID(id), HttpStatus.OK);
	}
	
	@GetMapping("/getByEmail")
	public ResponseEntity<UserEntity> getByEmail(@RequestParam("email") String email) {
		return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GenericResponseDTO<?>> deleteUser(@PathVariable("id") Long id) {
		
		return new ResponseEntity<>(new GenericResponseDTO<String>(Boolean.toString(userService.delete(id)), HttpStatus.OK), HttpStatus.OK);
	}
}
