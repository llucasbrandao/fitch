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
	
	private String snack;
	
	private Integer snack_qnt;
	
	private Long quantity;
	
	public OrderIngredientsEntity() {
		super();
	}

	public OrderIngredientsEntity(IngredientsEntity ingredient, OrdersEntity order, Long quantity) {
		this.ingredient = ingredient;
		this.order = order;
		this.quantity = quantity;
	}
	
	public OrderIngredientsEntity(String snack, Integer snack_qnt, OrdersEntity order, Long quantity) {
		this.snack = snack;
		this.snack_qnt = snack_qnt;
		this.order = order;
		this.quantity = quantity;
	}
	
	public Long getId() {
		return id;
	}

	public OrdersEntity getOrder() {
		return order;
	}

	public void setOrder(OrdersEntity order) {
		this.order = order;
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

	public IngredientsEntity getIngredient() {
		return ingredient;
	}

	public void setIngredient(IngredientsEntity ingredient_id) {
		this.ingredient = ingredient_id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}