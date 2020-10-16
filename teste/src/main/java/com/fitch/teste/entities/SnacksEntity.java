package com.fitch.teste.entities;

import java.util.List;

/*
 * Dummy class para representar os lanches.
 */
public class SnacksEntity {
	
	private Long id;
	
	private String name;
	
	private List<IngredientsEntity> ingredients;

	public SnacksEntity(String name, List<IngredientsEntity> ingredients) {
		super();
		this.name = name;
		this.ingredients = ingredients;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IngredientsEntity> getIngredients() {
		return ingredients;
	}

	public void setIngredient(List<IngredientsEntity> ingredients) {
		this.ingredients = ingredients;
	}
}
