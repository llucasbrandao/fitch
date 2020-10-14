package com.fitch.teste.respositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitch.teste.entities.OrdersEntity;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

}
