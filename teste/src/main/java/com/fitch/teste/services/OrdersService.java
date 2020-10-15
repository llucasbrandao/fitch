package com.fitch.teste.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitch.teste.dto.OrdersDTO;
import com.fitch.teste.entities.OrdersEntity;
import com.fitch.teste.respositories.OrdersRepository;

@Service
public class OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private UserService userService;
	
	public Long newOrder(OrdersDTO ordersDTO) {
		//System.out.println(fromDTO(ordersDTO).getIngredients());
		return Integer.toUnsignedLong(0);//ordersRepository.save(fromDTO(ordersDTO)).getId();
	}
	
	public OrdersEntity fromDTO(OrdersDTO ordersDTO) {
		return new OrdersEntity(userService.findUserByID(UserService.authenticated().getID()).get(), 0.00, ordersDTO.getIngredients(), 0.00);
	}
}
