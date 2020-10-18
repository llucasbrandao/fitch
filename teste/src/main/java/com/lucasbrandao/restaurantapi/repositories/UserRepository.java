package com.fitch.teste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.fitch.teste.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	@Transactional
	UserEntity findByEmail(String email);
	
}
