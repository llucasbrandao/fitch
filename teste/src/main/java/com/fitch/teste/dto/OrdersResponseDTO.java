package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fitch.teste.entities.IngredientsEntity;
import com.fitch.teste.entities.UserEntity;

public class OrdersResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private UserEntity user;

	private Double total_due;
	
	private Double discount;
	
	private Set<IngredientsEntity> order_ingredients = new HashSet<>();

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

	public Set<IngredientsEntity> getOrder_ingredients() {
		return order_ingredients;
	}

	public void setOrder_ingredients(Set<IngredientsEntity> order_ingredients) {
		this.order_ingredients = order_ingredients;
	}
}
