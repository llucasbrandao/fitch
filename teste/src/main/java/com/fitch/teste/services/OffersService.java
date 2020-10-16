package com.fitch.teste.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitch.teste.entities.AppliedOffersOrder;
import com.fitch.teste.entities.OrderIngredientsEntity;
import com.fitch.teste.entities.OrdersEntity;
import com.fitch.teste.exceptions.InvalidParameterException;
import com.fitch.teste.repositories.AppliedOfferOrderRepository;

@Service
public class OffersService {
	
	@Autowired
	private AppliedOfferOrderRepository appliedOfferOrderRepository;
	
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
	
	public AppliedOffersOrder findByOrder(OrdersEntity order) {
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
		
		// Pega a quantidade de cada ingrediente
		for (OrderIngredientsEntity ingred : orderIngredientsEntity) {
			switch (ingred.getIngredient().getName()) {
				case "Alface":
					promoQnt.put("Alface", promoQnt.get("Alface") + 1);
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
				
				case "Bacon":
					promoQnt.put("Bacon", promoQnt.get("Bacon") + 1);
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
					
				case "Hambúrguer":
					promoQnt.put("Hambúrguer", promoQnt.get("Hambúrguer") + 1);
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
					
					break;
					
				case "Queijo":
					promoQnt.put("Queijo", promoQnt.get("Queijo") + 1);
					ingredientsPrice.put(ingred.getIngredient().getName(), ingred.getIngredient().getPrice());
			}
		}
		
		// Define os descontos
		
		/*
		 * OBS: Não estava explícito no PDF do teste se os descontos são, ou não, cumulativos.
		 * Eu tratei como sendo cumulativos.
		 */
		if (promoQnt.get("Alface") != null && promoQnt.get("Bacon") == null)
			// Light: Se tem alface e não tem bacon, 10% de desconto.
			ordersEntity.setDiscount(ordersEntity.getTotal_due() * 10 / 100);
			ordersEntity.setTotal_due(ordersEntity.getTotal_due() - ordersEntity.getDiscount());
			ordersEntity.setAplAppliedOffersOrder(new AppliedOffersOrder(ordersEntity, "Light"));
		
		if (promoQnt.get("Hambúrguer") >= 3) {
			/*Muita carne: A cada 3 porções de hambúrguer o cliente só paga 2, a cada 6
			porções, o cliente pagará 4 e assim sucessivamente.*/
			
			/*
			 * O desconto final será calculado pela (quantidade do ingrediente no pedido / 3) * 2 * o preço do ingrediente.
			 */
			ordersEntity.setDiscount((promoQnt.get("Hambúrguer") / 3) * 2 * ingredientsPrice.get("Hambúrguer"));
			ordersEntity.setTotal_due(ordersEntity.getTotal_due() - ordersEntity.getDiscount());
			ordersEntity.setAplAppliedOffersOrder(new AppliedOffersOrder(ordersEntity, "Muita carne"));
			
		} 
		
		if (promoQnt.get("Queijo") >= 3) {
			/*Muito queijo: A cada 3 porções de queijo o cliente só paga 2, a cada 6
				porções, o cliente pagará 4 e assim sucessivamente.*/
			
			ordersEntity.setDiscount((promoQnt.get("Queijo") / 3) * 2 * ingredientsPrice.get("Queijo"));
			ordersEntity.setTotal_due(ordersEntity.getTotal_due() - ordersEntity.getDiscount());
			ordersEntity.setAplAppliedOffersOrder(new AppliedOffersOrder(ordersEntity, "Queijo"));
		}
		
		return ordersEntity;
	}
}
