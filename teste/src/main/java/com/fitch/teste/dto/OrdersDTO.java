package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class OrdersDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private Set<Integer> ingredients;

	public OrdersDTO(@NotBlank Set<Integer> ingredients) {
		super();
		this.ingredients = ingredients;
	}

	public Set<Integer> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Integer> ingredients) {
		this.ingredients = ingredients;
	}
}
