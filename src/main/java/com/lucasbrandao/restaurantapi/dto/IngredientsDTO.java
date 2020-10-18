package com.lucasbrandao.restaurantapi.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class IngredientsDTO {
	
	@NotBlank(message = "name must not be empty")
	@Length(min =  2, max = 30, message = "name must have at least 2 characters and 30 max ")
	private String name;
	
	@NotNull(message = "available_quantity must not be empty")
	@Min(0)
	private Long available_quantity;
	
	@NotNull(message = "price must not be empty")
	@DecimalMin("0.00")
	private Double price;
	
	public IngredientsDTO() {}

	public IngredientsDTO(
			@NotBlank(message = "name must not be empty") @Length(min = 2, max = 30, message = "name must have at least 2 characters and 30 max ") String name,
			@NotEmpty(message = "available_quantity must not be empty") @Min(0) Long available_quantity,
			@NotNull(message = "price must not be empty") @DecimalMin("0.00") Double price) {
		super();
		this.name = name;
		this.available_quantity = available_quantity;
		this.price = price;
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
