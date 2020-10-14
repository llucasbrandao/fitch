package com.fitch.teste.authentication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author Lucas Brandão
 * Componente responsável por gerar e validar os tokens JWT
 */

@Component
public class JWTUtil {
	/**
	 * O secret e o tempo de expiração do token vêm do application.properties, injetados pelo @Value()
	 */
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiry}")
	private Long expiration;
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
}
