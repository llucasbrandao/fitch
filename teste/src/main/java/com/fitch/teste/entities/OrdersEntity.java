package com.fitch.teste.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fitch.teste.services.OrdersService;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Ao buscar um pedido, já traz junto o usuário que o fez
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "users")
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@Column(nullable = false)
	private Double total_due;
	
	// Ao buscar um pedido, já traz junto seus respectivos ingredientes
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "orders_ingredients")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id")
	private Set<Integer> order_ingredients = new HashSet<>();
	
	private Double discount;

	public OrdersEntity() {}

	public OrdersEntity(UserEntity user, Double total_due, Set<Integer> order_ingredients, Double discount) {
		super();
		this.user = user;
		this.total_due = total_due;
		this.order_ingredients = order_ingredients;
		this.discount = discount;
	}
	
	public Long getId() {
		return id;
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

	public Set<Integer> getIngredients() {
		return order_ingredients;
	}

	public void setIngredients(Set<Integer> order_ingredients) {
		this.order_ingredients = order_ingredients;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
