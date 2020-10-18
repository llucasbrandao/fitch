package com.lucasbrandao.restaurantapi.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasbrandao.restaurantapi.entities.OrderIngredientsEntity;
import com.lucasbrandao.restaurantapi.entities.OrdersEntity;

public interface OrderIngredientsRepository extends JpaRepository<OrderIngredientsEntity, Long> {
	/*
	 * Buscamos os ingredientes através do pedido
	 */
	@Transactional
	<T> List<T> findAllByOrder(OrdersEntity order);
}