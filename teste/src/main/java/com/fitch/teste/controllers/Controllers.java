package com.fitch.teste.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitch.teste.entities.GreetingEntity;
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
	public GreetingEntity greeting(@RequestParam("id") Long id) {
		return repository.getOne(id);
			
	}
}
