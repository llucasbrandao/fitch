/*package com.fitch.teste.entities;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders_ingredients")
public class OrderIngredientsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "ingredients")
	@JoinColumn(name = "id")
	private Long ingredient_id;
	
	public OrderIngredientsEntity() {}
	
	public OrderIngredientsEntity(Long ingredient_id) {
		super();
		this.ingredient_id = ingredient_id;
	}

	public Long getID() {
		return this.id;
	}

	public Long getIngredient_id() {
		return ingredient_id;
	}

	public void setIngredient_id(Long ingredient_id) {
		this.ingredient_id = ingredient_id;
	}
}*/
