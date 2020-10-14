package com.fitch.teste.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitch.teste.authentication.UserAuthentication;
import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.respositories.UserRepository;

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
