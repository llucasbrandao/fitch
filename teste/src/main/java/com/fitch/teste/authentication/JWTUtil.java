package com.fitch.teste.authentication;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	public boolean isValidToken(String token) {
		Claims claims = getClaims(token);
		
		return claims != null && claims.getSubject() != null && claims.getExpiration() != null && new Date().before(claims.getExpiration());
			
	}
	
	public String getEmail(String token) {
		Claims claims = getClaims(token);
		
		return claims.getSubject();
	}

	private Claims getClaims(String token) {
		try {
			// Extrai e retorna os dados do token
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
			
		} catch (Exception e) {
			return null;
		}
	}
}
