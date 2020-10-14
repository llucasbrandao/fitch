package com.fitch.teste.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitch.teste.dto.UserDTO;
import com.fitch.teste.entities.GreetingEntity;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.respositories.GreetingRepository;
import com.fitch.teste.services.UserService;

@RestController
@RequestMapping("/api/v1")
class Controllers {
	
	/**
	 * Ao utilizar os roles para autorizar acesso aos endpoints, o nome do role não ter o prefixo ROLE_, ao contrário do que é definido 
	 * no Spring Security. Aqui, usar apenas o nome do role. 
	 */
	@Autowired
	private final GreetingRepository repository;
	private final UserService userService;
	
	Controllers(GreetingRepository repository, UserService userService) {
		this.repository = repository;
		this.userService = userService;
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/welcome")
	public ResponseEntity<GreetingEntity> greeting(@RequestParam("id") Long id) throws Exception {
		Optional<GreetingEntity> gte = repository.findById(id);
		
		if (!gte.isPresent()) {
			throw new NotFoundException("Value not found");
			
		}
		
		return new ResponseEntity<GreetingEntity>(gte.get(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/newTest")
	public boolean newTest(@RequestBody GreetingEntity payload) {
		System.out.println(payload.getName());
		
		repository.save(payload);
		
		return true;
	}
	
	@PostMapping("/users/new")
	public boolean newUser(@Valid @RequestBody UserDTO payload) {
		userService.saveUser(UserService.fromDTO(payload));
		
		return true;
	}
}
