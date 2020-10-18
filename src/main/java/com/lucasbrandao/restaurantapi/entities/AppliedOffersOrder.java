package com.lucasbrandao.restaurantapi.entities;

import javax.persistence.CascadeType;
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
@Table(name = "applied_offers")
public class AppliedOffersOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@CollectionTable(name = "order")
	@JoinColumn(name = "order_id")
	private OrdersEntity order;
	
	private String name;
	
	private String description;

	public AppliedOffersOrder() {}

	public AppliedOffersOrder(OrdersEntity order, String name, String description) {
		super();
		this.order = order;
		this.name = name;
		this.description = description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
