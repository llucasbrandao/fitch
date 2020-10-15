package com.fitch.teste.entities;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders_ingredients")
public class OrderIngredientsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "order")
	@JoinColumn(name = "order_id")
	private OrdersEntity order;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "ingredients")
	@JoinColumn(name = "ingredient_id")
	private IngredientsEntity ingredient;
	
	private Long quantity;
	
	public OrderIngredientsEntity() {
		super();
	}

	public OrderIngredientsEntity(IngredientsEntity ingredient, OrdersEntity order, Long quantity) {
		this.ingredient = ingredient;
		this.order = order;
		this.quantity = quantity;
	}
	
	public Long getId() {
		return id;
	}

	public OrdersEntity getOrder_id() {
		return order;
	}

	public void setOrder_id(OrdersEntity order) {
		this.order = order;
	}

	public IngredientsEntity getIngredient_id() {
		return ingredient;
	}

	public void setIngredient_id(IngredientsEntity ingredient_id) {
		this.ingredient = ingredient_id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}