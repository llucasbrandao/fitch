package com.fitch.teste.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ingredients")
public class IngredientsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Long available_quantity;
	
	@Column(nullable = false)
	private Double price;
	
	public IngredientsEntity() {}
	
	public IngredientsEntity(String name, Long available_quantity, Double price) {
		super();
		this.name = name;
		this.available_quantity = available_quantity;
		this.price = price;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAvailable_quantity() {
		return available_quantity;
	}

	public void setAvailable_quantity(Long available_quantity) {
		this.available_quantity = available_quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
