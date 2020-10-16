package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrdersDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("ingredients")
	private List<Map<String, Object>> ingredients;
	
	// Lanche pronto
	@JsonProperty("snack")
	private String snack;

	@JsonProperty("snack_qnt")
	private Integer snack_qnt;
	
	public OrdersDTO() {
		super();
	}
	
	public OrdersDTO(List<Map<String, Object>> ingredients, String snack, Integer snack_qnt) {
		super();
		this.ingredients = ingredients;
		this.snack = snack;
		this.snack_qnt = snack_qnt;
	}

	public List<Map<String, Object>> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Map<String, Object>> ingredients) {
		this.ingredients = ingredients;
	}

	public String getSnack() {
		return snack;
	}

	public void setSnack(String snack) {
		this.snack = snack;
	}

	public Integer getSnack_qnt() {
		return snack_qnt;
	}

	public void setSnack_qnt(Integer snack_qnt) {
		this.snack_qnt = snack_qnt;
	}
}
