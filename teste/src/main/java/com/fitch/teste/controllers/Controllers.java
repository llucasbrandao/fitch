package com.fitch.teste.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitch.teste.entities.GreetingEntity;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.respositories.GreetingRepository;

@RestController
@RequestMapping("/api/v1")
class Controllers {
	
	@Autowired
	private final GreetingRepository repository;
	
	Controllers(GreetingRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/welcome")
	public ResponseEntity<GreetingEntity> greeting(@RequestParam("id") Long id) throws Exception {
		Optional<GreetingEntity> gte = repository.findById(id);
		
		if (!gte.isPresent()) {
			throw new NotFoundException("Value not found");
			
		}
		
		return new ResponseEntity<GreetingEntity>(gte.get(), HttpStatus.OK);
	}
	
	@PostMapping("/newTest")
	public boolean newTest(@RequestBody GreetingEntity payload) {
		System.out.println(payload.getName());
		
		repository.save(payload);
		
		return true;
	}
}
