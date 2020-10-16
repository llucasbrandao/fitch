package com.fitch.teste.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fitch.teste.entities.IngredientsEntity;
import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.repositories.IngredientsRepository;
import com.fitch.teste.repositories.UserRepository;

@Component
public class DBService {
	
	/*
	 * Estamos injetando os repositórios diretamente para evitarmos as validações executadas pelos respectivos Services,
	 * uma vez que nosso objetivo é apenas criar dados de teste.
	 */
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IngredientsRepository ingredientsRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	/**
	 * Usamos este método para criarmos dados iniciais de testes.
	 */
	
	@EventListener(ApplicationReadyEvent.class) // Roda o método assim que o Spring inicializar
	public void fakeData() {
		int userInserted = 0;
		int ingredientsInserted = 0;
		
		System.out.println("\n\tInserindo dados de testes...");
		
		Set<Integer> userRoles = new HashSet<>();
		
		userRoles.add(UserRoleEnum.ROLE_ADMIN.getCode());
		
		if (userRepository.findByEmail("admin@admin.com") == null) {
			UserEntity userEntity = new UserEntity("Usuário", "Admin", "admin@admin.com", pe.encode("123456"), new Date(), userRoles);
			
			userRepository.save(userEntity);
			
			System.out.println("\n\tAdmin de testes inserido com sucesso...\n");
			
			userInserted = 1;
			
		} 
		
		if (userRepository.findByEmail("user@user.com") == null) {
			userRoles.clear();
			
			userRoles.add(UserRoleEnum.ROLE_USER.getCode());
			
			UserEntity userEntity = new UserEntity("Usuário", "User", "user@user.com", pe.encode("123456"), new Date(), userRoles);
			
			userRepository.save(userEntity);
			
			System.out.println("\n\tUser de testes inserido com sucesso...\n");
			
			userInserted = 1;
		
		}
		
		if (userInserted == 0)
			System.out.println("\n\tUsuários de testes já existem...\n");
		
		/*
		 * Cria snacks e insere ingredientes no banco
		 */
		
		IngredientsEntity ingredientsEntity;
		
		// Alface
		if (ingredientsRepository.findByName("Alface") == null) {
			ingredientsEntity = new IngredientsEntity("Alface", Integer.toUnsignedLong(1000), 0.40);
			
			ingredientsRepository.save(ingredientsEntity);
			
			ingredientsInserted = 1;
		}
		
		// Bacon
		if (ingredientsRepository.findByName("Bacon") == null) {
			ingredientsEntity = new IngredientsEntity("Bacon", Integer.toUnsignedLong(1000), 2.00);
			
			ingredientsRepository.save(ingredientsEntity);
			
			ingredientsInserted = 1;
		}
		
		// Hambúrguer
		if (ingredientsRepository.findByName("Hambúrguer") == null) {
			ingredientsEntity = new IngredientsEntity("Hambúrguer", Integer.toUnsignedLong(1000), 3.00);
			
			ingredientsRepository.save(ingredientsEntity);
		}
		
		// Ovo
		if (ingredientsRepository.findByName("Ovo") == null) {
			ingredientsEntity = new IngredientsEntity("Ovo", Integer.toUnsignedLong(1000), 0.80);
			
			ingredientsRepository.save(ingredientsEntity);
			
			ingredientsInserted = 1;
		}
		
		// Queijo
		if (ingredientsRepository.findByName("Queijo") == null) {
			ingredientsEntity = new IngredientsEntity("Queijo", Integer.toUnsignedLong(1000), 1.50);
			
			ingredientsRepository.save(ingredientsEntity);
			
			ingredientsInserted = 1;
		}
		
		if (ingredientsInserted == 1)
			System.out.println("\tIngredientes criados com sucesso...\n");
		
		else System.out.println("\tIngredientes já existem...\n");
	}
	
}
