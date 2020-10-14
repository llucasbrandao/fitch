package com.fitch.teste.authentication;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fitch.teste.enums.UserRoleEnum;

public class UserAuthentication implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserAuthentication() {}

	
	public UserAuthentication(Long id, String email, String password,
			Set<UserRoleEnum> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = roles.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
	}


	public Long getID() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
