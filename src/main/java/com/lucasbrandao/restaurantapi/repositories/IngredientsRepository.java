package com.lucasbrandao.restaurantapi.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasbrandao.restaurantapi.entities.IngredientsEntity;

public interface IngredientsRepository extends JpaRepository<IngredientsEntity, Long> {
	
	@Transactional
	IngredientsEntity findByName(String name);
}
