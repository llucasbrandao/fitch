package com.lucasbrandao.restaurantapi.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lucasbrandao.restaurantapi.entities.AppliedOffersOrder;
import com.lucasbrandao.restaurantapi.entities.OrderIngredientsEntity;
import com.lucasbrandao.restaurantapi.entities.OrdersEntity;
import com.lucasbrandao.restaurantapi.exceptions.InvalidParameterException;
import com.lucasbrandao.restaurantapi.repositories.AppliedOfferOrderRepository;

@Service
public class OffersService {
	
	@Autowired
	private AppliedOfferOrderRepository appliedOfferOrderRepository;
	
	@Value("${IS_CUMMULATIVE_DISCOUNT}") // Define se o desconto deve ser cumulativo, ou não ## application.properties
	private boolean IS_CUMMULATIVE_DISCOUNT;
	
	public boolean save(AppliedOffersOrder applOffer) {
		if (applOffer != null && applOffer.getOrder() != null) 
			appliedOfferOrderRepository.save(applOffer);
		
		else 
			throw new InvalidParameterException("No offer to save or order is missing");
		
		return true;
	}
	
	public AppliedOffersOrder findByID(Long id) {
		if (id != null)
			return appliedOfferOrderRepository.findById(id).get();
		
		return null;
	}
	
	public Set<AppliedOffersOrder> findByOrder(OrdersEntity order) {
		if (order != null)
			return appliedOfferOrderRepository.findByOrder(order);
		
		return null;
	}
	
	/*
	 * Processa as promoções do pedido.
	 */
	public OrdersEntity applyDiscount(OrdersEntity ordersEntity, List<OrderIngredientsEntity> orderIngredientsEntity) {
		// Armazena quantas vezes um ingrediente aparece no pedido.
		Map<String, Integer> promoQnt = new HashMap<>();
		
		// Para conveniência, salvamos os preços dos ingredientes.
		Map<String, Double> ingredientsPrice = new HashMap<>();
		
		// Inicializamos as quantidades de cada ingrediente como 0. 
		promoQnt.put("Alface", 0);
		promoQnt.put("Bacon", 0);
		promoQnt.put("Hambúrguer", 0);
		promoQnt.put("Queijo", 0);
		
		// Define o preço inicial padrão
		ordersEntity.setOriginal_total(0.00);
		
		// Pega a quantidade de cada ingrediente
		for (OrderIngredientsEntity ingred : orderIngredientsEntity) {
			switch (ingred.getIngredient().getName()) {
				case "Alface":
					promoQnt.put(ingred.getIngredient().getName(), Integer.parseInt(ingred.getQuantity().toString()));
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
				
				case "Bacon":
					promoQnt.put(ingred.getIngredient().getName(), Integer.parseInt(ingred.getQuantity().toString()));
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
					
				case "Hambúrguer":
					promoQnt.put(ingred.getIngredient().getName(), Integer.parseInt(ingred.getQuantity().toString()));
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
					
				case "Queijo":
					promoQnt.put(ingred.getIngredient().getName(), Integer.parseInt(ingred.getQuantity().toString()));
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
			}
		}
		
		// Define o preço original total do pedido, antes de aplicar possíveis descontos.
		for (Map.Entry<String, Double> original_prices : ingredientsPrice.entrySet())
			ordersEntity.setOriginal_total(ordersEntity.getOriginal_total() + 
					(ingredientsPrice.get(original_prices.getKey()) * promoQnt.get(original_prices.getKey())));

		// Define os descontos
		// Com base na config IS_CUMMULATIVE_DISCOUNT, calcula o desconto cumulativo, ou não.
		return IS_CUMMULATIVE_DISCOUNT ? cummulativeDiscount(ordersEntity, orderIngredientsEntity, promoQnt, ingredientsPrice)
				: nonCummulativeDiscount(ordersEntity, orderIngredientsEntity, promoQnt, ingredientsPrice);
		
	}
	
