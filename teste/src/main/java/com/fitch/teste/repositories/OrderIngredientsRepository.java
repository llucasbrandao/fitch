package com.fitch.teste.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitch.teste.entities.OrderIngredientsEntity;
import com.fitch.teste.entities.OrdersEntity;

public interface OrderIngredientsRepository extends JpaRepository<OrderIngredientsEntity, Long> {
	/*
	 * Buscamos os ingredientes através do pedido
	 */
	@Transactional
	<T> List<T> findAllByOrder(OrdersEntity order);
}