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
import com.fitch.teste.entities.SnacksEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.exceptions.AuthorizationException;
import com.fitch.teste.exceptions.InvalidParameterException;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.repositories.OrderIngredientsRepository;
import com.fitch.teste.repositories.OrdersRepository;

@Service
public class OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private OrderIngredientsRepository orderIngredientsRepository;
	
	@Autowired
	private OffersService offersService;
	
	@Autowired
	private IngredientsService ingredientsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SnacksService snacksService;
	
	public Long newOrder(OrdersDTO ordersDTO) {
		// Este objeto terá os IDs dos ingredientes do pedido.
		Set<Integer> ingredIntegers = new HashSet<>();
		IngredientsEntity tempIngredientsEntity;
		OrdersEntity ordersEntity = fromDTO(ordersDTO);

		/*
		 * Extrai o id do Map, que refere-se aos ingredientes constantes no pedido,
		 * e adiciona-os a um Set, verificando para que não haja valores repetidos.
		 */
		if (ordersDTO.getSnack() != "") {
			boolean foundSnack = false;
			double total_due = 0;
			
			for (SnacksEntity snack : snacksService.generateDummySnacks()) {
				if (snack.getName().equals(ordersDTO.getSnack())) {
					foundSnack = true;
					total_due = calculateSnackPrice(snack);
				}
			}
			
			if (!foundSnack)
				throw new NotFoundException("No snack was found with name: " + ordersDTO.getSnack());
			
			ordersEntity.setTotal_due(total_due);
			
			/*
			 * Salvamos o pedido e o snack.
			 */
			
			orderIngredientsRepository.save(
					new OrderIngredientsEntity(
							ordersDTO.getSnack(), 
							ordersDTO.getSnack_qnt() != null ? ordersDTO.getSnack_qnt() : 1, // Quantidade padrão é 1
							ordersEntity, 
							Integer.toUnsignedLong(1)));
		
		} else if (ordersDTO.getIngredients() != null) {
			/*
			 * Adicionamos os ingredientes ao pedido, a partir do DTO.
			 */
			for (Map<String, Object> map : ordersDTO.getIngredients())
				for (Map.Entry<String, Object> entry : map.entrySet())
					if (entry.getKey().equals("id")) 
						if (!ingredIntegers.contains(Integer.parseInt(entry.getValue().toString())))
							ingredIntegers.add(Integer.parseInt(entry.getValue().toString()));
			
			/*
			 * Salvamos os ingredientes do pedido no DB.
			 */
			
			// Lista auxiliar para calcularmos descontos baseados em ingredientes.
			List<OrderIngredientsEntity> orderIngredientsEntities = new ArrayList<>();
			
			for (Integer i : ingredIntegers) {
				for (int j = 0; j < ingredIntegers.size(); j++) {
					// Verificamos se o ID do Map equivale ao do ingrediente em questão.
					// Sendo, salvamos o pedido.
					if (Integer.parseInt(ordersDTO.getIngredients().get(j).get("id").toString()) == i) {
						tempIngredientsEntity = ingredientsService.findByID(Integer.toUnsignedLong(i)).get();
						orderIngredientsEntities.add(new OrderIngredientsEntity(tempIngredientsEntity, ordersEntity, 
								Long.parseLong(ordersDTO.getIngredients().get(j).get("quantity").toString())));
						
					}
				}
			}
			
			// Validação de desconto, se aplicável.
			offersService.applyDiscount(ordersEntity, orderIngredientsEntities);
			
			// Salvamos os ingredientes do pedido no banco.
			for (OrderIngredientsEntity orderIngred : orderIngredientsEntities)
				orderIngredientsRepository.save(orderIngred);
			
		} else throw new InvalidParameterException("You must inform either 'snack' name or the ingredients list"); 
		
		return ordersRepository.save(ordersEntity).getId();
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
				
				/*
				 * Monta o response DTO conforme o pedido.
				 * Verifica se é snack, ou avulso.
				 */
				if (orderIngredientsEntity.getSnack() != "") {
					mapIngredients.put("snack", orderIngredientsEntity.getSnack());
					
					for (SnacksEntity snack :  snacksService.generateDummySnacks()) 
						if (snack.getName().equals(orderIngredientsEntity.getSnack())) {
							mapIngredients.put("price", String.valueOf(calculateSnackPrice(snack)));
							mapIngredients.put("quantity", orderIngredientsEntity.getSnack_qnt().toString());
						}
				
				} else {
					mapIngredients.put("ingredient_name", orderIngredientsEntity.getIngredient().getName());
					mapIngredients.put("price", orderIngredientsEntity.getIngredient().getPrice().toString());
					mapIngredients.put("quantity", orderIngredientsEntity.getQuantity().toString());
				}
				
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
	
	// Calcula o preço dos lanches com base em seus ingredientes.
	public static double calculateSnackPrice(SnacksEntity snackEntity) {
		double snackPrice = 0;
		
		for (IngredientsEntity ingred : snackEntity.getIngredients())
			snackPrice += ingred.getPrice();
		
		return snackPrice;
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
