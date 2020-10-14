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
import com.fitch.teste.respositories.UserRepository;

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
		System.out.println("\n\tInserindo dados de testes...");
		
		Set<Integer> userRoles = new HashSet<>();
		
		userRoles.add(UserRoleEnum.ROLE_ADMIN.getCode());
		
		if (userRepository.findByEmail("llucasbrandao@gmail.com") == null) {
			UserEntity userEntity = new UserEntity("Lucas", "Brandão", "llucasbrandao@gmail.com", pe.encode("123456"), new Date(), userRoles);
			
			userRepository.save(userEntity);
			
			System.out.println("\n\tDados de testes inseridos com sucesso...\n");
			
		} else 
			System.out.println("\n\tDados de testes já existem...\n");
	}
}
