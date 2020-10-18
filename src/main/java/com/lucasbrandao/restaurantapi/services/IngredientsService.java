package com.lucasbrandao.restaurantapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasbrandao.restaurantapi.dto.IngredientsDTO;
import com.lucasbrandao.restaurantapi.entities.IngredientsEntity;
import com.lucasbrandao.restaurantapi.enums.UserRoleEnum;
import com.lucasbrandao.restaurantapi.exceptions.AuthorizationException;
import com.lucasbrandao.restaurantapi.exceptions.InvalidParameterException;
import com.lucasbrandao.restaurantapi.exceptions.NotFoundException;
import com.lucasbrandao.restaurantapi.repositories.IngredientsRepository;

@Service
public class IngredientsService {
	
	@Autowired
	private IngredientsRepository ingredientsRepository;
	
	public Long newIngredient(IngredientsEntity ingredientsEntity) {
		if (UserService.authenticated() != null && UserService.authenticated().hasRole(UserRoleEnum.ROLE_ADMIN)) 
			return this.ingredientsRepository.save(ingredientsEntity).getId();
		
		throw new AuthorizationException("Access denied. User is either not logged or is trying to perform a not allowed action");
	}
	
	public List<IngredientsEntity> findAll() {
		return ingredientsRepository.findAll();
	}
	
	public Optional<IngredientsEntity> findByID(Long id) {
		Optional<IngredientsEntity> ingredientsEntity = ingredientsRepository.findById(id);
		
		if (!ingredientsEntity.isPresent())
			throw new NotFoundException("No ingredient was found with ID: " + id);
		
		return ingredientsEntity;
	}
	
	public IngredientsEntity findByName(String name) {
		IngredientsEntity ingredientsEntity = ingredientsRepository.findByName(name);
		
		if (ingredientsEntity == null)
			throw new NotFoundException("No ingredient was found with name: " + name);
		
		return ingredientsEntity;
	}
	
	public boolean updatePrice(Long id, Double price) {
		if (UserService.authenticated() != null && UserService.authenticated().hasRole(UserRoleEnum.ROLE_ADMIN)) {
			if (id == null)
				throw new InvalidParameterException("The parameter 'id' is missing");
			
			/*
			 * Estamos usando o @DynamicUpdate para atualizar apenas os campos que forem alterados.
			 * Pegamos o registro "corrente" e fazemos as alterações nele. Em seguida, mandamos salvar.
			 */
			IngredientsEntity current = ingredientsRepository.getOne(id);
			
			current.setPrice(price);
			
			ingredientsRepository.save(current);
			
			return true;
		}
		
		throw new AuthorizationException("Access denied. User is either not logged or is trying to perform a not allowed action");
	}
	
	public boolean delete(Long id) {
		if (UserService.authenticated() != null && UserService.authenticated().hasRole(UserRoleEnum.ROLE_ADMIN)) 
			throw new AuthorizationException("Access denied. User is either not logged or is trying to perform a not allowed action");
		
		Optional<IngredientsEntity> ingredientsEntity = ingredientsRepository.findById(id);
		
		if (!ingredientsEntity.isPresent())
			throw new NotFoundException("No ingredient was found with name: " + id);
		
		ingredientsRepository.delete(ingredientsEntity.get());
		
		return true;
	}
	
	// Converte do DTO para entidade
	public static IngredientsEntity fromDTO(IngredientsDTO ingredientsDTO) {
		return new IngredientsEntity(ingredientsDTO.getName(), ingredientsDTO.getAvailable_quantity(), ingredientsDTO.getPrice());
	}
}
