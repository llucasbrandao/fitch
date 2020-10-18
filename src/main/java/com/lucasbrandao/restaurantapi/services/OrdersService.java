package com.lucasbrandao.restaurantapi.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasbrandao.restaurantapi.authentication.UserAuthentication;
import com.lucasbrandao.restaurantapi.dto.OrdersDTO;
import com.lucasbrandao.restaurantapi.dto.OrdersResponseDTO;
import com.lucasbrandao.restaurantapi.entities.AppliedOffersOrder;
import com.lucasbrandao.restaurantapi.entities.IngredientsEntity;
import com.lucasbrandao.restaurantapi.entities.OrderIngredientsEntity;
import com.lucasbrandao.restaurantapi.entities.OrdersEntity;
import com.lucasbrandao.restaurantapi.entities.SnacksEntity;
import com.lucasbrandao.restaurantapi.enums.UserRoleEnum;
import com.lucasbrandao.restaurantapi.exceptions.AuthorizationException;
import com.lucasbrandao.restaurantapi.exceptions.InvalidParameterException;
import com.lucasbrandao.restaurantapi.exceptions.NotFoundException;
import com.lucasbrandao.restaurantapi.repositories.OrderIngredientsRepository;
import com.lucasbrandao.restaurantapi.repositories.OrdersRepository;

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
		
		Long finalOrderID = Integer.toUnsignedLong(0);

		/*
		 * Verifica se o pedido é de um lanche pronto.
		 * Não sendo, processa os ingredientes.
		 */
		if (ordersDTO.getSnack() != null && ordersDTO.getSnack() != "") {
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
			
			ordersEntity.setOriginal_total(total_due);
			ordersEntity.setTotal_due(total_due);
			
			/*
			 * Salvamos o pedido e o snack.
			 */
			
			ordersRepository.save(ordersEntity);
			
			orderIngredientsRepository.save(
					new OrderIngredientsEntity(
							ordersDTO.getSnack(), 
							ordersEntity, 
							Integer.toUnsignedLong(ordersDTO.getSnack_qnt() != null ? ordersDTO.getSnack_qnt() : 1)));
		
		} else if (ordersDTO.getIngredients() != null) {
			/*
			 * Adicionamos os ingredientes ao pedido, a partir do DTO.
			 */
			for (Map<String, Object> map : ordersDTO.getIngredients()) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					// Pega o ingrediente a partir do campo id
					if (entry.getKey().equals("id"))
						try {
							/*
							 * Verifica para que não haja valores repetidos.
							 */
							if (!ingredIntegers.contains(Integer.parseInt(entry.getValue().toString())))
								ingredIntegers.add(Integer.parseInt(entry.getValue().toString()));
							
						} catch (Exception e) {
							throw new InvalidParameterException("The provided ID is invalid.");
							
						}
				}
			}
			
			// O usuário não informou nenhum ID, ou o(s) id(s) são inválido(s).
			if (ingredIntegers.size() == 0)
				throw new InvalidParameterException("No ingredient ID was found. "
						+ "Please double check your order and be sure to inform at least one valid ingredient ID.");
			
			/*
			 * Salvamos os ingredientes do pedido no DB.
			 */
			
			// Lista auxiliar para calcularmos descontos baseados em ingredientes.
			List<OrderIngredientsEntity> orderIngredientsEntities = new ArrayList<>();
			
			for (Integer i : ingredIntegers) {
				for (int j = 0; j < ingredIntegers.size(); j++) {
					// Verificamos se o ID do Map equivale ao do ingrediente em questão.
					// Sendo, adicionamos o ingrediente.
					if (Integer.parseInt(ordersDTO.getIngredients().get(j).get("id").toString()) == i) {
						tempIngredientsEntity = ingredientsService.findByID(Integer.toUnsignedLong(i)).get();
						
						orderIngredientsEntities.add(new OrderIngredientsEntity(tempIngredientsEntity, ordersEntity, 
								Long.parseLong(ordersDTO.getIngredients().get(j).get("quantity").toString())));
						
					}
				}
			}
			
			// Validação de desconto, se aplicável.
			finalOrderID = ordersRepository.save(ordersEntity).getId();
			
			/*
			 * Verificamos e aplicamos possíveis descontos.
			 * O preços totais e originais (antes de aplicar desconto) são definidos no offersService.applyDiscount().
			 */
			ordersRepository.save(offersService.applyDiscount(ordersRepository.findById(finalOrderID).get(), orderIngredientsEntities));
			
			// Salvamos os ingredientes do pedido no banco.
			for (OrderIngredientsEntity orderIngred : orderIngredientsEntities)
				orderIngredientsRepository.save(orderIngred);
			
		} else throw new InvalidParameterException("You must inform either 'snack' name or the ingredients list"); 
		
		return finalOrderID;
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
				if (orderIngredientsEntity.getSnack() != null && orderIngredientsEntity.getSnack() != "") {
					mapIngredients.put("snack", orderIngredientsEntity.getSnack());
					
					for (SnacksEntity snack :  snacksService.generateDummySnacks()) 
						if (snack.getName().equals(orderIngredientsEntity.getSnack())) {
							mapIngredients.put("price", String.valueOf(calculateSnackPrice(snack)));
							mapIngredients.put("quantity", orderIngredientsEntity.getQuantity().toString());
						}
				
				} else {
					mapIngredients.put("ingredient_name", orderIngredientsEntity.getIngredient().getName());
					mapIngredients.put("price", orderIngredientsEntity.getIngredient().getPrice().toString());
					mapIngredients.put("quantity", orderIngredientsEntity.getQuantity().toString());
				}
				
				ingredientsMapList.add(mapIngredients);
			}
			
			ordersResponseDTO.setOrder_ingredients(ingredientsMapList);
			
			Set<AppliedOffersOrder> offersOrders = offersService.findByOrder(ordersEntity.get());
			List<Map<String, String>> offersList = new ArrayList<>();
			Map<String, String> offersDTO = new HashMap<>();
			
			for (AppliedOffersOrder offer : offersOrders)
				offersDTO.put(offer.getName(), offer.getDescription());
			
			offersList.add(offersDTO);
			
			ordersResponseDTO.setOrder_offers(offersList);
			ordersResponseDTO.setOriginal_price(ordersEntity.get().getOriginal_total());
			ordersResponseDTO.setCreated_at(ordersEntity.get().getCreated_at());
			
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
				0.00, 0.00, 0.00);
	}
	
	public Optional<OrdersResponseDTO> fromEntity(OrdersEntity ordersEntity) {
		return Optional.of(new OrdersResponseDTO(ordersEntity.getId(), ordersEntity.getUser(), ordersEntity.getTotal_due(), 
				ordersEntity.getDiscount()));
	}
}
