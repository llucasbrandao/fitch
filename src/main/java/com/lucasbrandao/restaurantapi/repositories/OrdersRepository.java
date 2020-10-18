package com.lucasbrandao.restaurantapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasbrandao.restaurantapi.entities.OrdersEntity;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {
	
}
