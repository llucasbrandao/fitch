package com.fitch.teste.entities;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitch.teste.enums.UserRoleEnum;

@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"email"})) 
public class UserEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@JsonProperty("first_name")
	@Length(min =  2, max = 30, message = "first_name must have at least 2 characters and max 30")
	private String first_name;
	
	@JsonProperty("last_name")
	@Length(min =  2, max = 30, message = "last_name must have at least 2 characters and max 30")
	private String last_name;
	
	@JsonProperty("email")
	@Email(message = "Invalid email address")
	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	@Length(min =  2, max = 80, message = "password must have at least 8 characters and max 80")
	private String password;
	
	@JsonProperty("birthday")
	@NotNull(message = "birthday must not be empty")
	private Date birthday;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "UserRoleEnum")
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

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
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
		return roles.stream().map(x -> UserRoleEnum.toEnum(x)).collect(Collectors.toSet());
	}

	public void setRole(Set<Integer> role) {
		this.roles = role;
	}
	
	private void addRole(UserRoleEnum role) {
		this.roles.add(role.getCode());
	}
}