	private OrdersEntity cummulativeDiscount(OrdersEntity ordersEntity, List<OrderIngredientsEntity> orderIngredientsEntity,
			Map<String, Integer> promoQnt, Map<String, Double> ingredientsPrice) {
		
		AppliedOffersOrder aplOffers;
		
		if (promoQnt.get("Alface") != null && promoQnt.get("Alface") > 0 && (promoQnt.get("Bacon") == null || promoQnt.get("Bacon") == 0)) {
			// Como este é o primeiro caso, não temos que pegar desconto já aplicado, porque ele ainda não existe
			// Light: Se tem alface e não tem bacon, 10% de desconto.
			
			ordersEntity.setDiscount((ordersEntity.getOriginal_total() * 10) / 100);
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Light", "Se tem alface e não tem bacon, 10% de desconto");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
		}
		
		if (promoQnt.get("Hambúrguer") != null && promoQnt.get("Hambúrguer") >= 3) {
			/*Muita carne: A cada 3 porções de hambúrguer o cliente só paga 2, a cada 6
			porções, o cliente pagará 4 e assim sucessivamente.*/
			
			/*
			 * O desconto será (quantidade do ingrediente no pedido / 3) * o preço do ingrediente
			 * e somado aos descontos aplicados anteriormente em outro ingrediente, se for o caso.
			 */
			ordersEntity.setDiscount(ordersEntity.getDiscount() + (promoQnt.get("Hambúrguer") / 3) * ingredientsPrice.get("Hambúrguer"));
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Muita Carne", "A cada 3 porções de hambúrguer o cliente só paga 2, a cada 6"
					+ "	porções, o cliente pagará 4 e assim sucessivamente.");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
			
		} 
		
		if (promoQnt.get("Queijo") != null && promoQnt.get("Queijo") >= 3) {
			/*Muito queijo: A cada 3 porções de queijo o cliente só paga 2, a cada 6
				porções, o cliente pagará 4 e assim sucessivamente.*/
			
			/*
			 * O desconto será (quantidade do ingrediente no pedido / 3) * o preço do ingrediente,
			 * somado aos descontos aplicados anteriormente em outro ingrediente, se for o caso.
			 */
			ordersEntity.setDiscount(ordersEntity.getDiscount() + (promoQnt.get("Queijo") / 3) * ingredientsPrice.get("Queijo"));
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Muito Queijo", "A cada 3 porções de queijo o cliente só paga 2, a cada 6"
					+ " porções, o cliente pagará 4 e assim sucessivamente.");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
		}
		
		// Aplica o desconto.
		ordersEntity.setTotal_due(ordersEntity.getOriginal_total() - ordersEntity.getDiscount());
		
		return ordersEntity;
		
	}
	
	private OrdersEntity nonCummulativeDiscount(OrdersEntity ordersEntity, List<OrderIngredientsEntity> orderIngredientsEntity,
			Map<String, Integer> promoQnt, Map<String, Double> ingredientsPrice) {
		
		AppliedOffersOrder aplOffers;
		
		if (promoQnt.get("Alface") != null && promoQnt.get("Alface") > 0 && (promoQnt.get("Bacon") == null || promoQnt.get("Bacon") == 0)) {
			// Light: Se tem alface e não tem bacon, 10% de desconto.
			
			// Aplica o desconto de 10% sobre o valor total
			ordersEntity.setDiscount((ordersEntity.getOriginal_total() * 10) / 100);
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Light", "Se tem alface e não tem bacon, 10% de desconto");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
			
		} else if (promoQnt.get("Hambúrguer") != null && promoQnt.get("Hambúrguer") >= 3) {
			/*Muita carne: A cada 3 porções de hambúrguer o cliente só paga 2, a cada 6
			porções, o cliente pagará 4 e assim sucessivamente.*/
			
			/*
			 * O desconto será calculado pela quantidade do ingrediente no pedido / 3 * o preço do ingrediente
			 */
			ordersEntity.setDiscount(promoQnt.get("Hambúrguer") / 3 * ingredientsPrice.get("Hambúrguer"));
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Muita Carne", "A cada 3 porções de hambúrguer o cliente só paga 2, a cada 6"
					+ "	porções, o cliente pagará 4 e assim sucessivamente.");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
			
		} else if (promoQnt.get("Queijo") != null && promoQnt.get("Queijo") >= 3) {
			/*Muito queijo: A cada 3 porções de queijo o cliente só paga 2, a cada 6
				porções, o cliente pagará 4 e assim sucessivamente.*/
			
			/*
			 * O desconto final será calculado pela quantidade do ingrediente no pedido / 3 * o preço do ingrediente
			 */
			
			ordersEntity.setDiscount(promoQnt.get("Queijo") / 3 * ingredientsPrice.get("Queijo"));
			
			aplOffers = new AppliedOffersOrder(ordersEntity, "Muito Queijo", "A cada 3 porções de queijo o cliente só paga 2, a cada 6"
					+ " porções, o cliente pagará 4 e assim sucessivamente.");
			
			ordersEntity.setAplAppliedOffersOrder(appliedOfferOrderRepository.save(aplOffers));
			
		} else 
			return ordersEntity; // Se chegar aqui, é porque nenhum desconto foi aplicado.
		
		// Aplica o desconto.
		ordersEntity.setTotal_due(ordersEntity.getOriginal_total() - ordersEntity.getDiscount());
		
		return ordersEntity;
	}
}
