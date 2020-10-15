package com.fitch.teste.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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
	
	private static SessionFactory sessionFactory;
	
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

	/*
	 * Os dois métodos abaixo são usados para criarmos uma Sessão do Hibernate e a usarmos com o Criteria, para buscas mais flexíveis.
	 */
    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().
                configure("hibernate.cfg.xml").build();

        Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().
                build();

        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

        SessionFactory sessionFactory = sessionFactoryBuilder.build();

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        
        return sessionFactory;
    }
	
}
