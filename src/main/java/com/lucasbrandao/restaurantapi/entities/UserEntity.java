package com.lucasbrandao.restaurantapi.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasbrandao.restaurantapi.enums.UserRoleEnum;

@Entity
@Table(name = "users", uniqueConstraints= @UniqueConstraint(columnNames = {"email"})) 
public class UserEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(nullable = false)
	private String first_name;
	
	@Column(nullable = false)
	private String last_name;
	
	@JsonProperty("email")
	@Column(unique = true, nullable = false)
	private String email;
	
	@JsonIgnore
	@Column(nullable = false)
	private String password;
	
	@JsonProperty("birthday")
	@Column(nullable = false)
	private Date birthday;
	
	// Ao buscar um usuário, já traz junto seus respectivos roles
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "users_roles")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id")
	private Set<Integer> roles = new HashSet<>();
	
	public UserEntity() {
		// Todo usuário recebe o role user, mesmo que ele também seja ADMIN
		addRole(UserRoleEnum.ROLE_USER);
	}
	
	public UserEntity(String first_name, String last_name, String email, String password, Date birthday, Set<Integer> role) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.roles = role;
	}
	
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Set<UserRoleEnum> getRole() {
		// O usuário pode ter mais de um role
		return roles.stream().map(x -> UserRoleEnum.toEnum(x)).collect(Collectors.toSet());
	}

	public void setRole(Set<Integer> role) {
		this.roles = role;
	}
	
	private void addRole(UserRoleEnum role) {
		this.roles.add(role.getCode());
	}
}
