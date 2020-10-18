package com.lucasbrandao.restaurantapi.repositories;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasbrandao.restaurantapi.entities.AppliedOffersOrder;
import com.lucasbrandao.restaurantapi.entities.OrdersEntity;

public interface AppliedOfferOrderRepository extends JpaRepository<AppliedOffersOrder, Long> {
	
	@Transactional
	Set<AppliedOffersOrder> findByOrder(OrdersEntity order);
}
