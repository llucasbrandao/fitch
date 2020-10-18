package com.lucasbrandao.restaurantapi.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// Ao buscar um pedido, já traz junto o usuário que o fez
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@CollectionTable(name = "users")
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	/*
	 * Usamos BigDecimal no total do pedido e no desconto, para definirmos a precisão de casas decimais.
	 */
	
	@Column(nullable = false, columnDefinition = "Decimal(10 , 2)")
	private BigDecimal total_due;
	
	@Column(columnDefinition = "Decimal(10 , 2)")
	private BigDecimal original_total;
	
	@Column(columnDefinition = "Decimal(10 , 2)")
	private BigDecimal discount;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@CollectionTable(name = "applied_offers")
	@JoinColumn(name = "offer_id")
	private AppliedOffersOrder aplAppliedOffersOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();

	public OrdersEntity() {}

	public OrdersEntity(UserEntity user, Double total_due, Double original_total, Double discount) {
		super();
		this.user = user;
		this.total_due = new BigDecimal(total_due);
		this.original_total = new BigDecimal(original_total);
		this.discount = new BigDecimal(discount);
	}
	
	public OrdersEntity(UserEntity user, Double total_due, Double discount, Double original_total, AppliedOffersOrder appliedOffers) {
		super();
		this.user = user;
		this.total_due = new BigDecimal(total_due);
		this.original_total = new BigDecimal(original_total);
		this.discount = new BigDecimal(discount);
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

	public Double getOriginal_total() {
		return original_total.doubleValue();
	}

	public void setOriginal_total(Double original_total) {
		this.original_total = new BigDecimal(original_total);
	}

	public Double getTotal_due() {
		return total_due.doubleValue();
	}

	public void setTotal_due(Double total_due) {
		this.total_due = new BigDecimal(total_due);
	}

	public Double getDiscount() {
		return discount.doubleValue();
	}

	public void setDiscount(Double discount) {
		this.discount = new BigDecimal(discount);
	}

	public AppliedOffersOrder getAplAppliedOffersOrder() {
		return aplAppliedOffersOrder;
	}

	public void setAplAppliedOffersOrder(AppliedOffersOrder aplAppliedOffersOrder) {
		this.aplAppliedOffersOrder = aplAppliedOffersOrder;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
