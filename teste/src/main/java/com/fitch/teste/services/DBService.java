package com.fitch.teste.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.respositories.UserRepository;

@Component
public class DBService {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Usamos este método para criarmos dados de testes
	 */
	
	@EventListener(ApplicationReadyEvent.class)
	public void fakeData() {
		System.out.println("Inserindo dados de testes...");
		
		Set<Integer> userRoles = new HashSet<>();
		
		userRoles.add(UserRoleEnum.ADMIN.getCode());
		
		UserEntity userEntity = new UserEntity("Lucas", "Brandão", "llucasbrandao@gmail.com", "123456", new Date(), userRoles);
		
		//userRepository.save(userEntity);
	}
}
