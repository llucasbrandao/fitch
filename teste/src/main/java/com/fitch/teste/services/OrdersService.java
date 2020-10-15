package com.fitch.teste.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitch.teste.authentication.UserAuthentication;
import com.fitch.teste.dto.OrdersDTO;
import com.fitch.teste.dto.OrdersResponseDTO;
import com.fitch.teste.entities.IngredientsEntity;
import com.fitch.teste.entities.OrdersEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.exceptions.AuthorizationException;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.repositories.OrdersRepository;

@Service
public class OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IngredientsService ingredientsService;
	
	public Long newOrder(OrdersDTO ordersDTO) {
		return ordersRepository.save(fromDTO(ordersDTO)).getId();
	}
	
	public OrdersResponseDTO findOrderByID(Long id) {
		Optional<OrdersEntity> ordersEntity = ordersRepository.findById(id);
		UserAuthentication authUser = UserService.authenticated();
		
		if (ordersEntity.isPresent()) {
			if (authUser == null || !ordersEntity.get().getUser().getId().equals(authUser.getID()) && !authUser.hasRole(UserRoleEnum.ROLE_ADMIN))
				throw new AuthorizationException("Access denied. User is either not logged or is trying to access protected content");
			
			/**
			 * Transformarmos a entity num DTO especial.
			 * A Entity tem o campo Set<Integer> ingredients, e queremos retornar Set<IngredientsEntity>.
			 * O DTO possui o campo que queremos retornar.
			 */
			OrdersResponseDTO ordersResponseDTO = fromEntity(ordersEntity.get()).get();
			
			/*
			 * Percorremos a lista de IDs dos ingredientes e buscamos um a um no banco.
			 */
			ordersResponseDTO.setOrder_ingredients(getIngredients(ordersEntity.get().getIngredients()));
			
			return ordersResponseDTO;
		}
		
		throw new NotFoundException("No order was found with ID: " + id);
	}
	
	public Set<IngredientsEntity> getIngredients(Set<Integer> ingredients) {
		Set<IngredientsEntity> ingredientsEntities = new HashSet<>();
		
		for (Integer i : ingredients)
			ingredientsEntities.add(ingredientsService.findByID(Integer.toUnsignedLong(i)).get());
		
		return ingredientsEntities;
	}
	
	public OrdersEntity fromDTO(OrdersDTO ordersDTO) {
		return new OrdersEntity(userService.findUserByID(UserService.authenticated().getID()).get(), 0.00, ordersDTO.getIngredients(), 0.00);
	}
	
	public Optional<OrdersResponseDTO> fromEntity(OrdersEntity ordersEntity) {
		return Optional.of(new OrdersResponseDTO(ordersEntity.getId(), ordersEntity.getUser(), ordersEntity.getTotal_due(), 
				ordersEntity.getDiscount()));
	}
}
