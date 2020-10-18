package com.lucasbrandao.restaurantapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucasbrandao.restaurantapi.authentication.UserAuthentication;
import com.lucasbrandao.restaurantapi.entities.UserEntity;
import com.lucasbrandao.restaurantapi.repositories.UserRepository;

/*
 * Classe que implementa o contrato de autenticação do usuário padrão do Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Usamos o email, ao invés do username
		UserEntity user = repository.findByEmail(email);
		
		if (user == null)
			throw new UsernameNotFoundException("User not found with email: " + email);
		
		return new UserAuthentication(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
	}
}
