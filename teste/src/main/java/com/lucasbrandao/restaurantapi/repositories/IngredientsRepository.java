package com.fitch.teste.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitch.teste.entities.IngredientsEntity;

public interface IngredientsRepository extends JpaRepository<IngredientsEntity, Long> {
	
	@Transactional
	IngredientsEntity findByName(String name);
}
