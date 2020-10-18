package com.fitch.teste.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitch.teste.entities.IngredientsEntity;
import com.fitch.teste.entities.SnacksEntity;

@Service
public class SnacksService {
	
	@Autowired
	private IngredientsService ingredientsService;
	
	/*
	 * Método que cria os dummy snacks.
	 */
	public List<SnacksEntity> generateDummySnacks() {
		SnacksEntity snacksEntity;
		
		List<SnacksEntity> dummySnacksEntities = new ArrayList<>();
		List<IngredientsEntity> ingredientsEntities = new ArrayList<>();
		
		/*
		 * Gera X-Bacon: Bacon, hambúrguer de carne e queijo
		 */
		ingredientsEntities.add(ingredientsService.findByName("Bacon"));
		ingredientsEntities.add(ingredientsService.findByName("Hambúrguer"));
		ingredientsEntities.add(ingredientsService.findByName("Queijo"));
		
		snacksEntity = new SnacksEntity("X-Bacon", ingredientsEntities);
		
		dummySnacksEntities.add(snacksEntity);
		
		// FIM X-Bacon
		
		/**
		 * Gera X-Burger: Hambúrguer de carne e queijo
		 */
		
		ingredientsEntities = new ArrayList<>();
		
		ingredientsEntities.add(ingredientsService.findByName("Hambúrguer"));
		ingredientsEntities.add(ingredientsService.findByName("Queijo"));
		
		snacksEntity = new SnacksEntity("X-Burger", ingredientsEntities);
		
		dummySnacksEntities.add(snacksEntity);
		
		// FIM X-Burger
		
		/*
		 * Gera X-Egg: Ovo, hambúrguer de carne e queijo
		 */
		
		ingredientsEntities = new ArrayList<>();
		
		ingredientsEntities.add(ingredientsService.findByName("Ovo"));
		ingredientsEntities.add(ingredientsService.findByName("Hambúrguer"));
		ingredientsEntities.add(ingredientsService.findByName("Queijo"));
		
		snacksEntity = new SnacksEntity("X-Egg", ingredientsEntities);
		
		dummySnacksEntities.add(snacksEntity);
		
		// FIM X-Egg
		
		/*
		 * Gera X-Egg Bacon: Ovo, bacon, hambúrguer de carne e queijo
		 */
		
		ingredientsEntities = new ArrayList<>();
		
		ingredientsEntities.add(ingredientsService.findByName("Ovo"));
		ingredientsEntities.add(ingredientsService.findByName("Hambúrguer"));
		ingredientsEntities.add(ingredientsService.findByName("Queijo"));
		ingredientsEntities.add(ingredientsService.findByName("Bacon"));
		
		snacksEntity = new SnacksEntity("X-Egg Bacon", ingredientsEntities);
		
		dummySnacksEntities.add(snacksEntity);
		
		// FIM X-Egg Bacon
		
		return dummySnacksEntities;
	}
}
