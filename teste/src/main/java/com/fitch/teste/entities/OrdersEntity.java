package com.fitch.teste.entities;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// Ao buscar um pedido, já traz junto o usuário que o fez
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "users")
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@Column(nullable = false)
	private Double total_due;
	
	private Double discount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@CollectionTable(name = "applied_offers")
	@JoinColumn(name = "offer_id")
	private AppliedOffersOrder aplAppliedOffersOrder;

	public OrdersEntity() {}

	public OrdersEntity(UserEntity user, Double total_due, Double discount) {
		super();
		this.user = user;
		this.total_due = total_due;
		this.discount = discount;
	}
	
	public OrdersEntity(UserEntity user, Double total_due, Double discount, AppliedOffersOrder appliedOffers) {
		super();
		this.user = user;
		this.total_due = total_due;
		this.discount = discount;
		this.aplAppliedOffersOrder = appliedOffers;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public AppliedOffersOrder getAplAppliedOffersOrder() {
		return aplAppliedOffersOrder;
	}

	public void setAplAppliedOffersOrder(AppliedOffersOrder aplAppliedOffersOrder) {
		this.aplAppliedOffersOrder = aplAppliedOffersOrder;
	}
}
