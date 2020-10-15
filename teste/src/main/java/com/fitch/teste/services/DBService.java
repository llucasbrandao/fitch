package com.fitch.teste.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.repositories.UserRepository;

@Component
public class DBService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	/**
	 * Usamos este método para criarmos dados de testes
	 */
	
	@EventListener(ApplicationReadyEvent.class) // Roda o método assim que o Spring inicializar
	public void fakeData() {
		int userInserted = 0;
		
		System.out.println("\n\tInserindo dados de testes...");
		
		Set<Integer> userRoles = new HashSet<>();
		
		userRoles.add(UserRoleEnum.ROLE_ADMIN.getCode());
		
		if (userRepository.findByEmail("llucasbrandao@admin.com") == null) {
			UserEntity userEntity = new UserEntity("Lucas", "Brandão", "llucasbrandao@admin.com", pe.encode("123456"), new Date(), userRoles);
			
			userRepository.save(userEntity);
			
			System.out.println("\n\tAdmin de testes inserido com sucesso...\n");
			
			userInserted = 1;
			
		} 
		
		if (userRepository.findByEmail("llucasbrandao@user.com") == null) {
			userRoles.clear();
			
			userRoles.add(UserRoleEnum.ROLE_USER.getCode());
			
			UserEntity userEntity = new UserEntity("Lucas", "Brandão", "llucasbrandao@user.com", pe.encode("123456"), new Date(), userRoles);
			
			userRepository.save(userEntity);
			
			System.out.println("\n\tUser de testes inserido com sucesso...\n");
			
			userInserted = 1;
		
		}
		
		if (userInserted == 0)
			System.out.println("\n\tDados de testes já existem...\n");
	}
}
