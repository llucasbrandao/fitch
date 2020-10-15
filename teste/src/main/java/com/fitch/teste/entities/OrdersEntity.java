package com.fitch.teste.entities;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Ao buscar um pedido, já traz junto o usuário que o fez
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "users")
	private UserEntity user;
	
	@Column(nullable = false)
	private Double total_due;
	
	/*// Ao buscar um pedido, já traz junto seus respectivos ingredientes
	@Column(nullable = false)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ingredients")
	private Set<Integer> ingredients;*/
	
	private Double discount;

	public OrdersEntity() {}

	public OrdersEntity(UserEntity user, Double total_due, Set<Integer> ingredients, Double discount) {
		super();
		this.user = user;
		this.total_due = total_due;
		//this.ingredients = ingredients;
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

	/*public Set<Integer> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Integer> ingredients) {
		this.ingredients = ingredients;
	}*/

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
