package com.fitch.teste.services;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fitch.teste.authentication.UserAuthentication;
import com.fitch.teste.dto.UserDTO;
import com.fitch.teste.entities.UserEntity;
import com.fitch.teste.enums.UserRoleEnum;
import com.fitch.teste.exceptions.AuthorizationException;
import com.fitch.teste.exceptions.NotFoundException;
import com.fitch.teste.exceptions.UserAlreadyExistsException;
import com.fitch.teste.respositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
	
	public Long saveUser(UserEntity user) throws UserAlreadyExistsException {
		if (this.userRepository.findByEmail(user.getEmail()) == null) {
			return this.userRepository.save(user).getId();
		}
		
		throw new UserAlreadyExistsException("User with email " + user.getEmail() + " is already registered.");
	}
	
	public static UserEntity fromDTO(UserDTO userDTO) {
		return new UserEntity(userDTO.getFirst_name(),userDTO.getLast_name(), userDTO.getEmail(), 
				pe.encode(userDTO.getPassword()), userDTO.getBirthday(), userDTO.getRole());
	}
	
	public Optional<UserEntity> findUserByID(Long id) {
		/*
		 * O usuário não pode pesquisar por ID's que não sejam o seu, a menos que ele seja ADMIN.
		 */
		if (authenticated() == null || !authenticated().hasRole(UserRoleEnum.ROLE_ADMIN) && !id.equals(authenticated().getID()))
			throw new AuthorizationException("Access denied. User is either not logged or is trying to access protected content");
		
		return userRepository.findById(id);
	}
	
	public UserEntity findUserByEmail(String email) {
		/*
		 * O usuário não pode pesquisar por ID's que não sejam o seu, a menos que ele seja ADMIN.
		 */
		if (authenticated() == null || !authenticated().hasRole(UserRoleEnum.ROLE_ADMIN) && !email.equals(authenticated().getUsername()))
			throw new AuthorizationException("Access denied. User is either not logged or is trying to access protected content");
		
		UserEntity user = userRepository.findByEmail(email);
		
		if (user == null)
			throw new NotFoundException("No user was found with email: " + email);
		
		return user;
	}
	
	public boolean delete(Long id) {
		if (authenticated() == null || !authenticated().hasRole(UserRoleEnum.ROLE_ADMIN) && !id.equals(authenticated().getID()))
			throw new AuthorizationException("Access denied. User is either not logged or is trying to perform a not allowed action");
		
		if (id == null)
			throw new InvalidParameterException("The 'id' parameter cannot be null");
		
		try {
			userRepository.deleteById(id);
			
			return true;
			
		} catch (Exception e) {
			throw new NotFoundException("No user was found with ID: " + id);
		}
	}
	
	/*
	 * Método que pega o usuário autenticado pelo Spring Security.
	 */
	public static UserAuthentication authenticated() {
		try {
			return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		} catch (Exception e) {
			return null;
		}
	}
}
