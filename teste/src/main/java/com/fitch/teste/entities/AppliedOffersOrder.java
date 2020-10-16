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
@Table(name = "applied_offers")
public class AppliedOffersOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "order")
	@JoinColumn(name = "order_id")
	private OrdersEntity order;
	
	private String name;

	public AppliedOffersOrder(OrdersEntity order, String name) {
		super();
		this.order = order;
		this.name = name;
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
}
