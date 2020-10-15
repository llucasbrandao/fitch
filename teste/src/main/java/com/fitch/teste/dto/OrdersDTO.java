package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class OrdersDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private Set<Integer> ingredients;
	
	public OrdersDTO() {
		super();
	}
	
	public OrdersDTO(@NotEmpty Set<Integer> ingredients) {
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
