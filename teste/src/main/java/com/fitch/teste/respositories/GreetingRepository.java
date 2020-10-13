package com.fitch.teste.respositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitch.teste.entities.GreetingEntity;

public interface GreetingRepository extends JpaRepository<GreetingEntity, Long> {

}