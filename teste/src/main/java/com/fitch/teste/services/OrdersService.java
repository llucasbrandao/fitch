package com.fitch.teste.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitch.teste.authentication.UserAuthentication;
import com.fitch.teste.dto.OrdersDTO;
import com.fitch.teste.dto.OrdersResponseDTO;
import com.fitch.teste.entities.IngredientsEntity;
import com.fitch.teste.entities.OrderIngredientsEntity;
import com.fitch.teste.entities.OrdersEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.exceptions.AuthorizationException;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.repositories.OrderIngredientsRepository;
import com.fitch.teste.repositories.OrdersRepository;

@Service
public class OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IngredientsService ingredientsService;
	
	@Autowired
	private OrderIngredientsRepository orderIngredientsRepository;
	
	public Long newOrder(OrdersDTO ordersDTO) {
		Set<Integer> ingredIntegers = new HashSet<>();
		
		OrdersEntity ordersEntity = fromDTO(ordersDTO);
		Long id = ordersRepository.save(ordersEntity).getId();
		
		//System.out.println(ordersEntity.getOrder_ingredients());
		
		for (Map<String, Object> map : ordersDTO.getIngredients())
			for (Map.Entry<String, Object> entry : map.entrySet())
				if (entry.getKey().equals("id"))
					ingredIntegers.add(Integer.parseInt(entry.getValue().toString()));
				
		for (Integer i : ingredIntegers) {
			for (int j = 0; j < ordersDTO.getIngredients().size(); j++) {
				if (Integer.parseInt(ordersDTO.getIngredients().get(j).get("id").toString()) == i) {
					orderIngredientsRepository.save(
							new OrderIngredientsEntity(ingredientsService.findByID(Integer.toUnsignedLong(i)).get(), ordersEntity, 
									Long.parseLong(ordersDTO.getIngredients().get(j).get("quantity").toString())							)
					);
				}
			}
		}

		return id;
	}
	
	public OrdersResponseDTO findOrderByID(Long id) {
		Optional<OrdersEntity> ordersEntity = ordersRepository.findById(id);
		UserAuthentication authUser = UserService.authenticated();
		
		if (ordersEntity.isPresent()) {
			if (authUser == null || !ordersEntity.get().getUser().getId().equals(authUser.getID()) && !authUser.hasRole(UserRoleEnum.ROLE_ADMIN))
				throw new AuthorizationException("Access denied. User is either not logged or is trying to access protected content");
			
			/**
			 * Transformarmos a entity num DTO especial, que retorna uma lista de Map<String, String>.
			 * É o nosso objeto chave-valor.
			 */
			OrdersResponseDTO ordersResponseDTO = fromEntity(ordersEntity.get()).get();
			List<Map<String, String>> ingredientsMapList = new ArrayList<>();
			
			for (Object o : orderIngredientsRepository.findAllByOrder(ordersEntity.get())) {
				Map<String, String> mapIngredients = new HashMap<>();
				
				OrderIngredientsEntity orderIngredientsEntity = (OrderIngredientsEntity) o;
				
				mapIngredients.put("ingredient_name", orderIngredientsEntity.getIngredient_id().getName());
				mapIngredients.put("price", orderIngredientsEntity.getIngredient_id().getPrice().toString());
				mapIngredients.put("quantity", orderIngredientsEntity.getQuantity().toString());
				
				ingredientsMapList.add(mapIngredients);
			}
			
			ordersResponseDTO.setOrder_ingredients(ingredientsMapList);
			
			return ordersResponseDTO;
		}
		
		throw new NotFoundException("No order was found with ID: " + id);
	}
	
	/**
	 * Recebe a lista dos ID's dos ingredientes usados no pedido, e devolve as entidades completas dos ingredientes.
	 * @param ingredients
	 * @return Lista de ingredientes
	 */
	public List<IngredientsEntity> getIngredients(Map<String, String> ingredients) {
		List<IngredientsEntity> ingredientsEntities = new ArrayList<>();
		
		for (Map.Entry<String, String> ingredient : ingredients.entrySet()) 
			if (ingredient.getKey().equals("id"))
				ingredientsEntities.add(ingredientsService.findByID(Long.parseLong(ingredient.getValue())).get());
		
		return ingredientsEntities;
	}
	
	public List<IngredientsEntity> getIngredients(Set<Integer> ingredients) {
		List<IngredientsEntity> ingredientsEntities = new ArrayList<>();
		
		for (Integer i : ingredients) 
			ingredientsEntities.add(ingredientsService.findByID(Integer.toUnsignedLong(i)).get());
		
		return ingredientsEntities;
	}
	
	public OrdersEntity fromDTO(OrdersDTO ordersDTO) {
		return new OrdersEntity(userService.findUserByID(UserService.authenticated().getID()).get(), 
				0.00, 0.00);
	}
	
	public Optional<OrdersResponseDTO> fromEntity(OrdersEntity ordersEntity) {
		return Optional.of(new OrdersResponseDTO(ordersEntity.getId(), ordersEntity.getUser(), ordersEntity.getTotal_due(), 
				ordersEntity.getDiscount()));
	}
}
