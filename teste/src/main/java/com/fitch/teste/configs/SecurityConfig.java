package com.fitch.teste.configs;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fitch.teste.authentication.JWTAuthFilter;
import com.fitch.teste.authentication.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService; 
	
	@Autowired
	private JWTUtil jwtUtil;
	
	// Rotas abertas
	private static final String[] PUBLIC_ROUTES = {
			"/login"
	};
	
	// Rotas abertas que não permitem inserção de dados, apenas consultas
	private static final String[] PUBLIC_ROUTES_GET = {
			"/api/teste**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_ROUTES_GET).permitAll()
			.antMatchers(PUBLIC_ROUTES).permitAll()
			.anyRequest().authenticated();
		http.addFilter(new JWTAuthFilter(authenticationManager(), jwtUtil));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Como o sistema é stateless, não precisamos nos preocupar com CSFR
		
		http
	    .exceptionHandling()
	    .authenticationEntryPoint((request, response, e) -> 
	    {
	        response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.getWriter().write(new JSONObject() 
	                .put("timestamp", new Date())
	                .put("message", e.getMessage())
	                .toString());
	    });
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		/**
		 * O Spring usa uma interface para definir a autenticação via JWT.
		 * Injetamos a interface aqui, e o próprio framework procura onde está implementada essa interface.
		 */
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	/**
	 * Cofiguração para permitir acesso Cors de qualquer origem
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		
		return source;
	}
	
	// Bean que define o bcrypt, usado no configure do AuthenticationManagerBuilder, para hash do token JWT
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
