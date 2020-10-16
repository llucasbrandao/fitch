package com.fitch.teste.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitch.teste.entities.AppliedOffersOrder;
import com.fitch.teste.entities.OrdersEntity;

public interface AppliedOfferOrderRepository extends JpaRepository<AppliedOffersOrder, Long> {
	
	@Transactional
	AppliedOffersOrder findByOrder(OrdersEntity order);
}
