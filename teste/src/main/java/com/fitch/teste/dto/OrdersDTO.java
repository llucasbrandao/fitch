package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrdersDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	@JsonProperty("ingredients")
	private List<Map<String, Object>> ingredients;
	
	public OrdersDTO() {
		super();
	}
	
	public OrdersDTO(@NotEmpty List<Map<String, Object>> ingredients) {
		super();
		this.ingredients = ingredients;
	}

	public List<Map<String, Object>> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Map<String, Object>> ingredients) {
		this.ingredients = ingredients;
	}
	
	/*@SuppressWarnings(value = {"unchecked"})
	@JsonProperty("ingredients")
	private void unpackNestedJSON(Map<String, Object> jsonMap) {
		for (Map.Entry<String, Object> entry : jsonMap.entrySet())
			System.out.println(entry.getKey() + " => " + entry.getValue());
	}*/
}
