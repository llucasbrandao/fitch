package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fitch.teste.entities.UserEntity;

public class OrdersResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private UserEntity user;

	private Double total_due;
	
	private Double discount;
	
	private List<Map<String, String>> order_ingredients = new ArrayList<>();

	public OrdersResponseDTO(Long id, UserEntity user, Double total_due, Double discount) {
		super();
		this.id = id;
		this.user = user;
		this.total_due = total_due;
		this.discount = discount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Double getTotal_due() {
		return total_due;
	}

	public void setTotal_due(Double total_due) {
		this.total_due = total_due;
	}
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public List<Map<String, String>> getOrder_ingredients() {
		return order_ingredients;
	}

	public void setOrder_ingredients(List<Map<String, String>> order_ingredients) {
		this.order_ingredients = order_ingredients;
	}
}
